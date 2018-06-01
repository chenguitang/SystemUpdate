package com.posin.systemupdate.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.posin.systemupdate.R;
import com.posin.systemupdate.adapter.FileBrowserAdapter;
import com.posin.systemupdate.bean.FilePathItem;
import com.posin.systemupdate.utils.MiniposInfo;
import com.posin.systemupdate.utils.UDiskUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * FileName: FileBrowserDialog
 * Author: Greetty
 * Time: 2018/5/29 20:15
 * Desc: TODO
 */

public abstract class FileBrowserDialog implements FileFilter {

    private static final String TAG = "FileBrowserDialog";

    private final LayoutInflater mLayoutInflater;

    private final Bitmap mIconDir;
    private final Bitmap mIconFile;

    // for file filter
    private final String[] mExt;
    private final boolean mShowHidden;

    private final View mView;
    private final AlertDialog mDlg;

    private final ArrayList<FilePathItem> mPaths = new ArrayList<FilePathItem>();

    private String mRootPath = "/mnt";
    private File mCurrentSelection = null;
    private TextView mPath;
    private TextView mSelectedFile;
    private boolean mChooseFile = true;
    private Button mBtnConfirm;
    private Button mBtnGoUp;

    private ListView mListView = null;

    private FileBrowserAdapter mListAdapter = null;

    private View mSeletedView = null;

    private final String[] ROOT_PATHS_NAME;

    private HashMap<String, File> ROOT_PATHS = null;

    public abstract boolean onOk(File path);

    public abstract void onCancel();

    public FileBrowserDialog(Context context, String title, String path, String[] fileExt, boolean chooseFile,
                             boolean showHidden) throws IOException {

        ROOT_PATHS = new HashMap<String, File>();
        ROOT_PATHS = UDiskUtils.initRootPath(context);

        mListAdapter = new FileBrowserAdapter(context, mPaths);
        ROOT_PATHS_NAME = new String[]{
                context.getString(R.string.str_internal_storage),
                context.getString(R.string.str_external_storage),
                context.getString(R.string.str_usb_storage),
        };

        mChooseFile = chooseFile;
        mShowHidden = showHidden;
        mExt = fileExt;

        mIconDir = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.folder);
        mIconFile = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.doc);

        mLayoutInflater = LayoutInflater.from(context);
        mView = mLayoutInflater.inflate(R.layout.file_browser_view, null);


        mPath = (TextView) mView.findViewById(R.id.text_current_path);
        mSelectedFile = (TextView) mView.findViewById(R.id.txt_selected_file);

        mListView = (ListView) mView.findViewById(R.id.list);
        mListView.setAdapter(mListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position,
                                    long id) {
                File file = mPaths.get(position).getPath();
                onSelected(file, view);
            }
        });

        mBtnGoUp = (Button) mView.findViewById(R.id.btn_go_up);

        mBtnGoUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curr = mPath.getText().toString();

                if (mChooseFile) {
                    mBtnConfirm.setEnabled(false);
                } else {
                    mCurrentSelection = new File(curr);
                    mBtnConfirm.setEnabled(true);
                }

                if (mRootPath.equals(curr))
                    return;

                File path = new File(curr);
                if (path.isDirectory()) {
                    String parent = path.getParent();
                    if ("/".equals(parent))
                        return;

                    getFileDir(path.getParentFile());
                }
            }
        });

        Button btn = (Button) mView.findViewById(R.id.btn_go_root);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFileDir(new File(mRootPath));

                if (mChooseFile) {
                    mBtnConfirm.setEnabled(false);
                } else {
                    mCurrentSelection = new File(mRootPath);
                    mBtnConfirm.setEnabled(true);
                }

            }
        });

        //getFileDir(mRootPath);
        if (path != null)
            getFileDir(new File(path));

        mDlg = (new AlertDialog.Builder(context))
                .setTitle(title)
                .setView(mView)
                .setInverseBackgroundForced(true)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onOk(mCurrentSelection)) {
                            mDlg.dismiss();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDlg.dismiss();
                        onCancel();
                    }
                })
                .show();

        mBtnConfirm = mDlg.getButton(DialogInterface.BUTTON_POSITIVE);
        mBtnConfirm.setEnabled(false);

        scaleViewSize(mDlg.getWindow(), 0.8f, 0.8f);
    }

    private static void scaleViewSize(Window win, float scaleX, float scaleY) {
        WindowManager windowManager = win.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = win.getAttributes();
        Point outSize = new Point();

        display.getSize(outSize);
        lp.width = (int) (outSize.x * scaleX);
        lp.height = (int) (outSize.y * scaleY);
        win.setAttributes(lp);

    }

    private void onSelected(File path, View view) {

        if (path == null) {

            mSeletedView = null;
            mCurrentSelection = null;
            mSelectedFile.setText("");

            return;
        }

        Log.d(TAG, "select " + path.getAbsolutePath());

        mCurrentSelection = path;
        mSelectedFile.setText("");


        if (path.isDirectory()) {
            Log.e(TAG, "is directory.");

            getFileDir(path);
            mBtnConfirm.setEnabled(!mChooseFile);

            if (!mChooseFile) {
                mSelectedFile.setText(path.getAbsolutePath());
            }

        } else {
            Log.d(TAG, "is file.");
            if (mChooseFile) {
                mBtnConfirm.setEnabled(true);
                if (mSeletedView != null)
                    mSeletedView.setBackgroundColor(Color.WHITE);
                mSeletedView = view;
                view.setBackgroundColor(Color.LTGRAY);
                mSelectedFile.setText(path.getAbsolutePath());
            } else {
                // 不是选中文件， 选择文件夹
                // mBtnConfirm 一直是 enabled
            }
        }

    }

    @Override
    public boolean accept(File pathname) {
        if (pathname.isHidden() && !mShowHidden)
            return false;

        if (pathname.isDirectory()) {
            return true;
        }
        for (int i = 0; i < mExt.length; i++) {
            if (pathname.getName().endsWith(mExt[i])) {
                return true;
            }
        }
        return false;
    }

    private static void sortInsert(List<File> lst, int start, File f) {
        for (int i = start; i < lst.size(); i++) {
            if (lst.get(i).getName().compareTo(f.getName()) >= 0) {
                lst.add(i, f);
                return;
            }
        }
        lst.add(f);
    }

    private static File[] sortFiles(File[] fs) {
        if (fs == null)
            return null;

        ArrayList<File> lst = new ArrayList<File>();
        for (int i = 0; i < fs.length; i++)
            if (fs[i].isDirectory())
                sortInsert(lst, 0, fs[i]);
        int start = lst.size();
        for (int i = 0; i < fs.length; i++)
            if (!fs[i].isDirectory())
                sortInsert(lst, start, fs[i]);

        if (lst.size() != 0)
            return lst.toArray(fs);
        return null;
    }

    private void getFileDir(File filePath) {

        onSelected(null, null);
        mPath.setText(filePath.getAbsolutePath());
        mPaths.clear();
        if (mRootPath.equals(filePath.getAbsolutePath())) {

            for (Map.Entry<String, File> e : ROOT_PATHS.entrySet()) {
                File file = e.getValue();
                FilePathItem item = new FilePathItem();
                item.setPath(file);
                item.setDisplay(e.getKey());//ROOT_PATHS_NAME[i];
                item.setView(null);
                item.setIcon(mIconDir);
                mPaths.add(item);
            }
        } else {

            File[] files;

            if (mExt != null) {
                files = sortFiles(filePath.listFiles(this));
            } else {
                files = sortFiles(filePath.listFiles());
            }

            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];

                    FilePathItem item = new FilePathItem();
                    item.setPath(file);
                    item.setDisplay(file.getName());
                    item.setView(null);
                    item.setIcon(file.isDirectory() ? mIconDir : mIconFile);
                    mPaths.add(item);
                }
            }
        }

        mListAdapter.notifyDataSetChanged();
    }
}


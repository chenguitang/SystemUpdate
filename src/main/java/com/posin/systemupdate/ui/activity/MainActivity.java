package com.posin.systemupdate.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.posin.systemupdate.R;
import com.posin.systemupdate.base.BaseActivity;
import com.posin.systemupdate.bean.UpdateDetail;
import com.posin.systemupdate.ui.contract.HomeContract;
import com.posin.systemupdate.ui.contract.UpdateContract;
import com.posin.systemupdate.ui.presenter.HomePresenter;
import com.posin.systemupdate.ui.presenter.UpdatePresenter;
import com.posin.systemupdate.utils.Proc;
import com.posin.systemupdate.utils.StringUtils;
import com.posin.systemupdate.utils.SystemUtils;
import com.posin.systemupdate.view.ConfirmDownloadDialog;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.id.message;
import static com.posin.systemupdate.R.id.tv_update_date;
import static com.posin.systemupdate.R.id.tv_update_version;


/**
 * FileName: MainActivity
 * Author: Greetty
 * Time: 2018/5/23 20:06
 * Desc: 在线更新系统主界面
 */
public class MainActivity extends BaseActivity implements UpdateContract.updateView,
        HomeContract.IHomeView, ConfirmDownloadDialog.ConfirmDialogListener {

    private static final String TAG = "MainActivity";
    private static final String[] PPK_EXT = {".ppk"};

    @BindView(R.id.rl_main_activity_root)
    RelativeLayout rlMainActivityRoot;
    @BindView(R.id.btn_check_update)
    Button btnCheckUpdate;
    @BindView(tv_update_version)
    TextView tvUpdateVersion;
    @BindView(tv_update_date)
    TextView tvUpdateDate;
    @BindView(R.id.tv_version_state)
    TextView tvVersionState;
    @BindView(R.id.view_root_background)
    View viewRootBackground;

    private HomePresenter mHomePresenter;
    private String mModel = "M102L";

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        mHomePresenter = new HomePresenter(this);
        mModel = SystemUtils.getPosinModel();

//        viewRootBackground.setAlpha(0.2f);

        initCurrentVersion();
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setLogo(R.mipmap.ic_toolbar_logo);
        mCommonToolbar.setTitle(R.string.app_name);

    }

    /**
     * 获取当前系统版本并显示
     */
    private void initCurrentVersion() {
        tvUpdateVersion.setText(StringUtils.append("当前版本：", SystemUtils.getSystemVersion()));
        tvUpdateDate.setText(StringUtils.append("版本日期：", SystemUtils.getSystemDateVersion()));
    }


    @OnClick({R.id.btn_check_update})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check_update:
//                showLoadingDialog("正在查找更新包");
//                mHomePresenter.searchUpdatePackage("M102L", "spk");
                mHomePresenter.searchUpdatePackage(mModel, "spk");

//                UpdatePresenter mUpdatePpkPresenter = new UpdatePresenter(this, this);
//                mUpdatePpkPresenter.updateSystem(new File("mnt/sdcard/test.ppk"));
//                UpdatePresenter mUpdatePresenter = new UpdatePresenter(this, this);
//                mUpdatePresenter.updateSpkSystem("test.spk");

                break;
            default:
                break;
        }
    }

    /**
     * 菜单按钮Item点击事件
     *
     * @param item 菜单Item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.offline_update:
                startActivityForResult(new Intent(this, SelectFileActivity.class), 100);
//                Toast.makeText(mContext, "进入离线更新模式", Toast.LENGTH_SHORT).show();
                break;
            case R.id.system_exit:
                finish();
                System.exit(0);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 加载菜单按钮
     *
     * @param menu Menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * 显示菜单Item中的图片
     *
     * @param featureId 图片ID
     * @param view      View
     * @param menu      Menu
     * @return boolean
     */
    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible",
                            Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPreparePanel(featureId, view, menu);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 100 && resultCode == 101) {
            String path = intent.getStringExtra("Path");
            if (!TextUtils.isEmpty(path)) {
                Log.e(TAG, "now update system ... ");
                UpdatePresenter mUpdatePpkPresenter = new UpdatePresenter(this, this);
                mUpdatePpkPresenter.updatePpkSystem(new File(path));
            } else {
                Log.e(TAG, "have cancel select path");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void showSearchProgress() {
        showLoadingDialog("正在查找更新包 ... ");
        tvVersionState.setText("正在查找更新包 ... ");
    }

    @Override
    public void dismissSearchProgress() {
        dismissLoadingDialog();
    }

    @Override
    public void unNeedUpdate(String type) {
        if (type.toLowerCase().equals("spk")) {
            mHomePresenter.searchUpdatePackage(mModel, "ppk");
        } else {
            Toast.makeText(this, "已是最新版本，无需更新系统...", Toast.LENGTH_SHORT).show();
            tvVersionState.setText("已是最新版本，无需更新系统");
        }
    }

    @Override
    public void needToUpdate(String type, UpdateDetail updateDetail) {

        String updateMessage = "更新内容：" + updateDetail.getInstruction() +
                "\n更新包版本为：" + updateDetail.getVersion() +
                "\n更新包上传时间：" + updateDetail.getUploaddate();

        tvVersionState.setText(updateMessage);

        String systemVersion = StringUtils.append(SystemUtils.getSystemVersion(),
                "-", SystemUtils.getSystemDateVersion());

        new ConfirmDownloadDialog(this, systemVersion, type.toLowerCase().equals("spk"),
                updateDetail, this).show();

    }

    @Override
    public void searchFailure(Throwable throwable) {
        Toast.makeText(this, "查找失败，请重新查找更新包: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        tvVersionState.setText("查找失败，请重新查找更新包");
        Log.e(TAG, "查找失败: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    @Override
    public void startDownloadPackage(String savePath) {
        Log.e(TAG, "开始下载更新包");
        showLoadingDialog("开始下载更新包");
        tvVersionState.setText("开始下载更新包");
    }

    @Override
    public void updateDownloadProgress(float progress) {
//        Log.e(TAG, "下载进度： " + progress);
        String progressStr = StringUtils.decimalFormat(progress, 2);
        showLoadingDialog("下载进度：" + progressStr + "%");
        tvVersionState.setText("下载进度：" + progressStr + "%");
    }

    @Override
    public void downloadFailure(Throwable throwable) {
        Log.e(TAG, "下载失败: " + throwable.getMessage());
//        tvVersionState.setText(StringUtils.append("下载失败: ", throwable.getMessage()));
        tvVersionState.setText("下载失败,请重新检查下载");
        throwable.printStackTrace();

    }

    @Override
    public void downloadSuccess(boolean isSpk, String savePath) {
        Log.e(TAG, "下载成功");
        tvVersionState.setText("下载成功，开始更新系统 ... ");
        UpdatePresenter mUpdatePresenter = new UpdatePresenter(this, this);
        if (isSpk) {
            Log.e(TAG, "downloadSuccess  更新SPK");
            Toast.makeText(this, "更新SPK... ", Toast.LENGTH_SHORT).show();
            int lastIndexOf = savePath.lastIndexOf("/");
            savePath = lastIndexOf > 0 ? savePath.substring(lastIndexOf + 1) : savePath;
            mUpdatePresenter.updateSpkSystem(savePath);
        } else {
            mUpdatePresenter.updatePpkSystem(new File(savePath));
        }
    }


    @Override
    public void updatePpkFailure(String filePath) {
        Log.e(TAG, "更新失败 。。。");
        tvVersionState.setText("更新失败，请重新下载更新");

    }

    @Override
    public void updatePpkSuccess(String filePath) {
        Log.e(TAG, "更新成功。。。");
        tvVersionState.setText("更新成功，请重启机器");
        try {
            Proc.createSuProcess("busybox rm -rf " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePpkOnClickOk(boolean updateSuccess) {
        if (updateSuccess) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("更新成功,马上重启！");
            builder.setPositiveButton("重启", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Proc.createSuProcess("reboot");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    @Override
    public void updateSpkStart(String filePath) {
        showLoadingDialog("正在执行准备升级环境，请稍后");
    }

    @Override
    public void updateSpkFailure(String filePath, String message) {
        dismissLoadingDialog();
        Toast.makeText(this, "出错了：" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateSpkSuccess(String filePath) {
        dismissLoadingDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("准备完成,马上重启更新系统！");
        builder.setPositiveButton("重启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Proc.createSuProcess("reboot");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void dismissDownloadProgress() {
        dismissLoadingDialog();
    }

    @Override
    public void confirm(boolean isSpk, String downloadUrl) {

        Log.d(TAG, "downloadUrl: " + downloadUrl);

        String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
        Log.e(TAG, "fileName: " + fileName);
        if (!TextUtils.isEmpty(fileName)) {
            mHomePresenter.downloadUpdatePackage(downloadUrl, isSpk,
                    StringUtils.append("mnt/sdcard/", fileName));
        } else {
            if (isSpk) {
                mHomePresenter.downloadUpdatePackage(downloadUrl, true, "mnt/sdcard/update.spk");
            } else {
                mHomePresenter.downloadUpdatePackage(downloadUrl, false, "mnt/sdcard/update.ppk");
            }
        }
    }

    @Override
    public void cancel() {

    }
}

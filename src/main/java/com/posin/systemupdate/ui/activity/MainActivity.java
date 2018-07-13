package com.posin.systemupdate.ui.activity;

import android.content.Intent;
import android.support.v7.view.menu.MenuBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.posin.systemupdate.R;
import com.posin.systemupdate.base.BaseActivity;
import com.posin.systemupdate.http.util.DownloadUtils;
import com.posin.systemupdate.http.util.HttpClient;
import com.posin.systemupdate.module.download.DownloadListener;
import com.posin.systemupdate.ui.contract.UpdatePpkContract;
import com.posin.systemupdate.ui.presenter.UpdatePpkPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * FileName: MainActivity
 * Author: Greetty
 * Time: 2018/5/23 20:06
 * Desc: 在线更新系统主界面
 */
public class MainActivity extends BaseActivity implements UpdatePpkContract.updatePpkView {

    private static final String TAG = "MainActivity";
    private static final String[] PPK_EXT = {".ppk"};

    private static final String savePath = "/mnt/sdcard/test.apk";

    private UpdatePpkPresenter mUpdatePpkPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {

//        mUpdatePpkPresenter = new UpdatePpkPresenter(this, this);
//        mUpdatePpkPresenter.updateSystem(new File("/mnt/media_rw/BC3D-FF39/ppk/" +
//                "安装广告系统-20180602-01.ppk"));

//        String url = "http://123.207.152.101/image-web/adv/201806/20180602114116852_474206.mp4";
//        String url = "http://andl.guopan.cn/103373-20539-1526513074.apk";
        String url = "http://shouji.360tpcdn.com/180514/ca679c25433a751cc4ef5c5379a1ea9a/com.popcap.pvz2cthd360_895.apk";
//        download(url);

        new DownloadUtils(new DownloadListener() {
            @Override
            public void onStartDownload() {
                Log.e(TAG, "**** onStartDownload ****");
            }

            @Override
            public void onProgress(int progress) {
                Log.e(TAG, "**** onProgress ****: " + progress);

            }

            @Override
            public void onFinishDownload() {
                Log.e(TAG, "**** onFinishDownload ****");

            }

            @Override
            public void onFail(Exception exception) {
                Log.e(TAG, "**** onFail ****: " + exception.getMessage());
                exception.printStackTrace();
            }
        }).download("/180514/ca679c25433a751cc4ef5c5379a1ea9a/com.popcap.pvz2cthd360_895.apk",
                "/mnt/sdcard/greetty.apk", null);

    }


    /**
     * 下载文件
     *
     * @param url 下载地址
     */
    public void download(String url) {
        Log.e(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

//        HttpClient.getInstance().download(url)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe(new Observer<ResponseBody>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        Log.e(TAG, " ==    onSubscribe   ==  ");
//                    }
//
//                    @Override
//                    public void onNext(@NonNull ResponseBody responseBody) {
//                        Log.e(TAG, " ==    onNext   ==  ");
//                        try {
//
//                            InputStream inputStream = responseBody.byteStream();
//                            OutputStream os = new FileOutputStream(savePath);
//                            long fileSize = responseBody.contentLength();
//                            Log.e(TAG, "responsebody size: " + fileSize);
//                            int len = 0;
//                            byte[] bytes = new byte[1024 * 2];
//                            long total = 0;
//                            while ((len = inputStream.read(bytes)) != -1) {
//                                total += len;
//                                Log.e(TAG, "length: " + len + "     " + (total * 100 / fileSize) + "%");
//                                os.write(bytes, 0, len);
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        Log.e(TAG, " ==    onError   ==  ");
//                        Log.e(TAG, "Error: " + e.getMessage());
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.e(TAG, " ==    onComplete   ==  ");
//
//                    }
//                });

        Log.e(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setLogo(R.mipmap.ic_toolbar_logo);
        mCommonToolbar.setTitle(R.string.app_name);

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
                Toast.makeText(mContext, "进入离线更新模式", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 100 && resultCode == 101) {
            String path = intent.getStringExtra("Path");
            if (!TextUtils.isEmpty(path)) {
                Log.e(TAG, "now update system ... ");
                mUpdatePpkPresenter = new UpdatePpkPresenter(this, this);
                mUpdatePpkPresenter.updateSystem(new File(path));
            } else {
                Log.e(TAG, "have cancel select path");
            }
        }
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
    public void updateFailure() {
        Log.e(TAG, "更新失败 。。。");
    }

    @Override
    public void updateSuccess() {
        Log.e(TAG, "更新成功。。。");
    }
}

package com.posin.systemupdate.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.posin.systemupdate.R;
import com.posin.systemupdate.base.BaseActivity;
import com.posin.systemupdate.bean.UpdateDetail;
import com.posin.systemupdate.ui.contract.HomeContract;
import com.posin.systemupdate.ui.contract.UpdatePpkContract;
import com.posin.systemupdate.ui.presenter.HomePresenter;
import com.posin.systemupdate.ui.presenter.UpdatePpkPresenter;
import com.posin.systemupdate.utils.StringUtils;
import com.posin.systemupdate.utils.SystemUtils;
import com.posin.systemupdate.view.ConfirmDownloadDialog;

import java.io.File;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * FileName: MainActivity
 * Author: Greetty
 * Time: 2018/5/23 20:06
 * Desc: 在线更新系统主界面
 */
public class MainActivity extends BaseActivity implements UpdatePpkContract.updatePpkView,
        HomeContract.IHomeView, ConfirmDownloadDialog.ConfirmDialogListener {

    private static final String TAG = "MainActivity";
    private static final String[] PPK_EXT = {".ppk"};

    @BindView(R.id.btn_check_update)
    Button btnCheckUpdate;
    @BindView(R.id.tv_update_version)
    TextView tvUpdateVersion;
    @BindView(R.id.tv_update_date)
    TextView tvUpdateDate;
    @BindView(R.id.tv_version_state)
    TextView tvVersionState;


    private HomePresenter mHomePresenter;
    private String mModel = "M102L";

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        mHomePresenter = new HomePresenter(this);
        mModel = "302.100.102";
    }


    @Override
    public void initToolBar() {
        mCommonToolbar.setLogo(R.mipmap.ic_toolbar_logo);
        mCommonToolbar.setTitle(R.string.app_name);

    }

    @OnClick({R.id.btn_check_update})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check_update:
//                showLoadingDialog("正在查找更新包");
//                mHomePresenter.searchUpdatePackage("M102L", "spk");
                mHomePresenter.searchUpdatePackage(mModel, "spk");

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
                UpdatePpkPresenter mUpdatePpkPresenter = new UpdatePpkPresenter(this, this);
                mUpdatePpkPresenter.updateSystem(new File(path));
            } else {
                Log.e(TAG, "have cancel select path");
            }
        }
    }

    @Override
    public void updateFailure() {
        Log.e(TAG, "更新失败 。。。");
    }

    @Override
    public void updateSuccess() {
        Log.e(TAG, "更新成功。。。");
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
            Toast.makeText(this, "已是最新版本，无法更新系统 ... ", Toast.LENGTH_SHORT).show();
            tvVersionState.setText("已是最新版本，无法更新系统 ... ");
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
        new ConfirmDownloadDialog(this, systemVersion, type.toLowerCase().equals("ppk"),
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
    public void downloadSuccess(String savePath) {
        Log.e(TAG, "下载成功");
        tvVersionState.setText("下载成功，开始更新系统 ... ");
        UpdatePpkPresenter mUpdatePpkPresenter = new UpdatePpkPresenter(this, this);
        mUpdatePpkPresenter.updateSystem(new File(savePath));
    }

    @Override
    public void dismissDownloadProgress() {
        dismissLoadingDialog();
    }

    @Override
    public void UpdatePpkSuccess() {

    }

    @Override
    public void UpdatePplFailure(Throwable throwable) {

    }

    @Override
    public void confirm(boolean isSpk, String downloadUrl) {

        Log.d(TAG, "downloadUrl: " + downloadUrl);

        if (isSpk) {
            mHomePresenter.downloadUpdatePackage(downloadUrl, "mnt/sdcard/test.spk");
        } else {
            mHomePresenter.downloadUpdatePackage(downloadUrl, "mnt/sdcard/test.ppk");

        }
    }

    @Override
    public void cancel() {

    }
}

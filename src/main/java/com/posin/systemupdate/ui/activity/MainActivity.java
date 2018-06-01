package com.posin.systemupdate.ui.activity;

import android.content.Context;
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
import com.posin.systemupdate.ui.contract.UpdatePpkContract;
import com.posin.systemupdate.ui.presenter.UpdatePpkPresenter;
import com.posin.systemupdate.utils.SystemUtils;
import com.posin.systemupdate.view.FileBrowserDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import static okhttp3.internal.Internal.instance;

/**
 * FileName: MainActivity
 * Author: Greetty
 * Time: 2018/5/23 20:06
 * Desc: 在线更新系统主界面
 */
public class MainActivity extends BaseActivity implements UpdatePpkContract.updatePpkView {

    private static final String TAG = "MainActivity";
    private static final String[] PPK_EXT = {".ppk"};

    private UpdatePpkPresenter mUpdatePpkPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setLogo(R.mipmap.ic_launcher2);
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

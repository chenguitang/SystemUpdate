package com.posin.systemupdate.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.posin.systemupdate.R;
import com.posin.systemupdate.view.LoadingDialog;

import butterknife.ButterKnife;

/**
 * FileName: BaseActivity
 * Author: Greetty
 * Time: 2018/5/23 20:03
 * Desc: Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected static Context mContext;
    public Toolbar mCommonToolbar;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSavedInstanceState(savedInstanceState);
        setContentView(getLayoutId());
        mContext = this;
        ButterKnife.bind(this);

        mCommonToolbar = ButterKnife.findById(this, R.id.common_toolbar);
        if (mCommonToolbar != null) {
            initToolBar();
            setSupportActionBar(mCommonToolbar);
        }

        initData();
    }

    /**
     * 显示加载进度框
     *
     * @param title String
     */
    public void showLoadingDialog(String title) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, "数据加载中");
        }
        mLoadingDialog.setTitle(title);
        mLoadingDialog.show();
    }

    /**
     * 隐藏加载进度框
     */
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    public abstract int getLayoutId();

    public abstract void initData();

    public abstract void initToolBar();

    public void initSavedInstanceState(Bundle savedInstanceState) {
    }

}

package com.posin.systemupdate.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.posin.systemupdate.ui.contract.SelectPpkContract;
import com.posin.systemupdate.view.FileBrowserDialog;

import java.io.File;
import java.io.IOException;

import static android.R.attr.name;
import static android.content.ContentValues.TAG;

/**
 * FileName: SelectPpkPresenter
 * Author: Greetty
 * Time: 2018/6/1 16:36
 * Desc: TODO
 */
public class SelectPpkPresenter implements SelectPpkContract.selectUpdatePackagePresenter {

    private static final String TAG = "SelectPpkPresenter";

    private Context mContext;
    private String[] mSuffixName;
    private SelectPpkContract.selectUpdatePackageView mSelectUpdatePackageView;

    public SelectPpkPresenter(Context mContext, String[] mSuffixName,
                              SelectPpkContract.selectUpdatePackageView mSelectUpdatePackageView) {
        this.mContext = mContext;
        this.mSuffixName = mSuffixName;
        this.mSelectUpdatePackageView = mSelectUpdatePackageView;
    }

    @Override
    public void selectUpdatePackage() {

        try {
            new FileBrowserDialog(mContext, "选择更新包", "/mnt", mSuffixName, true, true) {
                @Override
                public boolean onOk(File path) {
                    mSelectUpdatePackageView.complete(path);
                    return true;
                }

                @Override
                public void onCancel() {
                    mSelectUpdatePackageView.cancel();
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

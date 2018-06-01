package com.posin.systemupdate.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.posin.systemupdate.R;
import com.posin.systemupdate.ui.contract.SelectPpkContract;
import com.posin.systemupdate.ui.contract.UpdatePpkContract;
import com.posin.systemupdate.ui.presenter.SelectPpkPresenter;
import com.posin.systemupdate.ui.presenter.UpdatePpkPresenter;
import com.posin.systemupdate.view.FileBrowserDialog;

import java.io.File;
import java.io.IOException;

import static android.R.attr.path;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class SelectFileActivity extends AppCompatActivity
        implements SelectPpkContract.selectUpdatePackageView {

    private static final String TAG = "SelectFileActivity";
    private SelectPpkPresenter mSelectPpkPresenter;
    private String mPath = null;

    private String[] mSuffixName = {".ppk"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSelectPpkPresenter = new SelectPpkPresenter(SelectFileActivity.this, mSuffixName, this);
        mSelectPpkPresenter.selectUpdatePackage();
    }

    @Override
    public void complete(File path) {
        Log.e(TAG, "path: " + path.getPath());
        mPath = path.getPath();
        Intent intent = new Intent();
        intent.putExtra("Path", mPath);
        setResult(101, intent);
        SelectFileActivity.this.finish();
    }

    @Override
    public void cancel() {
        SelectFileActivity.this.finish();
    }

}

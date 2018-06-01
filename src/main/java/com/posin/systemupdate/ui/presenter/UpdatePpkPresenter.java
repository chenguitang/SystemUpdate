package com.posin.systemupdate.ui.presenter;

import android.content.Context;

import com.posin.systemupdate.ui.contract.UpdatePpkContract;
import com.posin.systemupdate.view.InstallerDialog;

import java.io.File;

/**
 * FileName: UpdatePpkPresenter
 * Author: Greetty
 * Time: 2018/6/1 16:36
 * Desc: TODO
 */
public class UpdatePpkPresenter implements UpdatePpkContract.updatePpkPresenter {

    private Context mContext;
    private UpdatePpkContract.updatePpkView mUpdatePpkView;

    public UpdatePpkPresenter(Context mContext, UpdatePpkContract.updatePpkView mUpdatePpkView) {
        this.mContext = mContext;
        this.mUpdatePpkView = mUpdatePpkView;
    }

    @Override
    public void updateSystem(File path) {

        new InstallerDialog(mContext, path) {
            @Override
            protected void onInstallError() {
                mUpdatePpkView.updateFailure();
            }

            @Override
            protected void onInstallSuccess() {
                mUpdatePpkView.updateSuccess();
            }
        };
    }


}

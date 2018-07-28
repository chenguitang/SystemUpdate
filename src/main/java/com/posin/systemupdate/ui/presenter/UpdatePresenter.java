package com.posin.systemupdate.ui.presenter;

import android.content.Context;

import com.posin.systemupdate.ui.contract.UpdateContract;
import com.posin.systemupdate.utils.Proc;
import com.posin.systemupdate.view.InstallerDialog;

import java.io.File;
import java.io.IOException;

/**
 * FileName: UpdatePresenter
 * Author: Greetty
 * Time: 2018/6/1 16:36
 * Desc: TODO
 */
public class UpdatePresenter implements UpdateContract.updatePresenter {

    private Context mContext;
    private UpdateContract.updateView mUpdateView;

    public UpdatePresenter(Context mContext, UpdateContract.updateView mUpdateView) {
        this.mContext = mContext;
        this.mUpdateView = mUpdateView;
    }

    @Override
    public void updatePpkSystem(final File path) {

        new InstallerDialog(mContext, path) {
            @Override
            protected void onInstallError() {
                mUpdateView.updatePpkFailure(path.getPath());
            }

            @Override
            protected void onInstallSuccess() {
                mUpdateView.updatePpkSuccess(path.getPath());
            }

            @Override
            protected void onClickOk(boolean updateSuccess) {
                mUpdateView.updatePpkOnClickOk(updateSuccess);
            }
        };
    }

    @Override
    public void updateSpkSystem(String filePath) {

        mUpdateView.updateSpkStart(filePath);

        String WRITE_CMD_TITLE = "echo \"/sbin/recovery update sdcard " + filePath + "\" >" +
                " \"/mnt/sdcard/autoexec\" \n /sbin/recovery setautoexec /mnt/sdcard/autoexec";
        try {
            int resultCode = Proc.suExecCallback(WRITE_CMD_TITLE, null, 2000);

            if (resultCode != 0) {
                mUpdateView.updateSpkFailure(filePath, "命令执行失败，请重新下载更新.");
            } else {
                mUpdateView.updateSpkSuccess(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mUpdateView.updateSpkFailure(filePath, "Error:" + e.getMessage());
        }


    }
}

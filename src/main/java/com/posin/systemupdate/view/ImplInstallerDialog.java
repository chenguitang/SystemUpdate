package com.posin.systemupdate.view;

import android.content.Context;

import java.io.File;

/**
 * FileName: ImplInstallerDialog
 * Author: Greetty
 * Time: 2018/6/1 20:03
 * Desc: TODO
 */
public class ImplInstallerDialog extends InstallerDialog {

    public ImplInstallerDialog(Context context, File pkgFile) {
        super(context, pkgFile);
    }

    @Override
    protected void onInstallError() {

    }

    @Override
    protected void onInstallSuccess() {

    }
}

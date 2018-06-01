package com.posin.systemupdate.ui.contract;

import com.posin.systemupdate.base.BaseContract;

import java.io.File;

/**
 * FileName: UpdatePpkContract
 * Author: Greetty
 * Time: 2018/6/1 15:40
 * Desc: 安装更新包交互协议
 */
public interface UpdatePpkContract {

    public interface updatePpkView extends BaseContract.BaseView {

        void updateFailure();

        void updateSuccess();
    }

    public interface updatePpkPresenter extends BaseContract.BasePresenter {

        void updateSystem(File path);
    }


}

package com.posin.systemupdate.ui.contract;

import com.posin.systemupdate.base.BaseContract;

import java.io.File;

/**
 * FileName: SelectPpkContract
 * Author: Greetty
 * Time: 2018/5/29 19:59
 * Desc: 选择更新包交互协议
 */
public class SelectPpkContract {

    public interface selectUpdatePackageView extends BaseContract.BaseView {

        void complete(File path);
        void cancel();
    }


    public interface selectUpdatePackagePresenter extends BaseContract.BasePresenter{

        /**
         * 选择更新包
         */
        void selectUpdatePackage();
    }

}

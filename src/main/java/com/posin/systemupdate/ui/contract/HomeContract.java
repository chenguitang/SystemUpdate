package com.posin.systemupdate.ui.contract;

import com.posin.systemupdate.base.BaseContract;

/**
 * FileName: HomeContract
 * Author: Greetty
 * Time: 2018/5/29 18:45
 * Desc: 主页面交互协议
 */
public interface HomeContract {


    interface IHomeView extends BaseContract.BaseView {

        /**
         * 显示正在查找更新包进度提示
         */
        void showProgress();

        /**
         * 隐藏正在查找更新包进度提示
         */
        void dismissProgress();

        /**
         * 查找成功,不需要更新系统
         */
        void unNeedUpdate();

        /**
         * 查找成功,有新更新包，需更新系统
         */
        void needToUpdate();

        /**
         * 查找失败
         */
        void searchFailure(Throwable throwable);


        /**
         * 下载进度
         *
         * @param progress 进度值0~100
         */
        void downloadUpdatePackage(float progress);

        /**
         * 下载失败
         */
        void downloadFailure(Throwable throwable);

        /**
         * 下载成功
         */
        void downloadSuccess();

        /**
         * 更新PPK成功
         */
        void UpdatePpkSuccess();

        /**
         * 更新PPK失败
         */
        void UpdatePplFailure(Throwable throwable);

    }

    interface IHomePresenter extends BaseContract.BasePresenter{

        /**
         * @param devices 适用设备型号
         * @param type    更新包属性（SPK=1，PPK=2）
         * @param version 更新包版本号
         */
        void searchUpdatePackage(String devices, String type, String version);

        /**
         * 下载更新包
         *
         * @param url 下载地址
         */
        void downloadUpdatePackage(String url);

        /**
         * 更新PPK
         *
         * @param path PPK文件路径
         */
        void updateSystemForPpk(String path);

    }


}

package com.posin.systemupdate.ui.contract;

import com.posin.systemupdate.base.BaseContract;

import java.io.File;

import static android.R.id.message;

/**
 * FileName: UpdateContract
 * Author: Greetty
 * Time: 2018/6/1 15:40
 * Desc: 安装更新包交互协议
 */
public interface UpdateContract {

    interface updateView extends BaseContract.BaseView {

        /**
         * PPK更新失败
         */
        void updatePpkFailure(String filePath);

        /**
         * PPK更新成功
         */
        void updatePpkSuccess(String filePath);

        /**
         * PPK更新结束，点击确定退出按钮
         *
         * @param updateSuccess boolean
         */
        void updatePpkOnClickOk(boolean updateSuccess);

        /**
         * 开始更新SPK
         */
        void updateSpkStart(String filePath);

        /**
         * SPK更新失败
         */
        void updateSpkFailure(String filePath,String message);

        /**
         * SPK更新成功
         */
        void updateSpkSuccess(String filePath);

    }

    interface updatePresenter extends BaseContract.BasePresenter {
        /**
         * 更新ppk
         *
         * @param path 文件路径
         */
        void updatePpkSystem(File path);

        /**
         * 更新spk
         *
         * @param filePath 文件路径
         */
        void updateSpkSystem(String filePath);

    }


}

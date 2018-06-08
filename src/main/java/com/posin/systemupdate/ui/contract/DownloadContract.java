package com.posin.systemupdate.ui.contract;

import com.posin.systemupdate.base.BaseContract;
import com.posin.systemupdate.module.download.DownloadListener;

/**
 * FileName: DownloadContract
 * Author: Greetty
 * Time: 2018/6/8 16:00
 * Desc: TODO
 */
public interface DownloadContract {


    interface DownloadView extends DownloadListener, BaseContract.BaseView {

    }

    interface DownloadPresenter extends BaseContract.BasePresenter {
        void download();
    }


}

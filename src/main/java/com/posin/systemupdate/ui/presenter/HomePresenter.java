package com.posin.systemupdate.ui.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.posin.systemupdate.bean.UpdateDetail;
import com.posin.systemupdate.http.util.HttpClient;
import com.posin.systemupdate.ui.contract.HomeContract;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


/**
 * FileName: HomePresenter
 * Author: Greetty
 * Time: 2018/5/30 20:10
 * Desc: TODO
 */
public class HomePresenter implements HomeContract.IHomePresenter {

    private static final String TAG = "HomePresenter";
    private HomeContract.IHomeView mHomeView;

    public HomePresenter(HomeContract.IHomeView homeView) {
        this.mHomeView = homeView;
    }

    @Override
    public void searchUpdatePackage(String devices, final String type) {
        HttpClient.getInstance().findUpdatePackage(devices, type).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpdateDetail>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mHomeView.showSearchProgress();
                    }

                    @Override
                    public void onNext(@NonNull UpdateDetail updateDetail) {
                        String updateUrl = updateDetail.getUrl();
                        if (TextUtils.isEmpty(updateUrl)) {
                            mHomeView.unNeedUpdate(type);
                        } else {
                            mHomeView.needToUpdate(type, updateDetail);
                            Log.e(TAG, "update url: " + updateDetail.getUrl());
                            Log.e(TAG, "update version: " + updateDetail.getVersion());
                            Log.e(TAG, "update desc: " + updateDetail.getInstruction());
                            Log.e(TAG, "update date: " + updateDetail.getUploaddate());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mHomeView.dismissSearchProgress();
                        mHomeView.searchFailure(e);
                    }

                    @Override
                    public void onComplete() {
                        mHomeView.dismissSearchProgress();
                    }
                });
    }

    @Override
    public void downloadUpdatePackage(String url, String savePath) {
        HttpClient.getInstance().download(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mHomeView.downloadUpdatePackage(0);
                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mHomeView.dismissDowloadProgress();
                        mHomeView.downloadFailure(e);
                    }

                    @Override
                    public void onComplete() {
                        mHomeView.dismissDowloadProgress();
                    }
                });
    }

    @Override
    public void updateSystemForPpk(String path) {

    }
}

package com.posin.systemupdate.ui.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.posin.systemupdate.bean.UpdateDetail;
import com.posin.systemupdate.http.util.HttpClient;
import com.posin.systemupdate.ui.contract.HomeContract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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
    public void downloadUpdatePackage(String url, final String savePath) {
        HttpClient.getInstance().download(url, mHomeView)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                       mHomeView.startDownloadPackage(savePath);
                    }
                })
                .map(new Function<ResponseBody, InputStream>() {
                    @Override
                    public InputStream apply(@NonNull ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }
                }).observeOn(Schedulers.computation())
                .doOnNext(new Consumer<InputStream>() {
                    @Override
                    public void accept(InputStream inputStream) throws Exception {
                        writeFile(inputStream, savePath);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mHomeView.downloadFailure(throwable);
                        mHomeView.dismissDownloadProgress();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mHomeView.dismissDownloadProgress();
                        mHomeView.downloadSuccess(savePath);
                    }
                })
                .subscribe( );
    }

    @Override
    public void updateSystemForPpk(String path) {

    }

    /**
     * 将输入流写入文件
     *
     * @param inputString 文件输入流
     * @param filePath    文件保存位置
     */
    private void writeFile(InputStream inputString, String filePath) throws Exception {

        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos = new FileOutputStream(file);

        byte[] b = new byte[1024 * 5];

        int len;
        Log.e(TAG, "开始保存数据数据");
        while ((len = inputString.read(b)) != -1) {
            fos.write(b, 0, len);
        }
        Log.e(TAG, "数据写入完成");
        inputString.close();
        fos.close();

    }
}

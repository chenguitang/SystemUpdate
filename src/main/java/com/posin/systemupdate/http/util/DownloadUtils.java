package com.posin.systemupdate.http.util;

import com.posin.systemupdate.base.Constant;
import com.posin.systemupdate.http.api.UpdateApi;
import com.posin.systemupdate.module.download.DownloadInterceptor;
import com.posin.systemupdate.module.download.DownloadListener;

import org.reactivestreams.Subscriber;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


/**
 * FileName: DownloadUtils
 * Author: Greetty
 * Time: 2018/6/8 15:50
 * Desc: 下载工具类
 */
public class DownloadUtils {

    private DownloadListener mDownloadListener;
    private static final int DEFAULT_TIMEOUT = 15;
    private Retrofit retrofit;


    public DownloadUtils(DownloadListener downloadListener) {
        this.mDownloadListener = downloadListener;

        DownloadInterceptor mInterceptor = new DownloadInterceptor(mDownloadListener);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://123.207.152.101:88")
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 开始下载
     *
     * @param url
     * @param filePath
     * @param observer
     */
    public void download(@NonNull String url, final String filePath, Observer observer) {

        mDownloadListener.onStartDownload();

        // subscribeOn()改变调用它之前代码的线程
        // observeOn()改变调用它之后代码的线程
        //
        retrofit.create(UpdateApi.class)
                .download(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, InputStream>() {

                    @Override
                    public InputStream apply(@NonNull ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }

                })
                .observeOn(Schedulers.computation()) // 用于计算任务
                .doOnNext(new Consumer<InputStream>() {
                    @Override
                    public void accept(InputStream inputStream) throws Exception {
                        writeFile(inputStream,filePath);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    /**
     * 将输入流写入文件
     *
     * @param inputString
     * @param filePath
     */
    private void writeFile(InputStream inputString, String filePath) {

        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos = null;
        try {
//            file.createNewFile();

            fos = new FileOutputStream(file);

            byte[] b = new byte[1024*5];

            int len;
            while ((len = inputString.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            inputString.close();
            fos.close();

        } catch (Exception e) {
            mDownloadListener.onFail(e);
        }

    }
}


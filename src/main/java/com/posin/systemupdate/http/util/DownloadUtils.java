package com.posin.systemupdate.http.util;

import android.util.Log;

import com.posin.systemupdate.base.Constant;
import com.posin.systemupdate.http.api.UpdateApi;
import com.posin.systemupdate.module.download.DownloadInterceptor;
import com.posin.systemupdate.module.download.DownloadListener;
import com.posin.systemupdate.ui.contract.HomeContract;

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

import static android.content.ContentValues.TAG;
import static com.google.common.collect.EnumMultiset.create;
import static com.posin.systemupdate.http.util.RetrofitUtil.getRetrofit;


/**
 * FileName: DownloadUtils
 * Author: Greetty
 * Time: 2018/6/8 15:50
 * Desc: 下载工具类
 */
public class DownloadUtils {

    private static UpdateApi mUpdateApi;
    private static final int DEFAULT_CONNECT_TIMEOUT = 15;
    private static final int DEFAULT_READ_TIMEOUT = 20;
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;


    static Retrofit getDownloadRetrofit(HomeContract.IHomeView homeView) {

        DownloadInterceptor mInterceptor = new DownloadInterceptor(homeView);

        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(mInterceptor)
                    .retryOnConnectionFailure(true)
                    .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS) //读写文件超时
                    .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)//连接超时
                    .build();
        }

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.API_BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        Log.e(TAG, "DownloadUtils method .....");
        return retrofit;
    }

    static UpdateApi getUpdateApi(HomeContract.IHomeView homeView) {
        if (mUpdateApi == null) {
            mUpdateApi = getDownloadRetrofit(homeView).create(UpdateApi.class);
        }
        return mUpdateApi;
    }


}


package com.posin.systemupdate.http.util;

import com.posin.systemupdate.base.Constant;
import com.posin.systemupdate.http.api.UpdateApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * FileName: RetrofitUtil
 * Author: Greetty
 * Time: 2018/5/29 15:16
 * Desc: TODO
 */
public class RetrofitUtil {

    private static Retrofit mRetrofit;
    private static UpdateApi mUpdateApi;
    private static OkHttpClient mOkHttpClient;


    protected static Retrofit getRetrofit() {
        if (mOkHttpClient == null) {
            mOkHttpClient= new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS) //连接超时
                    .readTimeout(20,TimeUnit.SECONDS) //读写文件超时
                    .retryOnConnectionFailure(true) // 失败重发
                    .build();
        }
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Constant.API_BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 添加Rx适配器
                    .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                    .client(mOkHttpClient)
                    .build();
        }
        return mRetrofit;
    }

    protected static UpdateApi getUpdateApi() {
        if (mUpdateApi == null) {
            mUpdateApi = getRetrofit().create(UpdateApi.class);
        }
        return mUpdateApi;
    }

}

package com.posin.systemupdate.module.download;

import android.util.Log;

import com.posin.systemupdate.ui.contract.HomeContract;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * FileName: DownloadInterceptor
 * Author: Greetty
 * Time: 2018/6/8 15:43
 * Desc: 拦截器，获取请求的ResponseBod
 */
public class DownloadInterceptor implements Interceptor {


    private HomeContract.IHomeView mHomeView;

    public DownloadInterceptor(HomeContract.IHomeView homeView) {
        this.mHomeView = homeView;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(
                new ProgressResponseBody(response.body(), mHomeView)).build();
    }
}

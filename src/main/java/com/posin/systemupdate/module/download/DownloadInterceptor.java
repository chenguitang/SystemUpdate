package com.posin.systemupdate.module.download;

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

    private DownloadListener mDownloadListener;

    public DownloadInterceptor(DownloadListener downloadListener) {
        this.mDownloadListener = downloadListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(
                new ProgressResponseBody(response.body(), mDownloadListener)).build();
    }
}

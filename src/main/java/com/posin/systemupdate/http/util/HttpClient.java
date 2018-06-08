package com.posin.systemupdate.http.util;

import com.posin.systemupdate.bean.UpdateDetail;
import com.posin.systemupdate.module.download.DownloadListener;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * FileName: HttpClient
 * Author: Greetty
 * Time: 2018/6/2 15:40
 * Desc: TODO
 */
public class HttpClient extends RetrofitUtil {

    private static class HttppClientHolder {
        private static final HttpClient INSTANCE = new HttpClient();
    }

    private HttpClient() {

    }

    public static HttpClient getInstance() {
        return HttppClientHolder.INSTANCE;
    }

    /**
     * 查找更新包
     *
     * @param devices 设备型号
     * @param type    更新包类型
     * @param version 系统版本
     * @return TODO
     */
    public Observable<UpdateDetail> findUpdatePackage(String devices, String type, String version) {
        return getUpdateApi().findUpdatePackage(devices, type, version);
    }

    /**
     * 下载文件
     *
     * @param url 下载地址
     * @return TODO
     */
    public Observable<ResponseBody> download(String url) {
        return getUpdateApi().download(url);
    }

}

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

    private static class HttpClientHolder {
        private static final HttpClient INSTANCE = new HttpClient();
    }

    private HttpClient() {

    }

    public static HttpClient getInstance() {
        return HttpClientHolder.INSTANCE;
    }

    /**
     * 查找更新包
     *
     * @param devices 设备型号
     * @param type    更新包类型
     * @return TODO
     */
    public Observable<UpdateDetail> findUpdatePackage(String devices, String type) {
        return getUpdateApi().findUpdatePackage(devices, type);
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

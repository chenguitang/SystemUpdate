package com.posin.systemupdate.module.download;

/**
 * FileName: DownloadListener
 * Author: Greetty
 * Time: 2018/6/8 15:22
 * Desc: 下载监听接口
 */
public interface DownloadListener {

    /**
     * 开始下载
     */
    void onStartDownload();

    /**
     * 下载进度
     * @param progress 进度值 0~100
     */
    void onProgress(int progress);

    /**
     * 下载完成
     */
    void onFinishDownload();

    /**
     * 下载失败
     * @param exception 异常信息
     */
    void onFail(Exception exception);

}

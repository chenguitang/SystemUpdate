package com.posin.systemupdate.bean;

/**
 * FileName: UpdateDetail
 * Author: Greetty
 * Time: 2018/5/29 10:33
 * Desc: TODO
 */
public class UpdateDetail {


    /**
     * url : http://123.207.152.101/image-web/appstore/app/201807/20180712103337022_773277.spk
     * instruction : 修复已知bug，优化用户体验，更新开机画面
     * uploaddate : 2018-07-12 10:33:40.0
     * version : V1.2.14
     */

    private String url;
    private String instruction;
    private String uploaddate;
    private String version;

    public UpdateDetail(String url, String instruction, String uploaddate, String version) {
        this.url = url;
        this.instruction = instruction;
        this.uploaddate = uploaddate;
        this.version = version;
    }

    public UpdateDetail() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getUploaddate() {
        return uploaddate;
    }

    public void setUploaddate(String uploaddate) {
        this.uploaddate = uploaddate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "UpdateDetail{" +
                "url='" + url + '\'' +
                ", instruction='" + instruction + '\'' +
                ", uploaddate='" + uploaddate + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}

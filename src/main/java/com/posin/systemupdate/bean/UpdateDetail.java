package com.posin.systemupdate.bean;

/**
 * FileName: UpdateDetail
 * Author: Greetty
 * Time: 2018/5/29 10:33
 * Desc: TODO
 */
public class UpdateDetail {


    /**
     * id : 123
     * instruction : 11
     * updatetype : 1
     * type : 1
     * devices : abc.def
     * date : 2017‐08‐31
     * url : http://139.199.36.31:2801/monit/uploadfile/20170831155856100904117.pdf
     * version : 1.1
     */

    //更新包ID
    private String id;
    //更新包说明
    private String instruction;
    //更新类型 立即更新、空闲更新
    private String updatetype;
    //更新包属性 PPK、SPK
    private String type;
    //适用设备型号
    private String devices;
    //日期
    private String date;
    //更新包下载地址
    private String url;
    //更新包版本号
    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getUpdatetype() {
        return updatetype;
    }

    public void setUpdatetype(String updatetype) {
        this.updatetype = updatetype;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDevices() {
        return devices;
    }

    public void setDevices(String devices) {
        this.devices = devices;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
                "id='" + id + '\'' +
                ", instruction='" + instruction + '\'' +
                ", updatetype='" + updatetype + '\'' +
                ", type='" + type + '\'' +
                ", devices='" + devices + '\'' +
                ", date='" + date + '\'' +
                ", url='" + url + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}

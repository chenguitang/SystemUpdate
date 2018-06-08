package com.posin.systemupdate.http.api;

import com.posin.systemupdate.bean.UpdateDetail;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * FileName: UpdateApi
 * Author: Greetty
 * Time: 2018/5/29 10:10
 * Desc: API接口
 */
public interface UpdateApi {

    /**
     * 查询更新包
     *
     * @param devices 设备型号
     * @param type    更新包属性（SPK=1，PPK=2）
     * @param version 系统版本
     * @return Observable<UpdateDetail>
     */
    @POST("/monit/advertisementControl/queryversions.do")
    Observable<UpdateDetail> findUpdatePackage(@Query("devices") String devices,
                                               @Query("type") String type,
                                               @Query("version") String version);

    /**
     * 下载文件
     *
     * @param url 下载地址
     * @return Observable<ResponseBody>
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

}

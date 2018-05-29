package com.posin.systemupdate.http.api;

import com.posin.systemupdate.bean.UpdateDetail;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
     * @param devices 适用设备型号
     * @param type    更新包属性（SPK=1，PPK=2）
     * @param version 更新包版本号
     * @return Observable<UpdateDetail>
     */
    @POST("/monit/advertisementControl/queryversions.do")
    Observable<UpdateDetail> findUpdatePackage(@Query("devices") String devices,
                                               @Query("type") String type,
                                               @Query("version") String version);


}

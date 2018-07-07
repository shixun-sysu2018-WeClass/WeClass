package com.example.asus.weclass;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ASUS on 2018/6/11.
 */

public interface ServerService {
    @GET("/API/Login")
    Observable<List<LoginSerClass>> getUser(@Query("UserName") String user,@Query("Password") String pass, @Query("IsStudent") int type);

    @GET("/API/{class}")
    Observable<List<ClassListSerClass>> getClass(@Path("class") String classes);

    @GET("/API/getAppointmentListByUID")
    Observable<List<AppListSerClass>> getAppList(@Query("UID") String app_list);

    @GET("/API/{Waiting}")
    Observable<List<RequListSerClass>> getReqList(@Path("Waiting") String waiting);


    @FormUrlEncoded
    @POST("/API/modifyPassword")
    Observable<ResponseClass> postPass(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST("/API/cancelAppointment")
    Observable<ResponseClass1> cancelapp(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST("/API/applyRoom")
    Observable<ResponseClass2> newapp(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST("/API/modifyStatus")
    Observable<ReqDetSerClass> postStatus(@FieldMap Map<String, Object> map);

    @GET("/API/searchRoomInfo")
    Observable<List<jsonModel>> getCourses(@Query("CID") String cid);


    @FormUrlEncoded
    @POST("/API/modifyCourse")
    Observable<ResponseClass3> modifyCourse(@FieldMap Map<String, Object> map);

}

package com.example.mytools_collections.apihelper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BaseApiService {
    // Fungsi ini untuk memanggil API http://192.168.43.247/android/_login.php
    @FormUrlEncoded
    @POST("_login.php")
    public Call<ResponseBody> loginRequest(@Field("userid") String userid,
                                           @Field("password") String password,
                                           @Field("flagging") String flagging,
                                           @Field("apps_name") String apps_name);
    @FormUrlEncoded
    @POST("_collections.php")
    public Call<ResponseBody> getCollections(@Field("userid") String userid,@Field("service_number") String service_number);

    @FormUrlEncoded
    @POST("_loginInfo.php")
    public Call<ResponseBody> loadLoginfo(@Field("date_x") String date_x,@Field("date_y") String date_y,@Field("userid") String userid);

    @FormUrlEncoded
    @POST("_dataCollectionsApi.php")
    public Call<ResponseBody> loadDataCollections(@Field("date_x") String date_x,@Field("date_y") String date_y,@Field("userid") String userid);

    @FormUrlEncoded
    @POST("_api_dataCollections.php")
    public Call<ResponseBody> updateDataCollections(@Field("id") String id);

    @FormUrlEncoded
    @POST("_deleteDataCollections.php")
    public Call<ResponseBody> deleteDataCollections(@Field("id") String id);

    @FormUrlEncoded
    @POST("_changePassword.php")
    public Call<ResponseBody> changePassword(@Field("userid") String userid,@Field("old_password") String old_password,@Field("new_password") String new_password);
}

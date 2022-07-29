package com.example.mytools_collections.apihelper;

import com.example.mytools_collections.Collections_Constants;

public class UtilsApi {
    // 10.0.2.2 ini adalah localhost.
//    public static final String BASE_URL_API = "http://192.168.43.247/login/";

    // Mendeklarasikan Interface BaseApiService
    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(Collections_Constants.BASE_URL_API).create(BaseApiService.class);
    }
}

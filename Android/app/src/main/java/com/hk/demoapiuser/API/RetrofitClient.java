package com.hk.demoapiuser.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String baseUrl = "https://hkobirweb.000webhostapp.com/DemoApi/public/";
    private Retrofit retrofit;
    private static RetrofitClient apiClient;

    public RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static RetrofitClient getInstance() {
        if (apiClient == null) {
            apiClient = new RetrofitClient();
        }
        return apiClient;
    }

    public RetrofitInterface getApi() {
        return retrofit.create(RetrofitInterface.class);
    }

}

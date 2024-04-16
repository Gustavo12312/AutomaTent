package com.example.automatent;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("api/users/auth")
    Call<LoginResult> loginUser(@Body HashMap<String, String> map);
    @POST("api/users")
    Call<RegisterResult> registerUser(@Body HashMap<String, String> map);
    @GET("api/dev/")
    Call<List<DevicesResult>> getDevices();
    @GET("api/dev/data/{id}")
    Call<DevandDataResult> getDevandData(@Path("id") Integer deviceId);

}

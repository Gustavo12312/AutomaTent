package com.example.automatent;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/users/auth")
    Call<LoginResult> loginUser(@Body HashMap<String, String> map);
}

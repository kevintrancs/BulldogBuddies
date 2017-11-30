package com.example.ktran.wannabetinder.models;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ktran on 11/26/17.
 */

public interface RetroInterfaces {

    @POST("register")
    Call<ServerResponse> registerUser(@Body User user);

    @POST("authenticate")
    Call<ServerResponse> authUser(@Body User user);

    @GET("profile/{id}")
    Call<ServerResponse> getProfile(@Header("x-access-token") String token,
                                    @Path("id") String name);

    @GET("profile/{id}/{f_id}")
    Call<ServerResponse> addFriend(@Header("x-access-token") String token,
                                    @Path("id") String myName,
                                    @Path("f_id") String friendName);

    @GET("users")
    Call<ServerResponse> getUsers(@Header("x-access-token") String token);

    @POST("requestFriend")
    Call<ServerResponse> requestAFriend(@Header("x-access-token") String token,
                                        @Body String friendName);

    @GET("friends")
    Call<ServerResponse> getFriends(@Header("x-access-token") String token);



}

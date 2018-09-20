package com.demo.photoalbum.interfaces;



import com.google.gson.JsonElement;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface MyApis {
    @POST
    @FormUrlEncoded
    public Call<JsonElement> login(@Url String url, @Field("username") String username, @Field("password") String password);

    @POST
    public Call<JsonElement> getPhotos(@Url String url);

}

package com.dentalclinic.capstone.api.services;

import com.dentalclinic.capstone.api.requestobject.RegisterRequest;
import com.dentalclinic.capstone.models.User;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by lucky on 15-Oct-17.
 */

public interface UserService {
    //    @GET("/api/users/{id}")
//    Call<User> getByID(@Path("id") int id);
//    @POST("/api/users/login")
//    Call<User> login(@Body User user);
//    @POST("/api/users/register")
//    Call<User> register(@Body User user);
//    @PUT("/api/users/{id}")
//    Call<User> update(@Path("id") int id, @Body User user);

//    @FormUrlEncoded
//     @POST("/api/users/updatepwd")
//    Call<User> updatePassword(@Field("id") int id, @Field("password") String password);

    @FormUrlEncoded
    @POST("/api/user/login")
    Single<Response<User>> login(@Field("phone") String phone, @Field("password") String password);

}

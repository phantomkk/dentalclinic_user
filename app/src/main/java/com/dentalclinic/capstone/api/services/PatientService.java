package com.dentalclinic.capstone.api.services;

import com.dentalclinic.capstone.api.requestobject.RegisterRequest;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PatientService {
    @FormUrlEncoded
    @POST("/api/user/login")
    Single<Response<User>> login(@Field("phone") String phone, @Field("password") String password);
}

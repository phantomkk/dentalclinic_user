package com.dentalclinic.capstone.api.services;

import com.dentalclinic.capstone.api.requestobject.RegisterRequest;
import com.dentalclinic.capstone.api.requestobject.UpdatePatientRequest;
import com.dentalclinic.capstone.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.User;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PatientService {
    @POST("api/patient/update")
    Single<Response<SuccessResponse>> changePatientInfo(@Body UpdatePatientRequest request);
    @Multipart
    @POST("api/patient/changeAvatar")
    Single<Response<SuccessResponse>> changeAvatar(
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part id);
}

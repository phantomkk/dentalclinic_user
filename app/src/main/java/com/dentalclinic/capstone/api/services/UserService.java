package com.dentalclinic.capstone.api.services;

import android.support.v4.media.MediaBrowserCompat;

import com.dentalclinic.capstone.api.requestobject.AppointmentRequest;
import com.dentalclinic.capstone.api.requestobject.LoginRequest;
import com.dentalclinic.capstone.api.requestobject.RegisterRequest;
import com.dentalclinic.capstone.api.requestobject.UpdatePatientRequest;
import com.dentalclinic.capstone.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.models.Appointment;
import com.dentalclinic.capstone.models.User;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by lucky on 15-Oct-17.
 */

public interface UserService {

    @POST("/api/user/login")
    Single<Response<User>> login(@Body LoginRequest request);

    @POST("/api/user/register")
    Single<Response<User>> register(@Body RegisterRequest requestObj);

    @FormUrlEncoded
    @POST("api/user/updateNotifToken")
    Single<Response<String>> updateNotifyFirebaseToken(@Field("notif_token") String token, @Field("phone") String phone);

    @GET("api/user/logout")
    Single<Response<SuccessResponse>> logout();

    @GET("api/user/resetPassword/{phone}")
    Single<Response<SuccessResponse>> resetPassword(@Path("phone") String phone);

    @FormUrlEncoded
    @POST("api/user/changePassword")
    Single<Response<SuccessResponse>> changePassword(@Field("phone") String phone,
                                                     @Field("current_password") String currentPassword,
                                                     @Field("password") String password);

    @POST("api/user/bookAppointment")
    Single<Response<List<Appointment>>> bookAppointment(@Body AppointmentRequest appointmentRequest);

}

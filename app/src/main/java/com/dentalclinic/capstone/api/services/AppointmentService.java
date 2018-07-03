package com.dentalclinic.capstone.api.services;

import com.dentalclinic.capstone.api.requestobject.AppointmentRequest;
import com.dentalclinic.capstone.models.Appointment;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AppointmentService {
    @POST("api/appointment/book")
    Single<Response<List<Appointment>>> bookAppointment(@Body AppointmentRequest appointmentRequest);

    @GET("api/appointment/all")
    Single<Response<List<Appointment>>> getAll();

    @GET("api/appointment/getByPhone/{phone}")
    Single<Response<List<Appointment>>> getByPhone(@Path("phone")String phone);

    @GET("api/appointment/{id}")
    Single<Response<List<Appointment>>> getById(@Path("id") int id);
}

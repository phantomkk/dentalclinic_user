package com.dentalclinic.capstone.api.services;

import com.dentalclinic.capstone.api.requestobject.AppointmentRequest;
import com.dentalclinic.capstone.models.Appointment;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AppointmentService {
    @POST("api/appointment/book")
    Single<Response<Appointment>> bookAppointment(@Body AppointmentRequest appointmentRequest);
}

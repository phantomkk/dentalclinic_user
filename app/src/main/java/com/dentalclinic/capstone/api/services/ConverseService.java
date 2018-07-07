package com.dentalclinic.capstone.api.services;

import com.dentalclinic.capstone.api.responseobject.QuotesResponse;
import com.dentalclinic.capstone.models.Appointment;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ConverseService {
    @GET("/api/live")
    Call<QuotesResponse> getConvers(@Query("access_key") String accessKey);
}

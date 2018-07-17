package com.dentalclinic.capstone.api.services;

import com.dentalclinic.capstone.models.TreatmentHistory;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HistoryTreatmentService {
    @GET("api/treatmentHistory/all")
    Single<Response<List<TreatmentHistory>>> getAllHistoryTreatment();

    @GET("api/treatmentHistory/{phone}")
    Single<Response<List<TreatmentHistory>>> getHistoryTreatmentByPhone(@Path("phone") String phone);

    @GET("api/treatmentHistory/getByPatientId/{id}")
    Single<Response<List<TreatmentHistory>>> getHistoryTreatmentById(@Path("id") int id);
}

package com.dentalclinic.capstone.api.services;

import com.dentalclinic.capstone.models.TreatmentHistory;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HistoryTreatmentService {
    @POST("api/historyTreatment/all")
    Single<Response<List<TreatmentHistory>>> getAllHistoryTreatment();

    @POST("api/historyTreatment/{phone}")
    Single<Response<List<TreatmentHistory>>> getHistoryTreatmentByPhone(@Path("phone") String phone);
}

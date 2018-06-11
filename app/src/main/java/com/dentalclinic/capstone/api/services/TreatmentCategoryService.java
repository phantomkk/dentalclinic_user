package com.dentalclinic.capstone.api.services;

import com.dentalclinic.capstone.api.requestobject.RegisterRequest;
import com.dentalclinic.capstone.models.TreatmentCategory;
import com.dentalclinic.capstone.models.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TreatmentCategoryService {

    @GET("api/treatmentcategory/all")
    Single<Response<List<TreatmentCategory>>> getAll();
}

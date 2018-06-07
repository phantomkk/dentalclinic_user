package com.dentalclinic.capstone.api.services;

import com.dentalclinic.capstone.models.City;
import com.dentalclinic.capstone.models.District;

import java.util.List;
import java.util.Observable;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AddressService {
    @GET("api/district/")
    Single<Response<List<District>>> getAllDistricts();

    @GET("api/city/all")
    Single<Response<List<City>>> getAllCities();
    @GET("api/city/{id}/districts")
    Single<Response<List<District>>> getDistrictByCityID(@Path("id") int id);
}

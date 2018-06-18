package com.dentalclinic.capstone.api.services;

import com.dentalclinic.capstone.models.Payment;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface PaymentService {
    @POST("api/payment/getByPhone")
    Single<Response<List<Payment>>> getByPhone(@Field("phone") String phone);
}

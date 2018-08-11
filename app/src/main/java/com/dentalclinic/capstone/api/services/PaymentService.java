package com.dentalclinic.capstone.api.services;

import com.dentalclinic.capstone.models.Payment;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PaymentService {
    @GET("api/payment/getByPhone/{phone}")
    Single<Response<List<Payment>>> getByPhone(@Path("phone") String phone);

    @FormUrlEncoded
    @POST("api/payment/verifyPayment")
    Single<Response<List<Payment>>> verifyPayment(
            @Field("local_payment_id") int localPaymentId,
            @Field("payment_id") String paymentId,
            @Field("payment_client_json") String paymentClientJson
    );
}

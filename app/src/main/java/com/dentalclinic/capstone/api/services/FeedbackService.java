package com.dentalclinic.capstone.api.services;

import com.dentalclinic.capstone.models.Feedback;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FeedbackService {
    @POST("api/feedback/create")
    Single<Response<String>> createFeedback(@Body Feedback request);
}

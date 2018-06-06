package com.dentalclinic.capstone.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lucky on 13-Sep-17.
 */

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static String baseUrl = "http://10.0.2.2:8000";
    public static Retrofit getClient(){
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();//.addInterceptor(interceptor).build();
        //add header in case of authorization
//        clientBuilder.addInterceptor((chain) -> {
//                    Request original = chain.request();
//                    Request.Builder reqBuilder = original.newBuilder()
//                            .addHeader("Authorization", "token");
//                    Request request = reqBuilder.build();
//                    return chain.proceed(request);
//                }
//        );
        //add log for retrofit
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(interceptor);
        OkHttpClient client = clientBuilder.build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}

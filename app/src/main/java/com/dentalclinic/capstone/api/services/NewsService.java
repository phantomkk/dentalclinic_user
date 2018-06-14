package com.dentalclinic.capstone.api.services;

import com.dentalclinic.capstone.models.News;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NewsService {
    @GET("api/news/all")
    Single<List<News>> getAllNews();

    @GET("api/news/{id}")
    Single<Response<News>> getNewById(@Path("id") int newId);

//    @GET("api/news/loadmore")
//    Single<Response<List<News>>> loadMore(@Query("currentIndex") int currentIndex,
//                                          @Query("numItem") int numItem
//    );

    @GET("api/news/loadmore")
    Single<Response<List<News>>> loadMoreByType(@Query("currentIndex") int currentIndex,
                                                @Query("numItem") int numItem,
                                                @Query("typeId") int typeID
    );


}

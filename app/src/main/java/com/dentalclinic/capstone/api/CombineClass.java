package com.dentalclinic.capstone.api;

import com.dentalclinic.capstone.models.News;

import java.util.List;

import retrofit2.Response;

public class CombineClass {
    private Response<List<News>> listNews;
    private Response<List<News>> listPromotion;

    public CombineClass(Response<List<News>> listNews, Response<List<News>> listPromotion) {
        this.listNews = listNews;
        this.listPromotion = listPromotion;
    }

    public Response<List<News>> getListNews() {
        return listNews;
    }

    public void setListNews(Response<List<News>> listNews) {
        this.listNews = listNews;
    }

    public Response<List<News>> getListPromotion() {
        return listPromotion;
    }

    public void setListPromotion(Response<List<News>> listPromotion) {
        this.listPromotion = listPromotion;
    }
}

package com.dentalclinic.capstone.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class News implements Serializable{
    @SerializedName("id")
    private int id;
    @SerializedName("news_image")
    private String newsImage;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("created_date")
    private Date createDate;
    @SerializedName("staff")
    private Staff author;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Staff getAuthor() {
        return author;
    }

    public void setAuthor(Staff author) {
        this.author = author;
    }

}

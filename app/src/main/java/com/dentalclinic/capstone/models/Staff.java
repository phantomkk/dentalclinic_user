package com.dentalclinic.capstone.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Staff implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("specialty")
    private String specialty;
    @SerializedName("phone")
    private String phone;
    @SerializedName("date_of_birth")
    private Date dateOfBirth;
    @SerializedName("gender")
    private String gender;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("user_id")
    private int userId;



}

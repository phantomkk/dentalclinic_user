package com.dentalclinic.capstone.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Patient implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private int phone;
    @SerializedName("date_of_birth")
    private Date dateOfBirth;
    @SerializedName("gender")
    private String gender;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("district_id")
    private String districtId;
    @SerializedName("district")
    private District district;






}

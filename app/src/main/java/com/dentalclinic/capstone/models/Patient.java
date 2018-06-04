package com.dentalclinic.capstone.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Patient implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private String phone;
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
    @SerializedName("treatmentHistories")
    private List<TreatmentHistory> treatmentHistories;

    public Patient(int id, String name, String address, String phone, Date dateOfBirth, String gender, String avatar, String districtId, District district) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.avatar = avatar;
        this.districtId = districtId;
        this.district = district;
    }

    public Patient(String name, String address, String phone, Date dateOfBirth, String avatar) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.avatar = avatar;
    }

    public Patient(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Patient() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public List<TreatmentHistory> getTreatmentHistories() {
        return treatmentHistories;
    }

    public void setTreatmentHistories(List<TreatmentHistory> treatmentHistories) {
        this.treatmentHistories = treatmentHistories;
    }
}

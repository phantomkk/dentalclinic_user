package com.dentalclinic.capstone.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Payment implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("treatment_history")
    private TreatmentHistory treatmentHistory;
    @SerializedName("patient")
    private Patient patient;
    @SerializedName("treatment")
    private Treatment treatment;
    @SerializedName("day_pay")
    private Date dayPay;
    @SerializedName("prepaid")
    private Double prepaid;
    @SerializedName("note_payable")
    private Double notePayable;
    @SerializedName("receptionist")
    private Staff receptionist;
}

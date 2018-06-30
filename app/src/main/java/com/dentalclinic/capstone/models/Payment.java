package com.dentalclinic.capstone.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Payment implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("prepaid")
    private Long prepaid;
    @SerializedName("note_payable")
    private Long notePayable;
    @SerializedName("total_price")
    private Long totalPrice;
    @SerializedName("user")
    private User user;
    @SerializedName("is_done")
    private int isDone;
    @SerializedName("treatment_histories")
    private List<TreatmentHistory> treatmentHistories;
    @SerializedName("payment_details")
    private List<PaymentDetail> paymentDetails;
    @SerializedName("treatment_names")
    private List<String> treatmentNames;

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    public List<String> getTreatmentNames() {
        return treatmentNames;
    }

    public void setTreatmentNames(List<String> treatmentNames) {
        this.treatmentNames = treatmentNames;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getPrepaid() {
        return prepaid;
    }

    public void setPrepaid(Long prepaid) {
        this.prepaid = prepaid;
    }

    public Long getNotePayable() {
        return notePayable;
    }

    public void setNotePayable(Long notePayable) {
        this.notePayable = notePayable;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int isDone() {
        return isDone;
    }

    public void setDone(int done) {
        isDone = done;
    }

    public List<TreatmentHistory> getTreatmentHistories() {
        return treatmentHistories;
    }

    public void setTreatmentHistories(List<TreatmentHistory> treatmentHistories) {
        this.treatmentHistories = treatmentHistories;
    }

    public List<PaymentDetail> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(List<PaymentDetail> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}

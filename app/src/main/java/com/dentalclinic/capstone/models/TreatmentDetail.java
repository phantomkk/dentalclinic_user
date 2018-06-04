package com.dentalclinic.capstone.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TreatmentDetail implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("treatment_history")
    private TreatmentHistory treatmentHistory;
    @SerializedName("note")
    private String note;
    @SerializedName("dentist")
    private Staff dentist;
    @SerializedName("created_date")
    private Date createdDate;
    @SerializedName("payment")
    private Payment payment;
    @SerializedName("treatment_step")
    private TreatmentStep treatmentStep;
    @SerializedName("treatment_images")
    private List<TreatmentImage> treatmentImages;
    @SerializedName("treatment")
    private Treatment treatment;
    public int getId() {
        return id;
    }

    public TreatmentDetail(String note) {
        this.note = note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TreatmentHistory getTreatmentHistory() {
        return treatmentHistory;
    }

    public void setTreatmentHistory(TreatmentHistory treatmentHistory) {
        this.treatmentHistory = treatmentHistory;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Staff getDentist() {
        return dentist;
    }

    public void setDentist(Staff dentist) {
        this.dentist = dentist;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public TreatmentStep getTreatmentStep() {
        return treatmentStep;
    }

    public void setTreatmentStep(TreatmentStep treatmentStep) {
        this.treatmentStep = treatmentStep;
    }

    public List<TreatmentImage> getTreatmentImages() {
        return treatmentImages;
    }

    public void setTreatmentImages(List<TreatmentImage> treatmentImages) {
        this.treatmentImages = treatmentImages;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }
}

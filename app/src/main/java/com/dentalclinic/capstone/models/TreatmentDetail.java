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
    @SerializedName("treatment_detail_steps")
    private List<TreatmentDetailStep> treatmentDetailSteps;
    @SerializedName("treatment_images")
    private List<TreatmentImage> treatmentImages;
    @SerializedName("treatment")
    private Treatment treatment;
    @SerializedName("prescription")
    private List<Prescription> prescriptions;

    public TreatmentDetail() {
    }

    public TreatmentDetail(TreatmentHistory treatmentHistory, String note, Staff dentist, Date createdDate, List<TreatmentDetailStep> treatmentDetailSteps, List<TreatmentImage> treatmentImages, List<Prescription> prescriptions) {
        this.treatmentHistory = treatmentHistory;
        this.note = note;
        this.dentist = dentist;
        this.createdDate = createdDate;
        this.treatmentDetailSteps = treatmentDetailSteps;
        this.treatmentImages = treatmentImages;
        this.prescriptions = prescriptions;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

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

    public List<TreatmentDetailStep> getTreatmentDetailSteps() {
        return treatmentDetailSteps;
    }

    public void setTreatmentDetailSteps(List<TreatmentDetailStep> treatmentDetailSteps) {
        this.treatmentDetailSteps = treatmentDetailSteps;
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

package com.dentalclinic.capstone.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class TreatmentHistory implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("patient_id")
    private String patientId;
    @SerializedName("created_date")
    private Date createDate;
    @SerializedName("finish_date")
    private Date finishDate;
    @SerializedName("description")
    private String description;
    @SerializedName("treatment_id")
    private int treatmentId;
    @SerializedName("treatment")
    private Treatment treatment;

    public TreatmentHistory(int id, String patientId, Date createDate, Date finishDate, String description, int treatmentId, Treatment treatment) {
        this.id = id;
        this.patientId = patientId;
        this.createDate = createDate;
        this.finishDate = finishDate;
        this.description = description;
        this.treatmentId = treatmentId;
        this.treatment = treatment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(int treatmentId) {
        this.treatmentId = treatmentId;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }
}

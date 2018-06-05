package com.dentalclinic.capstone.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TreatmentDetailStep implements Serializable {
    @SerializedName("treatment_detail")
    private TreatmentDetail treatmentDetail;
    @SerializedName("treatment_step")
    private TreatmentStep treatmentStep;
    @SerializedName("description")
    private String description;

    public TreatmentDetail getTreatmentDetail() {
        return treatmentDetail;
    }

    public void setTreatmentDetail(TreatmentDetail treatmentDetail) {
        this.treatmentDetail = treatmentDetail;
    }

    public TreatmentStep getTreatmentStep() {
        return treatmentStep;
    }

    public void setTreatmentStep(TreatmentStep treatmentStep) {
        this.treatmentStep = treatmentStep;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package com.dentalclinic.capstone.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TreatmentStep implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("treatment_id")
    private int treatmentId;
    @SerializedName("description")
    private String description;
}

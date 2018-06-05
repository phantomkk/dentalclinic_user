package com.dentalclinic.capstone.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Treatment implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("price")
    private Double price;
    @SerializedName("category")
    private TreatmentCategory category;
    @SerializedName("treatment_steps")
    private List<TreatmentStep> treatmentSteps;

    public Treatment(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Treatment() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public TreatmentCategory getCategory() {
        return category;
    }

    public void setCategory(TreatmentCategory category) {
        this.category = category;
    }

    public List<TreatmentStep> getTreatmentSteps() {
        return treatmentSteps;
    }

    public void setTreatmentSteps(List<TreatmentStep> treatmentSteps) {
        this.treatmentSteps = treatmentSteps;
    }
}

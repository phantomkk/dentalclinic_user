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
    @SerializedName("category_id")
    private int categoryId;
    @SerializedName("category")
    private TreatmentCategory category;
    @SerializedName("treatment_steps")
    private List<TreatmentStep> treatmentSteps;

    public Treatment(int id, String name, String description, Double price, int categoryId, TreatmentCategory category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.category = category;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public TreatmentCategory getCategory() {
        return category;
    }

    public void setCategory(TreatmentCategory category) {
        this.category = category;
    }
}

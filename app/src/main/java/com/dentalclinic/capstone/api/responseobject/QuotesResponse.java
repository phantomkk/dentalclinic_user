package com.dentalclinic.capstone.api.responseobject;

import com.dentalclinic.capstone.models.Quotes;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuotesResponse implements Serializable {
    @SerializedName("quotes")
    private Quotes quotes;

    public Quotes getQuotes() {
        return quotes;
    }

    public void setQuotes(Quotes quotes) {
        this.quotes = quotes;
    }
}

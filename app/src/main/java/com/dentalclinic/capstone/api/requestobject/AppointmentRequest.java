package com.dentalclinic.capstone.api.requestobject;

import java.io.Serializable;
import java.util.Date;

public class AppointmentRequest implements Serializable {
    private String phone;
    private String date;
    private String note;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

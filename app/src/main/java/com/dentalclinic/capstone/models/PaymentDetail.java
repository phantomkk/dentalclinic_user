package com.dentalclinic.capstone.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class PaymentDetail implements Serializable{
    @SerializedName("id")
    private int id;
    @SerializedName("payment")
    private Payment payment;
    @SerializedName("staff")
    private Staff receptionist;
    @SerializedName("date_create")
    private Date dateCreate;
    @SerializedName("received_money")
    private Long receivedMoney;

    public PaymentDetail(Staff receptionist, Long receivedMoney) {
        this.receptionist = receptionist;
        this.receivedMoney = receivedMoney;
    }

    public PaymentDetail(Staff receptionist, Date dateCreate, Long receivedMoney) {
        this.receptionist = receptionist;
        this.dateCreate = dateCreate;
        this.receivedMoney = receivedMoney;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Staff getReceptionist() {
        return receptionist;
    }

    public void setReceptionist(Staff receptionist) {
        this.receptionist = receptionist;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Long getReceivedMoney() {
        return receivedMoney;
    }

    public void setReceivedMoney(Long receivedMoney) {
        this.receivedMoney = receivedMoney;
    }
}

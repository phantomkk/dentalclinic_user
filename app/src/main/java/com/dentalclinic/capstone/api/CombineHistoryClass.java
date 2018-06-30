package com.dentalclinic.capstone.api;

import com.dentalclinic.capstone.models.Appointment;
import com.dentalclinic.capstone.models.News;
import com.dentalclinic.capstone.models.Payment;
import com.dentalclinic.capstone.models.TreatmentHistory;

import java.util.List;

import retrofit2.Response;

public class CombineHistoryClass {
    private Response<List<TreatmentHistory>> treatmentHistories;
    private Response<List<Payment>> payments;
    private Response<List<Appointment>> appointment;

    public CombineHistoryClass(Response<List<TreatmentHistory>> treatmentHistories, Response<List<Payment>> payments, Response<List<Appointment>> appointment) {
        this.treatmentHistories = treatmentHistories;
        this.payments = payments;
        this.appointment = appointment;
    }

    public Response<List<TreatmentHistory>> getTreatmentHistories() {
        return treatmentHistories;
    }

    public void setTreatmentHistories(Response<List<TreatmentHistory>> treatmentHistories) {
        this.treatmentHistories = treatmentHistories;
    }

    public Response<List<Payment>> getPayments() {
        return payments;
    }

    public void setPayments(Response<List<Payment>> payments) {
        this.payments = payments;
    }

    public Response<List<Appointment>> getAppointment() {
        return appointment;
    }

    public void setAppointment(Response<List<Appointment>> appointment) {
        this.appointment = appointment;
    }
}

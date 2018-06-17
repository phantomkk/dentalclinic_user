package com.dentalclinic.capstone.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.adapter.AppointmentAdapter;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.services.AppointmentService;
import com.dentalclinic.capstone.models.Appointment;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.utils.CoreManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryAppointmentFragment extends BaseFragment {


    public HistoryAppointmentFragment() {
        // Required empty public constructor
    }

    ListView lvHistoryAppoint;
    AppointmentAdapter adapter;
    List<Appointment> appointments = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_appointment, container, false);
        lvHistoryAppoint = view.findViewById(R.id.lv_history_appointment);
//        prepareData();
        adapter = new AppointmentAdapter(getContext(), appointments);
        lvHistoryAppoint.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareData();
    }

    public void prepareData() {
        Patient patient = CoreManager.getCurrentPatient();
        showLoading();
        AppointmentService appointmentService = APIServiceManager.getService(AppointmentService.class);
        appointmentService.getByPhone(patient.getPhone())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<Appointment>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<List<Appointment>> listResponse) {
                        if (listResponse.body() != null) {
                            appointments.addAll(listResponse.body());
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "SUCCESS but else", Toast.LENGTH_SHORT).show();
                        }
                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "On Error", Toast.LENGTH_SHORT).show();

                        hideLoading();
                    }
                });
//        Appointment appointment = new Appointment();
//    appointment.setStartTime();
//        appointments.add(appointment);
//        appointments.add(appointment);
//        appointments.add(appointment);
//        appointments.add(appointment);
//        adapter.notifyDataSetChanged();
    }

}

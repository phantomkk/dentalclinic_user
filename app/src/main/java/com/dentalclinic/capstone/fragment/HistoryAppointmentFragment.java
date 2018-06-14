package com.dentalclinic.capstone.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.adapter.AppointmentAdapter;
import com.dentalclinic.capstone.models.Appointment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryAppointmentFragment extends Fragment {


    public HistoryAppointmentFragment() {
        // Required empty public constructor
    }
    ListView lvHistoryAppoint;
    AppointmentAdapter adapter;
    List<Appointment> appointments;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_appointment, container, false);
        lvHistoryAppoint = view.findViewById(R.id.lv_history_appointment);
        prepareData();
        adapter = new AppointmentAdapter(getContext(),appointments);
        lvHistoryAppoint.setAdapter(adapter);

        return view;
    }

    public void prepareData(){
        appointments = new ArrayList<>();
        Appointment appointment = new Appointment();
        appointments.add(appointment);
        appointments.add(appointment);
        appointments.add(appointment);
        appointments.add(appointment);
//        adapter.notifyDataSetChanged();
    }

}

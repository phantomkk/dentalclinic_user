package com.dentalclinic.capstone.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.adapter.PatientProfileAdapter;
import com.dentalclinic.capstone.models.Patient;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccoutFragment extends Fragment {

    ListView lvPatient;
    Button btnChangePassword, btnChangePhone;

    public MyAccoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_my_accout, container, false);
        lvPatient = v.findViewById(R.id.lv_patient);
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient());
        patients.add(new Patient());
        PatientProfileAdapter adapter = new PatientProfileAdapter(getContext(),patients);
        lvPatient.setAdapter(adapter);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}

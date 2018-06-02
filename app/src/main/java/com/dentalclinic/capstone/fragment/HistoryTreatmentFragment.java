package com.dentalclinic.capstone.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.adapter.PatientAdapter;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryTreatmentFragment extends BaseFragment {
    ListView listView;
    User user = new User();
    PatientAdapter adapter;
    Patient patient = new Patient("trinh vo", "go vap");
    Patient patient1 = new Patient("trinh vo", "go vap");

    private static HistoryTreatmentFragment instance= new HistoryTreatmentFragment();

    public static HistoryTreatmentFragment newInstance() {
        instance = new HistoryTreatmentFragment();
        return instance;
    }

    public static HistoryTreatmentFragment getInstance(){
        return instance;
    }
    public HistoryTreatmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_history_treatment, container, false);
        View v = inflater.inflate(R.layout.fragment_history_treatment, container, false);
        List<Patient> patientList = new ArrayList<Patient>();
        patientList.add(patient);
        patientList.add(patient1);
        user.setPatients(patientList);
        listView = (ListView) v.findViewById(R.id.list_profile);
        adapter = new PatientAdapter(getContext(),R.layout.item_patient,user.getPatients());
        listView.setAdapter(adapter);
        listView.setDivider(null);
        return v;
    }

}

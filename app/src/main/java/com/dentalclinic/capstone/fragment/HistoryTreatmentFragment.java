package com.dentalclinic.capstone.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.activities.TreatmentDetailActivity;
import com.dentalclinic.capstone.adapter.PatientAdapter;
import com.dentalclinic.capstone.adapter.TreatmentHistoryAdapter;
import com.dentalclinic.capstone.models.Event;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.Tooth;
import com.dentalclinic.capstone.models.Treatment;
import com.dentalclinic.capstone.models.TreatmentHistory;
import com.dentalclinic.capstone.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryTreatmentFragment extends BaseFragment {
    ListView listView;
    User user = new User();
    TreatmentHistoryAdapter adapter;
    List<TreatmentHistory> treatmentHistories;
//    Patient patient1 = new Patient("trinh vo", "go vap");

    private static HistoryTreatmentFragment instance = new HistoryTreatmentFragment();

    public static HistoryTreatmentFragment newInstance() {
        instance = new HistoryTreatmentFragment();
        return instance;
    }

    public static HistoryTreatmentFragment getInstance() {
        return instance;
    }

    public HistoryTreatmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history_treatment, container, false);
//        user.setPatients(patientList);
        listView = (ListView) v.findViewById(R.id.list_profile);
        prepareListData();
        adapter = new TreatmentHistoryAdapter(getContext(), treatmentHistories);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), TreatmentDetailActivity.class);
                startActivity(intent);
            }
        });


        return v;
    }

//    List<Patient> patientList;

    private void prepareListData() {
        treatmentHistories = new ArrayList<>();
        TreatmentHistory treatmentHistory =new TreatmentHistory();
        Treatment treatment = new Treatment("Trám Răng", new Event(10));
        treatmentHistory.setTreatment(treatment);
        treatmentHistory.setTooth(new Tooth("răng cửa"));
        treatmentHistory.setCreateDate("2018-06-16 09:51:01");
        treatmentHistory.setFinishDate("2018-06-16 09:51:01");
        treatmentHistory.setPrice(Long.valueOf("150000"));
        treatmentHistory.setTotalPrice(Long.valueOf("150000"));
        treatmentHistories.add(treatmentHistory);
        treatmentHistories.add(treatmentHistory);
        treatmentHistories.add(treatmentHistory);
        treatmentHistories.add(treatmentHistory);
    }
}

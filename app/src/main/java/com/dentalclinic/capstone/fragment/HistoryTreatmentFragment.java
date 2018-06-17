package com.dentalclinic.capstone.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.activities.TreatmentDetailActivity;
import com.dentalclinic.capstone.adapter.PatientAdapter;
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
    ExpandableListView listView;
    User user = new User();
    PatientAdapter adapter;
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
        user.setPatients(patientList);
        listView = (ExpandableListView) v.findViewById(R.id.list_profile);
        prepareListData();
        adapter = new PatientAdapter(getContext(), patientList);

        listView.setAdapter(adapter);
//        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                if (listView.isGroupExpanded(groupPosition))
//                    listView.collapseGroupWithAnimation(groupPosition);
//                else
//                    listView.expandGroupWithAnimation(groupPosition);
//                return true;
//            }
//        });
//        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
//                Intent intent = new Intent(getContext(), TreatmentDetailActivity.class);
////                intent.putExtra("PersonID", personID);
//                startActivity(intent);
//                return false;
//            }
//        });
//        listView.expandGroup(0);
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                if (listView.isGroupExpanded(groupPosition))
                    listView.collapseGroup(groupPosition);
                else
                    listView.expandGroup(groupPosition);
                return true;
            }
        });
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Intent intent = new Intent(getContext(), TreatmentDetailActivity.class);
                startActivity(intent);
                return false;
            }
        });
        return v;
    }

    HashMap<Patient, List<TreatmentHistory>> listDataChild;
    List<Patient> patientList;

    private void prepareListData() {

        listDataChild = new HashMap<Patient, List<TreatmentHistory>>();
        patientList = new ArrayList<Patient>();
        Patient patient = new Patient();
        patient.setName("vo quoc trinh");
        patient.setAddress("go vap");
        patient.setPhone("01685149049");
        String dtStart = "30-06-1996";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            patient.setDateOfBirth(format.parse(dtStart));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        patient.setAvatar("https://scontent.fsgn5-5.fna.fbcdn.net/v/t1.0-9/27545086_1520633358052034_8513856773051240273_n.jpg?_nc_cat=0&_nc_eui2=AeHYSp5Hio8m6PKlSmzZfQXfME6SBJSaL_gi0QNCyxngl3PUW9JlHbLJFMv8Jn3x8nnkAP1xgtEsRjsrun9WOY_px9vNetVWiuSOy_Uvv5NGJw&oh=6c8421d07717bb804b0059471ded6cd0&oe=5BB3A0B1");

        Patient patient2 = new Patient();
        patient2.setName("nhieu sy luc");
        patient2.setAddress("quan 12");
        patient2.setPhone("01685149049");
        String dtStart2 = "01-01-1996";
        try {
            patient2.setDateOfBirth(format.parse(dtStart2));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        patient2.setAvatar("https://scontent.fsgn5-5.fna.fbcdn.net/v/t1.0-9/21106763_661579387371492_6919408620920338286_n.jpg?_nc_cat=0&_nc_eui2=AeFS24o42DjWOdszwhxK3fB8ztBLi1_14lzqdUPhz2P8iesHrofix5GpCo4bNdwV1f7W3cfkRM4k0TqlcNvwqWzUPhy4yKMlJ_gOD0adW4C5-g&oh=d3b2103d7ec405372c1f7518676e00f7&oe=5BB82FD4");

        // Adding child data
        List<TreatmentHistory> treatmentHistories = new ArrayList<TreatmentHistory>();
        TreatmentHistory treatmentHistory = new TreatmentHistory();
        treatmentHistory.setTreatment(new Treatment("Trám Răng", new Event(10)));
        treatmentHistory.setTooth(new Tooth("răng cửa"));
        treatmentHistory.setTotalPrice(Long.valueOf("100000"));
        treatmentHistory.setPrice(Long.valueOf("100000"));
        String dtStart3 = "01-01-1996";
        try {
            treatmentHistory.setCreateDate(format.parse(dtStart3));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            treatmentHistory.setFinishDate(format.parse(dtStart3));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        treatmentHistories.add(treatmentHistory);
        treatmentHistories.add(treatmentHistory);
        patient.setTreatmentHistories(treatmentHistories);
        patient2.setTreatmentHistories(treatmentHistories);

        patientList.add(patient);
        patientList.add(patient2);
    }
}

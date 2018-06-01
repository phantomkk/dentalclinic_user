package com.dentalclinic.capstone.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dentalclinic.capstone.dentalclinicuser.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryTreatmentFragment extends Fragment {


    public HistoryTreatmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_treatment, container, false);
    }

}

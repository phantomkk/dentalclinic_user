package com.dentalclinic.capstone.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.adapter.ServiceAdapter;
import com.dentalclinic.capstone.animation.AnimatedExpandableListView;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.Treatment;
import com.dentalclinic.capstone.models.TreatmentCategory;
import com.dentalclinic.capstone.models.TreatmentHistory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DentalFragment extends BaseFragment {


    public DentalFragment() {
        // Required empty public constructor
    }

    List<TreatmentCategory> treatmentCategories;
    HashMap<TreatmentCategory, List<Treatment>> listDataChild;

    AnimatedExpandableListView expandableListView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dental, container, false);
        prepareData();
        expandableListView = v.findViewById(R.id.eplv_list_categories);
        ServiceAdapter adapter = new ServiceAdapter(getContext(), treatmentCategories,listDataChild);
        expandableListView.setAdapter(adapter);

        return v;
    }


    public void prepareData(){
        treatmentCategories = new ArrayList<>();
        listDataChild = new HashMap<>();
        TreatmentCategory treatmentCategory = new TreatmentCategory("NHA CHU","https://nhakhoakim.com/wp-content/themes/Themesnhakhoaandong/assets/img/igray/11.png");
        Treatment treatment = new Treatment("TRÁM RĂNG COMPOSITE","tại nhà",Long.parseLong("40000"),Long.parseLong("40000"));
        Treatment treatment2 = new Treatment("TẨY TRẮNG",Long.parseLong("40000"),Long.parseLong("40000"));
        Treatment treatment3 = new Treatment("NHỔ RĂNG CỐI LỚN HOẶC RĂNG KHÔN HÀM TRÊN\n",Long.parseLong("40000"),Long.parseLong("40000"));
        treatmentCategory.getTreatments().add(treatment);
        treatmentCategory.getTreatments().add(treatment2);
        treatmentCategory.getTreatments().add(treatment3);
        TreatmentCategory treatmentCategory2 = new TreatmentCategory("PHỤC HÌNH CỐ ĐỊNH","https://nhakhoakim.com/wp-content/themes/Themesnhakhoaandong/assets/img/igray/12.png");
        Treatment treatment4 = new Treatment("TRÁM RĂNG COMPOSITE","tại nhà",Long.parseLong("40000"),Long.parseLong("40000"));
        Treatment treatment5 = new Treatment("TẨY TRẮNG\n",Long.parseLong("40000"),Long.parseLong("40000"));
        Treatment treatment6 = new Treatment("NHỔ RĂNG CỐI LỚN HOẶC RĂNG KHÔN HÀM TRÊN",Long.parseLong("40000"),Long.parseLong("40000"));
        treatmentCategory.getTreatments().add(treatment4);
        treatmentCategory.getTreatments().add(treatment5);
        treatmentCategory.getTreatments().add(treatment6);
        treatmentCategories.add(treatmentCategory);
        treatmentCategories.add(treatmentCategory2);
        listDataChild.put(treatmentCategories.get(0), treatmentCategory.getTreatments()); // Header, Child data
        listDataChild.put(treatmentCategories.get(1), treatmentCategory.getTreatments());
    }
}

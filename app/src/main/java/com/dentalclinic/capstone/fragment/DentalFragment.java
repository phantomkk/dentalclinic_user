package com.dentalclinic.capstone.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.adapter.ServiceAdapter;
import com.dentalclinic.capstone.models.Treatment;
import com.dentalclinic.capstone.models.TreatmentCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DentalFragment extends BaseFragment {


    public DentalFragment() {
        // Required empty public constructor
    }

    List<TreatmentCategory> treatmentCategories;
    GridView gridView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dental, container, false);
        prepareData();
        gridView = v.findViewById(R.id.gv_list_image);
        ServiceAdapter adapter = new ServiceAdapter(getContext(), treatmentCategories);
        gridView.setAdapter(adapter);

        return v;
    }


    public void prepareData(){
        treatmentCategories = new ArrayList<>();
        TreatmentCategory category = new TreatmentCategory("Trồng răng implant","https://nhakhoakim.com/wp-content/themes/Themesnhakhoaandong/assets/img/igray/11.png");
        TreatmentCategory category1 = new TreatmentCategory("Niềng răng thẩm mỹ","https://nhakhoakim.com/wp-content/themes/Themesnhakhoaandong/assets/img/igray/12.png");
        TreatmentCategory category2 = new TreatmentCategory("Bọc răng sứ","https://nhakhoakim.com/wp-content/themes/Themesnhakhoaandong/assets/img/igray/13.png");
        TreatmentCategory category3 = new TreatmentCategory("Tẩy răng trắng","https://nhakhoakim.com/wp-content/themes/Themesnhakhoaandong/assets/img/igray/21.png");
        TreatmentCategory category4 = new TreatmentCategory("Trám răng thẩm mỹ","https://nhakhoakim.com/wp-content/themes/Themesnhakhoaandong/assets/img/igray/22.png");
        TreatmentCategory category5 = new TreatmentCategory("Nhổ răng","https://nhakhoakim.com/wp-content/themes/Themesnhakhoaandong/assets/img/igray/23.png");
        TreatmentCategory category6 = new TreatmentCategory("Cạo vôi răng","https://nhakhoakim.com/wp-content/themes/Themesnhakhoaandong/assets/img/igray/31.png");
        TreatmentCategory category7 = new TreatmentCategory("Răng giả tháo lắp","https://nhakhoakim.com/wp-content/themes/Themesnhakhoaandong/assets/img/igray/32.png");
        TreatmentCategory category8 = new TreatmentCategory("Dán mặt sứ VENEER","https://nhakhoakim.com/wp-content/themes/Themesnhakhoaandong/assets/img/igray/33.png");
        treatmentCategories.add(category);
        treatmentCategories.add(category1);
        treatmentCategories.add(category2);
        treatmentCategories.add(category3);
        treatmentCategories.add(category4);
        treatmentCategories.add(category5);
        treatmentCategories.add(category6);
        treatmentCategories.add(category7);
        treatmentCategories.add(category8);
    }
}

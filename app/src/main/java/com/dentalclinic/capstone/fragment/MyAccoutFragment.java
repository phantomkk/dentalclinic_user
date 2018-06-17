package com.dentalclinic.capstone.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.activities.EditAccoutActivity;
import com.dentalclinic.capstone.adapter.PatientProfileAdapter;
import com.dentalclinic.capstone.models.City;
import com.dentalclinic.capstone.models.District;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.utils.DateTimeFormat;
import com.dentalclinic.capstone.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccoutFragment extends Fragment implements View.OnClickListener {

    Button btnChangeAvatar, btnEdit;
    CircleImageView cvAvatar;
    TextView txtName, txtGender, txtPhone, txtAddress, txtDateOfBirth;
    Patient patient;
    public MyAccoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_my_accout, container, false);
        btnChangeAvatar = v.findViewById(R.id.btn_change_avatar);
        btnChangeAvatar.setOnClickListener(this);
        btnEdit = v.findViewById(R.id.btn_edit_accout);
        btnEdit.setOnClickListener(this);
        cvAvatar = v.findViewById(R.id.img_avatar_user);
        txtName = v.findViewById(R.id.txt_name);
        txtDateOfBirth = v.findViewById(R.id.txt_date_of_birth);
        txtGender = v.findViewById(R.id.txt_gender);
        txtPhone = v.findViewById(R.id.txt_phone);
        txtAddress = v.findViewById(R.id.txt_address);
        prepareData();
        setData(patient);

        return v;
    }

    private void setData(Patient patient){
        if(patient!=null){
            if(patient.getAvatar()!=null){
                Picasso.get().load(patient.getAvatar()).into(cvAvatar);
            }
            if(patient.getName()!=null){
                txtName.setText(patient.getName());
            }
            if(patient.getDateOfBirth()!=null){
                txtDateOfBirth.setText(DateUtils.changeDateFormat(patient.getDateOfBirth(), DateTimeFormat.DATE_TIME_DB,DateTimeFormat.DATE_APP));
            }
            if(patient.getGender()!=null){
                txtGender.setText(patient.getGender());
            }
            if(patient.getPhone()!=null){
                txtPhone.setText(patient.getPhone());
            }
            if(patient.getAddress()!=null){
                String address= patient.getAddress();
                if(patient.getDistrict().getName()!=null){
                    address+=", "+getResources().getString(R.string.my_acc_district_text)+" "+patient.getDistrict().getName();
                    if(patient.getDistrict().getCity().getName()!=null){
                        address+=", "+getResources().getString(R.string.my_acc_city_text)+" "+patient.getDistrict().getCity().getName();
                    }
                }
                txtAddress.setText(address);
            }
        }
    }

    private void prepareData(){
        patient = new Patient("Vo Quoc Trinh","Quang Trung p11" ,"01695149049","1996-06-30 00:00:00","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS8lVVU1zu2SCLibBztyJFTTqCkOaNFg97S3RHyDpjbDbseoBOY");
        patient.setDistrict(new District("Go Vap", new City("Ho Chi Minh")));
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_avatar:
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1)
//                        .start(getContext(), this);
//                break;
            case R.id.btn_edit_accout:
                Intent myIntent = new Intent(getActivity(), EditAccoutActivity.class);
//                myIntent.putExtra("patient", item); //Optional parameters
                startActivity(myIntent);
                break;
        }
    }
}

package com.dentalclinic.capstone.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.activities.EditAccoutActivity;
import com.dentalclinic.capstone.activities.EditPasswordActivity;
import com.dentalclinic.capstone.adapter.PatientProfileAdapter;
import com.dentalclinic.capstone.models.City;
import com.dentalclinic.capstone.models.District;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.DateTimeFormat;
import com.dentalclinic.capstone.utils.DateUtils;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccoutFragment extends BaseFragment implements View.OnClickListener {

    Button btnChangeAvatar, btnEdit, btnChangePhone, btnChangePassword;
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
        View v = inflater.inflate(R.layout.fragment_my_accout, container, false);
        btnChangeAvatar = v.findViewById(R.id.btn_change_avatar);
        btnChangeAvatar.setOnClickListener(this);
        btnEdit = v.findViewById(R.id.btn_edit_accout);
        btnEdit.setOnClickListener(this);
//        btnChangePhone = v.findViewById(R.id.btn_edit_phone);
//        btnChangePhone.setOnClickListener(this);
        btnChangePassword = v.findViewById(R.id.btn_edit_password);
        btnChangePassword.setOnClickListener(this);
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

    private void setData(Patient patient) {
        if (patient != null) {
            if (patient.getAvatar() != null) {
                Picasso.get().load(patient.getAvatar()).into(cvAvatar);
            }
            if (patient.getName() != null) {
                txtName.setText(patient.getName());
            }
            if (patient.getDateOfBirth() != null) {
                txtDateOfBirth.setText(DateUtils.changeDateFormat(patient.getDateOfBirth(), DateTimeFormat.DATE_TIME_DB, DateTimeFormat.DATE_APP));
            }
            if (patient.getGender() != null) {
                txtGender.setText(patient.getGender());
            }
            if (patient.getPhone() != null) {
                txtPhone.setText(patient.getPhone());
            }
            if (patient.getAddress() != null) {
                String address = patient.getAddress();
                if (patient.getDistrict().getName() != null) {
                    address += ", " + getResources().getString(R.string.my_acc_district_text) + " " + patient.getDistrict().getName();
                    if (patient.getDistrict().getCity().getName() != null) {
                        address += ", " + getResources().getString(R.string.my_acc_city_text) + " " + patient.getDistrict().getCity().getName();
                    }
                }
                txtAddress.setText(address);
            }
        }
    }

    private void prepareData() {
        patient = new Patient("Vo Quoc Trinh", "Quang Trung p11", "01695149049", "1996-06-30 00:00:00", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS8lVVU1zu2SCLibBztyJFTTqCkOaNFg97S3RHyDpjbDbseoBOY");
        patient.setDistrict(new District("Go Vap", new City("Ho Chi Minh")));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_change_avatar:
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1)
                            .start(getContext(), this);
                    break;
            case R.id.btn_edit_accout:
                intent = new Intent(getActivity(), EditAccoutActivity.class);
                Patient patient = new Patient();
                patient.setId(1);
                patient.setName("vo quoc trinh");
                patient.setGender(AppConst.GENDER_OTHER);
                patient.setDateOfBirth("1996-30-06");
                patient.setAddress("Go Vap");
                patient.setDistrict(new District(2, "bảo lộc", new City(2, "Lam Dong")));
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConst.PATIENT_OBJ, patient);
                intent.putExtra("Bunder", bundle);
//                intent.putExtra(AppConst.PATIENT_OBJ,patient); //Optional parameters
                startActivity(intent);
                break;
            case R.id.btn_edit_password:
                intent = new Intent(getActivity(), EditPasswordActivity.class);
                intent.putExtra(AppConst.PATIENT_OBJ, new Patient()); //Optional parameters
                startActivity(intent);
                showMessage("Edit Password");
                break;
//            case R.id.btn_edit_phone:
//                showMessage("Edit Phone");
//                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == getActivity().RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    InputStream is = getActivity().getContentResolver().openInputStream(resultUri);
//                    uploadImage(getBytes(is));
                    showMessage("update success!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cvAvatar.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }


}

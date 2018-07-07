package com.dentalclinic.capstone.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import com.dentalclinic.capstone.activities.MainActivity;
import com.dentalclinic.capstone.adapter.PatientProfileAdapter;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.responseobject.ErrorResponse;
import com.dentalclinic.capstone.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.api.services.UserService;
import com.dentalclinic.capstone.models.City;
import com.dentalclinic.capstone.models.District;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.CoreManager;
import com.dentalclinic.capstone.utils.DateTimeFormat;
import com.dentalclinic.capstone.utils.DateUtils;
import com.dentalclinic.capstone.utils.GenderUtils;
import com.dentalclinic.capstone.utils.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccoutFragment extends BaseFragment implements View.OnClickListener {
    public static int REQUEST_CHANGE_PASSWORD = 10000;
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
//        prepareData();
        setData(CoreManager.getCurrentPatient(getContext()));

        return v;
    }

    private void setData(Patient patient) {
        if (patient != null) {
            if (patient.getAvatar() != null && patient.getAvatar().trim().length() > 0) {
//                Picasso.get().invalidate(patient.getAvatar());
                Picasso.get().load(patient.getAvatar()).into(cvAvatar);
            }
            if (patient.getName() != null) {
                txtName.setText(patient.getName());
            }
            if (patient.getDateOfBirth() != null) {
                txtDateOfBirth.setText(DateUtils.changeDateFormat(patient.getDateOfBirth(), DateTimeFormat.DATE_TIME_DB_2, DateTimeFormat.DATE_APP));
            }
            if (patient.getGender() != null) {
                txtGender.setText(GenderUtils.toString(patient.getGender()));
            }
            if (patient.getPhone() != null) {
                txtPhone.setText(patient.getPhone());
            }
            if (patient.getAddress() != null) {
                String address = patient.getAddress();
                if (patient.getDistrict() != null) {
                    address += ", " + patient.getDistrict().getName();
                    if (patient.getCity() != null) {
                        address += ", " + patient.getCity().getName();
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

    private UserService userService = APIServiceManager.getService(UserService.class);
    private Disposable userServiceDisposable;

    private void uploadImage(byte[] imageBytes) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        MultipartBody.Part id = MultipartBody.Part.createFormData("id", CoreManager.getCurrentPatient(getContext()).getId() + "");
        //cột username đang bị null hết chỉ có 2 record dc add vào: luc2, luc12345678
        userService.changeAvatar(image, id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SuccessResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        userServiceDisposable = d;
                    }

                    @Override
                    public void onSuccess(Response<SuccessResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                CoreManager.saveAvatar(getContext(), (String) response.body().getData());
                                MainActivity.resetHeader(getContext());
                                showSuccessMessage(getResources().getString(R.string.success_message_api));
                            }
                        } else if (response.code() == 500) {
                            showFatalError(response.errorBody(), "uploadImage");
                        } else if (response.code() == 401) {
                            showErrorUnAuth();
                        } else if (response.code() == 400) {
                            showBadRequestError(response.errorBody(), "uploadImage");
                        } else {
                            showDialog(getContext().getResources().getString(R.string.error_message_api));
                        }
                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        e.printStackTrace();
                        Toast.makeText(getContext(), getResources().getString(R.string.error_on_error_when_call_api), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        Bundle bundle = null;
        switch (view.getId()) {
            case R.id.btn_change_avatar:
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1)
                        .start(getContext(), this);
                break;
            case R.id.btn_edit_accout:
                intent = new Intent(getActivity(), EditAccoutActivity.class);
                bundle = new Bundle();
                bundle.putSerializable(AppConst.PATIENT_OBJ, CoreManager.getCurrentPatient(getContext()));
                intent.putExtra(AppConst.BUNDLE, bundle);
                startActivityForResult(intent, REQUEST_CHANGE_PASSWORD);
                break;
            case R.id.btn_edit_password:
                intent = new Intent(getActivity(), EditPasswordActivity.class);
                bundle = new Bundle();
                bundle.putSerializable(AppConst.PATIENT_OBJ, CoreManager.getCurrentPatient(getContext()));
                intent.putExtra(AppConst.BUNDLE, bundle);
                startActivity(intent);
                break;
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
                    showLoading();
                    uploadImage(getBytes(is));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cvAvatar.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CHANGE_PASSWORD) {
            if (resultCode == getActivity().RESULT_OK) {
                setData(CoreManager.getCurrentPatient(getContext()));
            }
        }
    }


}

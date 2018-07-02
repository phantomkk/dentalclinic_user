package com.dentalclinic.capstone.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.adapter.CitySpinnerAdapter;
import com.dentalclinic.capstone.adapter.DistrictSpinnerAdapter;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.requestobject.RegisterRequest;
import com.dentalclinic.capstone.api.requestobject.UpdatePatientRequest;
import com.dentalclinic.capstone.api.responseobject.ErrorResponse;
import com.dentalclinic.capstone.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.api.services.AddressService;
import com.dentalclinic.capstone.api.services.GuestService;
import com.dentalclinic.capstone.api.services.UserService;
import com.dentalclinic.capstone.databaseHelper.DatabaseHelper;
import com.dentalclinic.capstone.databaseHelper.DistrictDatabaseHelper;
import com.dentalclinic.capstone.fragment.MyAccoutFragment;
import com.dentalclinic.capstone.models.City;
import com.dentalclinic.capstone.models.District;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.User;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.CoreManager;
import com.dentalclinic.capstone.utils.Utils;
import com.dentalclinic.capstone.utils.Validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class EditAccoutActivity extends BaseActivity implements View.OnClickListener {
    EditText txtName;
    TextView txtDateOfBirth, txtDateError;
    AutoCompleteTextView txtAddress;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale, rbOther;
    Spinner spDistrict, spCity;
    Button btnUpdate;
    Patient patient;
    private Disposable addrServiceDisposable;
    private Disposable registerServiceDisposable;
    private Disposable districtServiceDisposable;
    private DistrictDatabaseHelper districtDatabaseHelper = new DistrictDatabaseHelper(EditAccoutActivity.this);
    private DatabaseHelper cityDatabaseHelper = new DatabaseHelper(EditAccoutActivity.this);
    private DistrictSpinnerAdapter districtSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_accout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));
        }
        Bundle bundle = getIntent().getBundleExtra(AppConst.BUNDLE);

        txtName = findViewById(R.id.edt_name);
        rgGender = findViewById(R.id.rg_gender_register);
        rbMale = findViewById(R.id.rbt_male_register);
        rbFemale = findViewById(R.id.rbt_female_register);
        rbOther = findViewById(R.id.rbt_others_register);
        txtDateOfBirth = findViewById(R.id.txt_birthday);
        txtDateError = findViewById(R.id.txt_error_date);
        txtAddress = findViewById(R.id.edt_address);
        spDistrict = findViewById(R.id.spinner_district);
        spCity = findViewById(R.id.spinner_city);
        btnUpdate = findViewById(R.id.btn_register);
        btnUpdate.setOnClickListener(this);
        txtDateOfBirth.setOnClickListener(this);
        if (bundle.getSerializable(AppConst.PATIENT_OBJ) != null) {
            patient = (Patient) bundle.getSerializable(AppConst.PATIENT_OBJ);
            setDataPatient(patient);
        } else {
            patient = new Patient();
            patient.setId(-1);
        }

//        setEvenForCityDistrict();
        if (cityDatabaseHelper.getAllCity().isEmpty()) {
            cityDatabaseHelper.insertDataCity();
        }
        if (cityDatabaseHelper.getAllDistrict().isEmpty()) {
            cityDatabaseHelper.insertDataDistrict();
        }
        spCity.setAdapter(new CitySpinnerAdapter(
                EditAccoutActivity.this,
                android.R.layout.simple_spinner_item, cityDatabaseHelper.getAllCity()));
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                City city = (City) spCity.getSelectedItem();
                if (city != null) {
                    spDistrict.setAdapter(new DistrictSpinnerAdapter(EditAccoutActivity.this,
                            android.R.layout.simple_spinner_item, cityDatabaseHelper.getDistrictOfCity(city.getId())));
                    spDistrict.setSelection(cityDatabaseHelper.getPositionDistrictById(patient.getDistrict()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spCity.setSelection(cityDatabaseHelper.getPositionCityById(patient.getCity().getId()));

    }

    @Override
    public String getMainTitle() {
        return getResources().getString(R.string.edit_acc_title);
    }

    @Override
    public void onCancelLoading() {
        if (registerServiceDisposable != null) {
            registerServiceDisposable.dispose();
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (registerServiceDisposable != null) {
//            registerServiceDisposable.dispose();
//        }
//    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                attemptRegister();
                break;
            case R.id.txt_birthday:
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(EditAccoutActivity.this,
                        (DatePicker datePicker, int iYear, int iMonth, int iDay) -> {
                            txtDateOfBirth
                                    .setText(iYear + "-" + iMonth + "-" + iDay);
                            calendar.set(iYear, iMonth, iDay);
                            Calendar currentDay = Calendar.getInstance();
                            if (currentDay.before(calendar)) {
                                txtDateError.setText(getString(R.string.label_error_birthday));
                            } else {
                                txtDateError.setText("");

                            }
                        }, year, month, day);
                dialog.show();
                break;
        }
    }

    AddressService addressService = APIServiceManager.getService(AddressService.class);

    public void setEvenForCityDistrict() {
        List<String> listCityStrs = new ArrayList<>();
        final List<City> listCitites = new ArrayList<>();
        addressService.getAllCities().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<City>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addrServiceDisposable = d;
                    }

                    @Override
                    public void onSuccess(Response<List<City>> listResponse) {
                        if (listResponse.isSuccessful()) {
                            if (listResponse.body() != null && listResponse.body().size() > 0) {
//                                listCitites.addAll(listResponse.body());
//                                for (City c : listResponse.body()) {
//                                    listCityStrs.add(c.getName());
//                            }
                                City c = new City();
                                c.setName("Chọn thành phố");
                                spCity.setAdapter(new CitySpinnerAdapter(
                                        EditAccoutActivity.this,
                                        android.R.layout.simple_spinner_item,
                                        listResponse.body()));
//                                cityDatabaseHelper.addAllCities(listResponse.body());
                                for (int i = 0; i < listResponse.body().size(); i++) {
                                    if (patient.getDistrict().getCity().getId() == listResponse.body().get(i).getId()) {
                                        spCity.setSelection(i);
                                        break;
                                    }
                                }
                            }
                        } else {
                            logError("setEvenForCityDistrict", "Success but failed");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                City city = (City) spCity.getSelectedItem();
                if (city != null) {
                    callDistrictAPI(city.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //donothing
            }
        });
    }

    public void callDistrictAPI(int id) {
//        List<String> listDistrictStrs = new ArrayList<>();
        addressService.getDistrictByCityID(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<District>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        districtServiceDisposable = d;
                    }

                    @Override
                    public void onSuccess(Response<List<District>> listResponse) {
                        if (listResponse.isSuccessful()) {
                            if (listResponse.body() != null && listResponse.body().size() > 0) {
                                spDistrict.setAdapter(new DistrictSpinnerAdapter(
                                        EditAccoutActivity.this,
                                        android.R.layout.simple_spinner_item,
                                        listResponse.body()));
//                                for (District d : listResponse.body()) {
//                                    listDistrictStrs.add(d.getName());
//                                }
//                                districtDatabaseHelper.addAllDistricts(listResponse.body());
                                for (int i = 0; i < listResponse.body().size(); i++) {
                                    if (patient.getDistrict().getId() == listResponse.body().get(i).getId()) {
                                        spDistrict.setSelection(i);
                                        break;
                                    }
                                }
                            }
                        } else if (listResponse.code() == 500) {
                            try {
                                showMessage(getString(R.string.error_server));
                                logError("Call API", listResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            showMessage("Có lỗi xảy ra");
                            logError("callDistrictAPI", "Success but on failed");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showMessage(e.getMessage());
                        logError("callDistrictAPI", "onError" + e.getMessage());
                    }
                });
    }

    public void attemptRegister() {
        txtName.setError(null);
        txtAddress.setError(null);
        View focusView = null;
        boolean cancel = false;
        String name = txtName.getText().toString().trim();
        String address = txtAddress.getText().toString().trim();
        String birthdayStr = txtDateOfBirth.getText().toString().trim();
        District district = (District) spDistrict.getSelectedItem();
        int districtID = -1;
        if (district != null) {
            districtID = district.getId();
        }
        if (!Validation.isNameValid(name)) {
            cancel = true;
            txtName.setError(getString(R.string.error_invalid_name));
            focusView = txtName;
        } else if (!Validation.isAddressValid(address)) {
            cancel = true;
            txtAddress.setError(getString(R.string.error_invalid_address));
            focusView = txtAddress;
        } else if (Validation.isNullOrEmpty(birthdayStr)) {
            cancel = true;
            txtDateOfBirth.setText(getString(R.string.label_error_birthday));
            focusView = txtDateOfBirth;
        } else if (birthdayStr != null && birthdayStr.equals(getString(R.string.label_birthday_register))) {
            cancel = true;
            txtDateError.setText(getString(R.string.label_error_birthday));
            focusView = txtDateError;
        }
        String gender = getGenderValue(rgGender.getCheckedRadioButtonId());
        if (cancel) {
            focusView.requestFocus();
        } else {
            UpdatePatientRequest request = new UpdatePatientRequest();
            request.setPatientId(patient.getId());
            request.setName(name);
            request.setAddress(address);
            request.setGender(gender);
            request.setDistrictId(districtID);
            request.setBirthday(birthdayStr);
            callApiUpdate(request);

        }
    }

    public void setDataPatient(Patient patient) {
        if (patient != null) {
            if (patient.getName() != null) {
                txtName.setText(patient.getName());
            }
            if (patient.getAddress() != null) {
                txtAddress.setText(patient.getAddress());
            }
            if (patient.getDateOfBirth() != null) {
                txtDateOfBirth.setText(patient.getDateOfBirth());
            }
            if (patient.getGender() != null) {
                checkedGender(patient.getGender());
            }
        }
    }

    public void checkedGender(String gender) {
        switch (gender) {
            case AppConst.GENDER_MALE:
                rbMale.setChecked(true);
                break;
            case AppConst.GENDER_FEMALE:
                rbFemale.setChecked(true);
                break;
            case AppConst.GENDER_OTHER:
                rbOther.setChecked(true);
                break;
            default:
                rbOther.setChecked(true);
                break;
        }
    }

    public String getGenderValue(int gender) {
        String value;
        switch (gender) {
            case R.id.rbt_male_register:
                value = AppConst.GENDER_MALE;
                break;
            case R.id.rbt_female_register:
                value = AppConst.GENDER_FEMALE;
                break;
            default:
                value = AppConst.GENDER_OTHER;
                break;
        }
        return value;
    }

    public void callApiUpdate(UpdatePatientRequest requestObj) {
        showLoading();
        UserService userService = APIServiceManager.getService(UserService.class);
        userService.changePatientInfo(requestObj)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SuccessResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        registerServiceDisposable = d;
                        hideLoading();
                    }

                    @Override
                    public void onSuccess(Response<SuccessResponse> response) {
                        if (response.isSuccessful()) {
//                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditAccoutActivity.this)
//                                    .setMessage("Đăng kí tài khoản thành công")
//                                    .setPositiveButton("Đăng nhập", (DialogInterface dialogInterface, int i) -> {
//                                        Intent intent = new Intent(EditAccoutActivity.this, LoginActivity.class);
//                                        startActivity(intent);
//                                    });
//                            alertDialog.show();
                            CoreManager.savePatient(EditAccoutActivity.this, requestObj);
                            MainActivity.resetHeader(EditAccoutActivity.this);
                            showMessage(getResources().getString(R.string.success_message_api));
                            setResult(RESULT_OK);
                            finish();
                        } else if (response.code() == 500) {
                            try {
                                String error = response.errorBody().string();
                                showMessage(getString(R.string.error_server));
                                logError("Call API", error);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
//                            String erroMsg = Utils.getErrorMsg(response.errorBody());
                            try {
                                String error = response.errorBody().string();
                                ErrorResponse errorResponse = Utils.parseJson(error, ErrorResponse.class);

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditAccoutActivity.this)
                                        .setMessage(errorResponse.getErrorMessage())
                                        .setPositiveButton("Thử lại", (DialogInterface dialogInterface, int i) -> {
                                        });
                                alertDialog.show();
                                logError("CallApiRegister", "SuccessBut on Failed" + errorResponse.getExceptionMessage());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showMessage(e.getMessage());
                        logError("CallApiRegister", e.getMessage());
                        hideLoading();
                    }
                });
    }


}

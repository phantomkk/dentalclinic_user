package com.dentalclinic.capstone.activities;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.adapter.CitySpinnerAdapter;
import com.dentalclinic.capstone.adapter.DistrictSpinnerAdapter;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.requestobject.RegisterRequest;
import com.dentalclinic.capstone.api.services.AddressService;
import com.dentalclinic.capstone.api.services.GuestService;
import com.dentalclinic.capstone.api.services.PatientService;
import com.dentalclinic.capstone.api.services.UserService;
import com.dentalclinic.capstone.models.City;
import com.dentalclinic.capstone.models.District;
import com.dentalclinic.capstone.models.User;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.Validation;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {
    private Disposable addrServiceDisposable;
    private Disposable registerServiceDisposable;
    private Disposable districtServiceDisposable;

    private EditText edtFullname;
    private EditText edtPhone;
    private EditText edtPassword;
    private RadioGroup radioGroup;
    private EditText edtAddress;
    private Button btnRegister;
    private TextView tvBirthday;
    private TextView tvErrorBirthday;
    private Spinner spnCity;
    private Spinner spnDistrict;

    private int selectDistrictID = -1;

    AddressService addressService = APIServiceManager.getService(AddressService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtFullname = findViewById(R.id.edt_fullname_register);
        edtPhone = findViewById(R.id.edt_phone_register);
        edtPassword = findViewById(R.id.edt_password_register);
        edtAddress = findViewById(R.id.edt_address_register);
        radioGroup = findViewById(R.id.rg_gender_register);
        tvBirthday = findViewById(R.id.tv_birthday_register);
        tvErrorBirthday = findViewById(R.id.txt_error_date_register);
        btnRegister = findViewById(R.id.btn_register);
        spnCity = findViewById(R.id.spinner_city_register);
        spnDistrict = findViewById(R.id.spinner_district_register);

        btnRegister.setOnClickListener((view) -> {
            attemptRegister();
        });

        setEventForBirthday();
        setEvenForCityDistrict();
    }

    public void setEventForBirthday() {
        //init birthday section
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        tvBirthday.setText(year + "/" + month + "/" + day);
        tvBirthday.setOnClickListener((view) -> {
            DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this,
                    (DatePicker datePicker, int iYear, int iMonth, int iDay) -> {
                        tvBirthday
                                .setText(iYear + "/" + iMonth + "/" + iDay);
                        calendar.set(iYear, iMonth, iDay);
                        Calendar currentDay = Calendar.getInstance();
                        if (currentDay.after(calendar)) {
                            tvErrorBirthday.setText(getString(R.string.label_error_birthday));
                        } else {
                            tvErrorBirthday.setText("");

                        }
                    }, year, month, day);
            dialog.show();
        });

    }

    public void setEvenForCityDistrict() {
        List<String> listCityStrs = new ArrayList<>();
        final List<City> listCitites = new ArrayList<>();
        addressService.getAllCities().subscribeOn(Schedulers.io())
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
                                listResponse.body().add(new City());
                                spnCity.setAdapter(new CitySpinnerAdapter(
                                        RegisterActivity.this,
                                        android.R.layout.simple_spinner_item,
                                        listResponse.body()));

//                            listCityStrs.add(0,"Chọn thành phố");
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
        spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                City city = (City) spnCity.getSelectedItem();
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
                .subscribeOn(Schedulers.io())
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
                                spnDistrict.setAdapter(new DistrictSpinnerAdapter(
                                        RegisterActivity.this,
                                        android.R.layout.simple_spinner_item,
                                        listResponse.body()));
//                                for (District d : listResponse.body()) {
//                                    listDistrictStrs.add(d.getName());
//                                }
                            }
                        } else {
                            logError("callDistrictAPI", "Success but on failed");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        logError("callDistrictAPI", "onError" + e.getMessage());
                    }
                });
    }

    public void attemptRegister() {
        edtFullname.setError(null);
        edtPhone.setError(null);
        edtPassword.setError(null);
        edtAddress.setError(null);
        View focusView = null;
        boolean cancel = false;
        String name = edtFullname.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String birthdayStr = tvBirthday.getText().toString().trim();
        District district = (District) spnDistrict.getSelectedItem();
        int districtID = -1;
        if (district != null) {
            districtID = district.getId();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        Date birthday = null;
        try {
            if (birthdayStr != null && birthdayStr.length() > 0) {
                birthday = simpleDateFormat.parse(birthdayStr);
                tvErrorBirthday.setText("");
            }
        } catch (ParseException e) {
            tvErrorBirthday.setText(getString(R.string.label_error_birthday));
            e.printStackTrace();
        }
        if (!Validation.isNameValid(name)) {
            cancel = true;
            edtFullname.setError(getString(R.string.error_invalid_name));
            focusView = edtFullname;
        } else if (!Validation.isPhoneValid(phone)) {
            cancel = true;
            edtPhone.setError(getString(R.string.error_invalid_phone));
            focusView = edtPhone;

        } else if (!Validation.isPasswordValid(password)) {
            edtPassword.setError(getString(R.string.error_invalid_password));
            cancel = true;
            focusView = edtPassword;
        } else if (!Validation.isAddressValid(address)) {
            cancel = true;
            edtAddress.setError(getString(R.string.error_invalid_address));
            focusView = edtAddress;
        } else if (Validation.isNullOrEmpty(birthdayStr)) {
            cancel = true;
            tvBirthday.setText(getString(R.string.label_error_birthday));
        }
        int gender = getGenderValue(radioGroup.getCheckedRadioButtonId());
        if (cancel) {
            focusView.requestFocus();
        } else {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setPhone(phone);
            registerRequest.setAddress(address);
            registerRequest.setPassword(password);
            registerRequest.setDistrictId(districtID);
            registerRequest.setFullname(name);
            registerRequest.setGender(gender);
            registerRequest.setBirthday(birthday);
            callApiRegister(registerRequest);

        }
    }

    public int getGenderValue(int id) {
        int value;
        switch (id) {
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

    public void callApiRegister(RegisterRequest requestObj) {
        showLoading();
        GuestService guestService = APIServiceManager.getService(GuestService.class);
        guestService.register(requestObj)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        registerServiceDisposable = d;
                        hideLoading();
                    }

                    @Override
                    public void onSuccess(Response<User> userResponse) {
                        if (userResponse.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            logError("CallApiRegister", "SuccessBut on Failed");
                        }
                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        logError("CallApiRegister", e.getMessage());
                        hideLoading();
                    }
                });
    }

    @Override
    public String getMainTitle() {
        return "Đăng kí tài khoản";
    }

    @Override
    public void onCancelLoading() {
        //do nothing
        if (addrServiceDisposable != null) {
            addrServiceDisposable.dispose();
        }
        if (districtServiceDisposable != null) {
            districtServiceDisposable.dispose();
        }
        if (registerServiceDisposable != null) {
            registerServiceDisposable.dispose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (addrServiceDisposable != null) {
            addrServiceDisposable.dispose();
        }
        if (districtServiceDisposable != null) {
            districtServiceDisposable.dispose();
        }
        if (registerServiceDisposable != null) {
            registerServiceDisposable.dispose();
        }
    }
}
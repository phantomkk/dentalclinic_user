package com.dentalclinic.capstone.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.content.res.AppCompatResources;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.requestobject.AppointmentRequest;
import com.dentalclinic.capstone.api.responseobject.ErrorResponse;
import com.dentalclinic.capstone.api.services.AppointmentService;
import com.dentalclinic.capstone.models.Appointment;
import com.dentalclinic.capstone.utils.CoreManager;
import com.dentalclinic.capstone.utils.DateTimeFormat;
import com.dentalclinic.capstone.utils.DateUtils;
import com.dentalclinic.capstone.utils.Utils;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.User;
import com.dentalclinic.capstone.utils.CoreManager;
import com.dentalclinic.capstone.utils.Validation;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class QuickBookActivity extends BaseActivity {
    private AutoCompleteTextView tvPhone;
    private AutoCompleteTextView tvFullname;
    private TextView tvDate;
    private TextView tvDateError;
    private AutoCompleteTextView comtvNote;
    private Button btnQuickBook;
    private Disposable appointmentDisposable;
    private User user;
    private Patient patient;
    private boolean  isDateValid = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_register);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(
                    ContextCompat.getDrawable(QuickBookActivity.this, R.drawable.side_nav_bar)
            );
        }
//        ImageView img = findViewById(R.id.img_logo_quick_register);
        tvFullname = findViewById(R.id.tv_fullname_quickbook);
        tvDate = findViewById(R.id.tv_date_quickbook);
        tvDateError = findViewById(R.id.tv_date_error_quickbook);
//        tvTime = findViewById(R.id.tv_time_quickbook);
        tvPhone = findViewById(R.id.tv_phone_quickbook);
        btnQuickBook = findViewById(R.id.btn_quickbook);
        comtvNote = findViewById(R.id.comtv_content_quickbook);

//        img.requestFocus();
        tvPhone.clearFocus();
//        tvFullname.clearFocus();


        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        tvDate.setOnClickListener((view) ->
        {
            DatePickerDialog dialog = new DatePickerDialog(this,
                    (DatePicker datePicker, int iYear, int iMonth, int iDay) -> {
                        String date = iDay + "/" + (iMonth + 1) + "/" + iYear;
                        c.set(iYear, iMonth, iDay,23,59);
                        Calendar currentDay = Calendar.getInstance();
                        if (currentDay.after(c)) {
                            tvDateError.setText(getString(R.string.label_error_appnt_date));
                            isDateValid= false;
                        } else {
                            tvDateError.setText("");
                            isDateValid=true;
                        }
                        tvDate.setText(DateUtils.getDate(c.getTime(), DateTimeFormat.DATE_APP));
                        tvDate.setTextColor(
                                ContextCompat.getColor(QuickBookActivity.this, R.color.color_black)
                        );
                    }, year, month, day);
            dialog.show();
        });
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);
//        tvTime.setOnClickListener((view) ->
//        {
//            TimePickerDialog dialog = new TimePickerDialog(this,
//                    (TimePicker timePicker, int tHour, int tMinute) ->{
//                    tvTime.setText(tHour + ":" + tMinute);
//            }, hour, minute, true);
//            dialog.show();
//        });
        if(CoreManager.getCurrentPatient(QuickBookActivity.this)!=null){
            patient= CoreManager.getCurrentPatient(QuickBookActivity.this);
            if(patient.getPhone()!=null){
                tvPhone.setText(patient.getPhone());
            }
            if(patient.getName()!=null){
                tvFullname.setText(patient.getName());

            }
        }

        btnQuickBook.setOnClickListener((view) -> {
            if (isValidateForm()) {
                AppointmentRequest requestObj = getFormData();
                if (requestObj != null) {
                    callApi(requestObj);
                } else {
                    showWarningMessage("Error null");
                }
            }

//            Intent intent = new Intent(QuickBookActivity.this, SelectDentistActivity.class);
//            startActivity(intent);
        });

    }

    @Override
    public String getMainTitle() {
        return "Đặt lịch";
    }

    @Override
    public void onCancelLoading() {
        if (appointmentDisposable != null) {
            appointmentDisposable.dispose();
        }
    }

    public AppointmentRequest getFormData() {
//        User user = CoreManager.getUser(this);
//        if (user != null) {
//            String phone = user.getPhone();
        String phone = "01678589696";
        String dateBooking = tvDate.getText().toString().trim();
        String note = comtvNote.getText().toString().trim();
        String name = tvFullname.getText().toString().trim();
        String bookingDate =DateUtils.changeDateFormat(
                dateBooking,
                DateTimeFormat.DATE_APP,
                DateTimeFormat.DATE_TIME_DB);
        AppointmentRequest request = new AppointmentRequest();
        request.setDate(bookingDate);
        request.setNote(note);
        request.setFullname(name);
        request.setPhone(phone);
        return request;
//        }
//        return null;
    }

    public boolean isValidateForm() {
        boolean isAllFieldValid = true;
        String phone = tvPhone.getText().toString().trim();
        String note = comtvNote.getText().toString().trim();
        String txtDate = tvDate.getText().toString().trim();
        View viewFocus = null;
        if (!Validation.isPhoneValid(phone)) {
            viewFocus = tvPhone;
            tvPhone.setError(getString(R.string.error_invalid_phone));
            isAllFieldValid = false;
        } else if (Validation.isNullOrEmpty(txtDate)
                || (txtDate!=null && txtDate.equals(getString(R.string.label_date_quickbook)))) {
            viewFocus = tvDateError;
            tvDateError.setText("Vui lòng chọn ngày");
            isAllFieldValid = false;
        }
        if (!isAllFieldValid) {
            viewFocus.requestFocus();
        }
        return isAllFieldValid && isDateValid;
    }

    public void callApi(AppointmentRequest requestObj) {
        showLoading();
        AppointmentService appointmentService =
                APIServiceManager.getService(AppointmentService.class);
        appointmentService.bookAppointment(requestObj)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<Appointment>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        appointmentDisposable = d;
                    }

                    @Override
                    public void onSuccess(Response<List<Appointment>> response) {
                        if (response.isSuccessful()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(QuickBookActivity.this)
                                    .setTitle("Đặt lịch thành công")
                                    .setPositiveButton("Xác nhận", (DialogInterface var1, int var2) -> {
                                        finish();
                                    });
                            builder.create().show();
                        } else if (response.code() == 500) {
                            showFatalError(response.errorBody(), "appointmentService");
                        } else if (response.code() == 401) {
                            showErrorUnAuth();
                        } else if (response.code() == 400) {
                            showBadRequestError(response.errorBody(), "appointmentService");
                        } else {
                            showErrorMessage(getString(R.string.error_on_error_when_call_api));
                        }

                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        e.printStackTrace();
                        showWarningMessage(getResources().getString(R.string.error_on_error_when_call_api));
                        logError(QuickBookActivity.class, "callApi", e.getMessage());
                        hideLoading();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}

package com.dentalclinic.capstone.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.requestobject.AppointmentRequest;
import com.dentalclinic.capstone.api.services.AppointmentService;
import com.dentalclinic.capstone.models.Appointment;
import com.dentalclinic.capstone.utils.Validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class QuickBookActivity extends BaseActivity {
    private AutoCompleteTextView tvPhone;
    private AutoCompleteTextView tvFullname;
    private View edtPassword;
    private TextView tvDate;
    private TextView tvDateError;
    private AutoCompleteTextView comtvNote;
    //    private TextView tvTime;
    private Button btnQuickBook;
    private Disposable appointmentDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_register);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
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
                        String date = iDay + "/" + iMonth + "/" + iYear;
                        c.set(iYear, iMonth, iDay);
                        Calendar currentDay = Calendar.getInstance();
                        if (currentDay.after(c)) {
                            tvDateError.setText(getString(R.string.label_error_birthday));
                        } else {
                            tvDateError.setText("");
                        }
                        tvDate.setText(date);
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

        btnQuickBook.setOnClickListener((view) -> {
            if (isValidateForm()) {
                AppointmentRequest requestObj = getFormData();
                if (requestObj != null) {
                    callApi(requestObj);
                } else {
                    showMessage("Error null");
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
        String name = tvFullname.getText().toString().trim();SimpleDateFormat smp = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        SimpleDateFormat smp2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);Date d1 = new Date();
        try {
            d1= smp.parse(dateBooking);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String bookingDate = smp2.format(d1);
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
        String tmpBirthday = tvDate.getText().toString().trim();
        View viewFocus = null;
        SimpleDateFormat smp = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat smp2 = new SimpleDateFormat("yyyy-MM-dd");Date d1 = new Date();
        try {
              d1= smp.parse(tmpBirthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String birthday = smp2.format(d1);
        if (!Validation.isPhoneValid(phone)) {

        }
        if (!Validation.isPhoneValid(phone)) {
            viewFocus = tvPhone;
//            tvPhone.requestFocus();
            tvPhone.setError(getString(R.string.error_invalid_phone));
            isAllFieldValid= false;
//        } else if (!Validation.isNullOrEmpty(note)) {
//            comtvNote.setError("Vui lòng nhập ghi chu");
//            isAllFieldValid= false;
        } else if (Validation.isNullOrEmpty(birthday))
        {
            viewFocus = tvDateError;
            tvDateError.setText("Vui lòng chọn ngày");
//            tvDate.setError();
            isAllFieldValid= false;
        }
            if (!isAllFieldValid) {
                viewFocus.requestFocus();
            }
        return isAllFieldValid;
    }

    public void callApi(AppointmentRequest requestObj) {
        showLoading();
        AppointmentService appointmentService =
                APIServiceManager.getService(AppointmentService.class);
        appointmentService.bookAppointment(requestObj)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<Appointment>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        appointmentDisposable = d;
                    }

                    @Override
                    public void onSuccess(Response<Appointment> appointmentResponse) {
                        if (appointmentResponse.isSuccessful()) {
                            Toast.makeText(QuickBookActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(QuickBookActivity.this)
                                    .setTitle("Đặt lịch thành công")
                                    .setPositiveButton("Xác nhận",(DialogInterface var1, int var2)->{finish();});
                            builder.create().show();
                        } else {
                            Toast.makeText(QuickBookActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        e.printStackTrace();
                        logError(QuickBookActivity.class, "callApi", e.getMessage());hideLoading();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}

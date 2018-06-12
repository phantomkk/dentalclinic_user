package com.dentalclinic.capstone.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.requestobject.AppointmentRequest;
import com.dentalclinic.capstone.api.services.AppointmentService;
import com.dentalclinic.capstone.models.Appointment;
import com.dentalclinic.capstone.models.User;
import com.dentalclinic.capstone.utils.CoreManager;
import com.dentalclinic.capstone.utils.Validation;

import java.util.Calendar;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class QuickRegisterActivity extends BaseActivity {
    private AutoCompleteTextView tvPhone;
//    private AutoCompleteTextView tvFullname;
    private View edtPassword;
    private TextView tvDate;
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
//        tvFullname = findViewById(R.id.tv_fullname_quickbook);
        tvDate = findViewById(R.id.tv_date_quickbook);
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
                            tvDate.setError(getString(R.string.label_error_birthday));
                        } else {
                            tvDate.setError("");
                        }
                        tvDate.setText(date);
                    }, year, month, day);
            dialog.show();
        });
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
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

//            Intent intent = new Intent(QuickRegisterActivity.this, SelectDentistActivity.class);
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
        AppointmentRequest request = new AppointmentRequest();
        request.setDate(dateBooking);
        request.setNote(note);
        request.setPhone(phone);
        return request;
//        }
//        return null;
    }

    public boolean isValidateForm() {
        boolean isAllFieldValid = false;
        String phone = tvPhone.getText().toString().trim();
        String note = comtvNote.getText().toString().trim();
        String birthday = tvDate.getText().toString().trim();
        View viewFocus = null;
        if (!Validation.isPhoneValid(phone)) {

        }
        if (!Validation.isPhoneValid(phone)) {
            viewFocus = tvPhone;
//            tvPhone.requestFocus();
            tvPhone.setError(getString(R.string.error_invalid_phone));
        } else if (!Validation.isNullOrEmpty(note)) {

        } else if (Validation.isNullOrEmpty(birthday))
        {
            viewFocus = tvDate;
//            tvDate.setError();
        }
            if (isAllFieldValid) {
                viewFocus.requestFocus();
            }
        return isAllFieldValid;
    }

    public void callApi(AppointmentRequest requestObj) {
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
                            Toast.makeText(QuickRegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(QuickRegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        logError(QuickRegisterActivity.class, "callApi", e.getMessage());
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}

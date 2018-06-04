package com.dentalclinic.capstone.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dentalclinic.capstone.R;

import java.util.ArrayList;
import java.util.Calendar;

public class QuickRegisterActivity extends AppCompatActivity {
    private View tvPhone;
    private View tvFullname;
    private View edtPassword;
    private TextView tvDate;
    private TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ImageView img = findViewById(R.id.img_logo_quick_register);
        tvFullname = findViewById(R.id.tv_fullname_quickbook);
        tvDate = findViewById(R.id.tv_date_quickbook);
        tvTime = findViewById(R.id.tv_time_quickbook);
        tvPhone = findViewById(R.id.tv_phone_quickbook);
        img.requestFocus();
        tvPhone.clearFocus();
        tvFullname.clearFocus();
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        tvDate.setOnClickListener((view) ->
        {
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    tvDate.setText(day + "/" + month + "/" + year);
                }
            }, year, month, day);
            dialog.show();
        });
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        tvTime.setOnClickListener((view) ->
        {
            TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int tHour, int tMinute) {
                    tvTime.setText(tHour + ":" + tMinute);

                }
            }, hour, minute, true);
            dialog.show();
        });

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}

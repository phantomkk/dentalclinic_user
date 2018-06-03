package com.dentalclinic.capstone.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.dentalclinic.capstone.R;

public class QuickRegisterActivity extends AppCompatActivity {
private View tvPhone;
private View tvFullname;
private View edtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_register);
        ImageView img = (ImageView)findViewById(R.id.img_logo_quick_register);
            tvFullname= (EditText)findViewById(R.id.tv_fullname_quickregister);
        tvPhone= (EditText)findViewById(R.id.tv_phone_quickregister);
        img.requestFocus();
        tvPhone.clearFocus();
        tvFullname.clearFocus();
    }
}

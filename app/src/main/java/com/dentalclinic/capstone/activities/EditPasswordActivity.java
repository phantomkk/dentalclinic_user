package com.dentalclinic.capstone.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.api.requestobject.RegisterRequest;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.Validation;

public class EditPasswordActivity extends BaseActivity implements View.OnClickListener {
    private EditText txtPassword, txtConfirmPassword;
    private Patient patient;
    private Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));
        }
        if (getIntent().getSerializableExtra(AppConst.PATIENT_OBJ) != null) {
            patient = (Patient) getIntent().getSerializableExtra(AppConst.PATIENT_OBJ);
        } else {
            patient = new Patient();
            patient.setId(-1);
        }
        txtPassword = findViewById(R.id.edt_password);
        txtConfirmPassword = findViewById(R.id.edt_confirm_password);
        btnChangePassword = findViewById(R.id.btn_update_password);
        btnChangePassword.setOnClickListener(this);

    }

    @Override
    public String getMainTitle() {
        return getResources().getString(R.string.edit_pass_title);
    }

    @Override
    public void onCancelLoading() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public boolean isValid() {
        boolean isValid = true;
        txtPassword.setError(null);
        txtConfirmPassword.setError(null);
        View focusView = null;
        String password = txtPassword.getText().toString().trim();
        String confirmPassword = txtConfirmPassword.getText().toString().trim();
        if (!confirmPassword.equals(password)) {
            txtConfirmPassword.setError(getString(R.string.error_invalid_confirm_password));
            isValid = false;
            focusView = txtConfirmPassword;
        } else if (!Validation.isPasswordValid(password)) {
            txtPassword.setError(getString(R.string.error_invalid_password));
            isValid = false;
            focusView = txtPassword;
        }
        if (!isValid) {
            focusView.requestFocus();
        }
        return isValid;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_password:
                if(!isValid()){

                }
                break;
        }
    }
}

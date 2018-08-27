package com.dentalclinic.capstone.activities;

import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.requestobject.RegisterRequest;
import com.dentalclinic.capstone.api.responseobject.ErrorResponse;
import com.dentalclinic.capstone.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.api.services.UserService;
import com.dentalclinic.capstone.models.FingerAuthObj;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.CoreManager;
import com.dentalclinic.capstone.utils.Utils;
import com.dentalclinic.capstone.utils.Validation;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class EditPasswordActivity extends BaseActivity implements View.OnClickListener {
    private EditText txtPassword, txtConfirmPassword, txtCurrentPassword;
    private Patient patient;
    private Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(
                    ContextCompat.getDrawable(this, R.drawable.side_nav_bar)
            );
        }
        Bundle bundle = getIntent().getBundleExtra(AppConst.BUNDLE);
        if (bundle.getSerializable(AppConst.PATIENT_OBJ) != null) {
            patient = (Patient) bundle.getSerializable(AppConst.PATIENT_OBJ);
        } else {
            patient = new Patient();
            patient.setId(-1);
        }
        txtPassword = findViewById(R.id.edt_password);
        txtCurrentPassword = findViewById(R.id.edt_current_password);
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
        String currentPassword = txtCurrentPassword.getText().toString().trim();
        String confirmPassword = txtConfirmPassword.getText().toString().trim();
        if (!Validation.isPasswordValid(currentPassword)) {
            txtCurrentPassword.setError(getString(R.string.error_invalid_password));
            isValid = false;
            focusView = txtCurrentPassword;
        } else if (!confirmPassword.equals(password)) {
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
                if (isValid()) {
                    callApiUpdate(patient.getPhone(), txtCurrentPassword.getText().toString().trim(), txtPassword.getText().toString().trim());
                }
                break;
        }
    }

    private Disposable userServiceDisposable;

    public void callApiUpdate(String phone, String currentPassword, String newPassword) {
        showLoading();
        UserService userService = APIServiceManager.getService(UserService.class);

        userService.changePassword(phone, currentPassword, newPassword)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SuccessResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        userServiceDisposable = d;
                    }

                    @Override
                    public void onSuccess(Response<SuccessResponse> response) {
                        if (response.isSuccessful()) {
                            FingerAuthObj obj = CoreManager.getFingerAuthObj(EditPasswordActivity.this);
                            obj.setPassword(newPassword);
                            CoreManager.setFingerAuthObj(EditPasswordActivity.this,obj);
                            showSuccessMessage(getResources().getString(R.string.success_message_api));
                            finish();
                        } else if (response.code() == 500) {
                            showFatalError(response.errorBody(), "changePassword");
                        } else if (response.code() == 401) {
                            showErrorUnAuth();
                        } else if (response.code() == 400) {
                            showBadRequestError(response.errorBody(), "changePassword");
                        } else {
                            showErrorMessage(getString(R.string.error_on_error_when_call_api));
                        }
                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showWarningMessage(getResources().getString(R.string.error_on_error_when_call_api));
                        logError("CallApiRegister", e.getMessage());
                        hideLoading();
                    }
                });
    }


}

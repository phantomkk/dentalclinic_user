package com.dentalclinic.capstone.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.services.PatientService;
import com.dentalclinic.capstone.api.services.UserService;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.User;
import com.dentalclinic.capstone.utils.CoreManager;
import com.dentalclinic.capstone.utils.Utils;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {
    // UI references.
    private AutoCompleteTextView txtPhone;
    private EditText txtPassword;
    private View mProgressView;
    private View mLoginFormView;
    private View btnLinkAppointment;
    private View btnSingin;
    private TextView txtErrorServer;


    private Disposable disposable;

    @Override
    public String getMainTitle() {
        return "Đăng nhập";
    }

    @Override
    public void onCancelLoading() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        txtPhone = findViewById(R.id.txt_phone_loginact);
        txtErrorServer = findViewById(R.id.txt_error_server_loginact);
        txtPassword = findViewById(R.id.password);
        btnLinkAppointment = findViewById(R.id.link_book_appointment_loginact);
        txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        TextView txtLinkAppointment = findViewById(R.id.link_book_appointment_loginact);
        txtLinkAppointment.setOnClickListener((view) -> {
//            attemptLogin();
        });

        btnLinkAppointment.setOnClickListener((view) ->
        {
            Intent intent = new Intent(LoginActivity.this, QuickRegisterActivity.class);
            startActivity(intent);
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        btnSingin = findViewById(R.id.btn_signin_loginact);
        btnSingin.setOnClickListener((view) ->
        {
            attemptLogin();
//            showLoading();
        });


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.dummy_focus_loginact);
        linearLayout.requestFocus();
        txtPassword.clearFocus();
        txtPhone.clearFocus();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        txtErrorServer.setText("");
        txtPhone.setError(null);
        txtPassword.setError(null);

        // Store values at the time of the login attempt.
        String phone = txtPhone.getText().toString();
        String password = txtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.

        if (TextUtils.isEmpty(phone) && !isPhoneValid(phone)) {
            txtPhone.setError(getString(R.string.error_invalid_phone));
            focusView = txtPhone;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            txtPassword.setError(getString(R.string.error_invalid_password));
            focusView = txtPassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            callApiLogin(phone, password);
        }
    }

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.matches("^\\d{9,}$");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 8;
    }

    public void callApiLogin(String phone, String password) {
        showLoading();
        PatientService patientService = APIServiceManager.getService(PatientService.class);
        patientService.login(phone, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        hideLoading();
                    }

                    @Override
                    public void onSuccess(Response<User> userResponse) {
                        if (userResponse.isSuccessful()) {
//                            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            //////donothing
                            CoreManager.setUser(LoginActivity.this, userResponse.body());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            String errorMsg = Utils.getErrorMsg(userResponse.errorBody());
                            Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                            logError(LoginActivity.class, "onSuccess", "OnSuccess but on failed");
                            txtErrorServer.setText(errorMsg);
                        }
                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        logError(LoginActivity.class, "attemptLogin", e.getMessage());
                        hideLoading();

                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}


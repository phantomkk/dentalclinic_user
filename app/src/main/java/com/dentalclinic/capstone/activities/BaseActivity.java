package com.dentalclinic.capstone.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.api.responseobject.ErrorResponse;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.Utils;

public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getMainTitle());
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public abstract String getMainTitle();


    public void showDialog(String message){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Thử lại", (DialogInterface dialogInterface, int i) -> {
                });
        alertDialog.show();
    }
    public void showLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.waiting_msg));
            progressDialog.setOnCancelListener((DialogInterface dialog) -> {
                showMessage(getString(R.string.dialog_cancel));
                onCancelLoading();
                hideLoading();
            });
            progressDialog.show();
        }
    }

    public void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public abstract void onCancelLoading();

    public void logError(String activity, String method, String message) {
        Log.e(AppConst.DEBUG_TAG, activity + "." + method + "(): " + message);
    }

    public void logError(Class t, String method, String message) {
        Log.e(AppConst.DEBUG_TAG, t.getSimpleName() + "." + method + "(): " + message);
    }

    public void logError(String method, String message) {
        Log.e(AppConst.DEBUG_TAG, this.getClass().getSimpleName() + "." + method + "(): " + message);
    }


}

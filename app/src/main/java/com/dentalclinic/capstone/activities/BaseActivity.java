package com.dentalclinic.capstone.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity{
    private ProgressDialog progressDialog;

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    showMessage("Cancel dialog");
                }
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

    public void logError(String activity, String method, String message) {
        Log.e("LOG_ERROR", activity + "." + method + "(): " + message);
    }


}

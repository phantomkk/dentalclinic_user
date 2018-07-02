package com.dentalclinic.capstone.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
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

import java.net.InetAddress;

public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getMainTitle());
        showMessNetword();
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public abstract String getMainTitle();


    public void showDialog(String message) {
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
        Log.e(AppConst.DEBUG_TAG, "Activity" + "." + method + "(): " + message);
//        Log.e(AppConst.DEBUG_TAG, this.getClass().getSimpleName() + "." + method + "(): " + message);
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public void showMessNetword() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    Toast.makeText(this, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
                    if (!activeNetwork.isAvailable()) {
                        showMessage("Vui lòng kiểm tra kết nối mạng của bạn đã được bật.");
                    }
                } else if ( activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    if (!activeNetwork.isAvailable()) {
                        showMessage("Vui lòng kiểm tra kết nối mạng của bạn đã được bật.");
                    }
                }
            } else {
                // not connected to the internet
                logError("CHECK Network Base Activity","ot connected to the internet");
                     showMessage("Vui lòng kiểm tra kết nối mạng của bạn đã được bật.");
            }
        }
////For 3G check
//        boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
//                .isConnectedOrConnecting();
////For WiFi Check
//        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//                .isConnectedOrConnecting();
//
//        System.out.println(is3g + " net " + isWifi);
//
//        if (!is3g && !isWifi)
//        {
//           showMessage("Vui lòng kiểm tra kết nối mạng của bạn đã được bật.");
//        }
    }

}

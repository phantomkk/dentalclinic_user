package com.dentalclinic.capstone.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.api.responseobject.ErrorResponse;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.Utils;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.IOException;

import okhttp3.ResponseBody;

public class BaseFragment extends Fragment {
    private ProgressDialog mProgressDialog;

    protected void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());

            mProgressDialog.setMessage(getString(R.string.waiting_msg));
            mProgressDialog.show();
        }
    }

    protected void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    public void showSuccessMessage(String message) {
        Context context = getContext();
        if (context != null) {
            StyleableToast.makeText(getContext(), message, Toast.LENGTH_LONG, R.style.succeToast).show();
        }
    }

    public void showErrorMessage(String message) {
        Context context = getContext();
        if (context != null) {
            StyleableToast.makeText(getContext(), message, Toast.LENGTH_LONG, R.style.errorToast).show();
        }
    }

    public void showWarningMessage(String message) {
        Context context = getContext();
        if (context != null) {
            StyleableToast.makeText(getContext(), message, Toast.LENGTH_LONG, R.style.warningToast).show();
        }
    }


    protected void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void showDialog(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("Thử lại", (DialogInterface dialogInterface, int i) -> {
                });
        alertDialog.show();
    }

    public void logError(Class t, String method, String message) {
        Log.e(AppConst.DEBUG_TAG, t.getSimpleName() + "." + method + "(): " + message);
    }

    public void logError(String method, String message) {
        Log.e(AppConst.DEBUG_TAG, this.getClass().getSimpleName() + "." + method + "(): " + message);
//        Log.e(AppConst.DEBUG_TAG, "Fragment" + "." + method + "(): " + message);
    }

    public void showErrorUnAuth() {
        showErrorMessage("401 Unauthentication");
    }

    public void showFatalError(ResponseBody errorBody, String method) {
        if (errorBody != null) {
            showErrorMessage("Lỗi server");
            try {
                String error = errorBody.string();
                logError("showFatalError: " + method, error);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            logError("showFatalError: " + method, "errorBody is null");
        }
    }

    public void showBadRequestError(ResponseBody errorBody, String method) {
        if (errorBody != null) {
            try {
                String error = errorBody.string();
                ErrorResponse errorResponse = Utils.parseJson(error, ErrorResponse.class);
                if(errorResponse!=null) {
                    showErrorMessage(errorResponse.getErrorMessage());
                    logError(method, errorResponse.getExceptionMessage());
                }else{
                    showErrorMessage("Lỗi không xác định!");
                    logError("showBadRequestError", error);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

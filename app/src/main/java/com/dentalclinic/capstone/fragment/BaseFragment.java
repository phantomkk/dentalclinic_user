package com.dentalclinic.capstone.fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.utils.AppConst;

public class BaseFragment extends Fragment {
    private ProgressDialog mProgressDialog;

    protected void showLoading() {
        if(mProgressDialog==null) {
            mProgressDialog = new ProgressDialog(getContext());

            mProgressDialog.setMessage(getString(R.string.waiting_msg));
            mProgressDialog.show();
        }
    }

    protected void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    protected void hideLoading() {
        if(mProgressDialog!=null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void logError(Class t, String method, String message) {
        Log.e(AppConst.DEBUG_TAG, t.getSimpleName() + "." + method + "(): " + message);
    }

    public void logError(String method, String message) {
        Log.e(AppConst.DEBUG_TAG, this.getClass().getSimpleName() + "." + method + "(): " + message);
    }
}

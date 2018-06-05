package com.dentalclinic.capstone.fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public class BaseFragment extends Fragment {
    private ProgressDialog mProgressDialog;

    protected void showLoading() {

        mProgressDialog = new ProgressDialog(getContext());

        mProgressDialog.setMessage("Working ...");
        mProgressDialog.show();
    }

    protected void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    protected void closeLoading() {
        mProgressDialog.dismiss();
    }
}

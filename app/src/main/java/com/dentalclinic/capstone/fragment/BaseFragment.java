package com.dentalclinic.capstone.fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    private ProgressDialog mProgressDialog;

    protected void showLoading(){

        mProgressDialog = new ProgressDialog(getContext());

        mProgressDialog.setMessage("Working ...");
        mProgressDialog.show();
    }

    protected void closeLoading() {
        mProgressDialog.dismiss();
    }
}

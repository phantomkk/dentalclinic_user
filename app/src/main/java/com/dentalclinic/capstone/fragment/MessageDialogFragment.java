package com.dentalclinic.capstone.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Lenovo on 12/10/2017.
 */

public class MessageDialogFragment extends DialogFragment {
    public interface MessageDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }


    private String mMessage;
    private MessageDialogListener mListener;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static MessageDialogFragment newInstance(String message, MessageDialogListener listener) {
        MessageDialogFragment fragment = new MessageDialogFragment();
        fragment.mMessage = message;
        fragment.mListener = listener;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setMessage(mMessage);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(mListener != null) {
                    mListener.onDialogPositiveClick(MessageDialogFragment.this);
                }
            }
        });

        return builder.create();
    }
}
package com.dentalclinic.capstone.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dentalclinic.capstone.R;

public class EditAccoutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_accout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));
        }


    }

    @Override
    public String getMainTitle() {
        return getResources().getString(R.string.edit_acc_title);
    }

    @Override
    public void onCancelLoading() {
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

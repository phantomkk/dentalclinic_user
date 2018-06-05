package com.dentalclinic.capstone.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.adapter.DentistSelectionAdapter;
import com.dentalclinic.capstone.models.Staff;

import java.util.ArrayList;
import java.util.List;

public class SelectDentistActivity extends AppCompatActivity {
    private DentistSelectionAdapter suitableDentistAdapter;
    private RecyclerView rcvSuitableDentist;
    private List<Staff> list = new ArrayList<>();


    private DentistSelectionAdapter suggestDentistAdapter;
    private RecyclerView rcvSuggestDentist;
    private List<Staff> list2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dentist);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        rcvSuitableDentist = findViewById(R.id.rcv_dentist_sltdentist);
        suitableDentistAdapter = new DentistSelectionAdapter(list);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager layoutManager2 =
                new LinearLayoutManager(getApplicationContext());
        rcvSuitableDentist.setLayoutManager(layoutManager);
        rcvSuitableDentist.setItemAnimator(new DefaultItemAnimator());
        rcvSuitableDentist.setAdapter(suitableDentistAdapter);

        rcvSuggestDentist = findViewById(R.id.rcv_suggest_dentist_sltdentist);
        suggestDentistAdapter = new DentistSelectionAdapter(list2);
        rcvSuggestDentist.setLayoutManager(layoutManager2);
        rcvSuggestDentist.setItemAnimator(new DefaultItemAnimator());
        rcvSuggestDentist.setAdapter(suggestDentistAdapter);
        prepareData();

    }

    public void prepareData() {
//        for(int i =0; i < 20; i++){
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(new Staff("Hoàng Tuấn Anh "));
        list.add(new Staff("Nguyễn Văn Đẩu "));
        list.add(new Staff("Phạm Thanh Hà"));
        list.add(new Staff("Lê Văn Sơn"));
        list.add(new Staff("Lâm Hoài Phương"));
        suitableDentistAdapter.notifyDataSetChanged();


        if (list == null) {
            list = new ArrayList<>();
        }
        list2.add(new Staff("Nguyễn Hữu Sơn "));
        list2.add(new Staff("Phạm Minh Tuấn "));
        list2.add(new Staff("Nguyễn Thị Thu Phương"));
        suggestDentistAdapter.notifyDataSetChanged();


    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}


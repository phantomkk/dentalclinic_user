package com.dentalclinic.capstone.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.adapter.HistoryPageAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    public HistoryFragment() {
    }
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        tabLayout = v.findViewById(R.id.sliding_tabs);
        viewPager = v.findViewById(R.id.view_pager);
//        HistoryPageAdapter adapter = new HistoryPageAdapter(getChildFragmentManager());
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        HistoryPageAdapter adapter = new HistoryPageAdapter(getChildFragmentManager());
        adapter.addFragment(new HistoryTreatmentFragment(), getResources().getString(R.string.history_treatment_fragment_title));
        adapter.addFragment(new HistoryPaymentFragment(), getResources().getString(R.string.history_payment_fragment_title));
        adapter.addFragment(new HistoryAppointmentFragment(), getResources().getString(R.string.history_appointment_title));
        viewPager.setAdapter(adapter);
    }


}

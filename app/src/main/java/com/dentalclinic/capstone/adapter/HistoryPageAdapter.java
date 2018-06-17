package com.dentalclinic.capstone.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.fragment.NewsPageViewFragment;

import java.util.ArrayList;
import java.util.List;

public class HistoryPageAdapter  extends FragmentPagerAdapter {
    public HistoryPageAdapter(FragmentManager fm) {
        super(fm);
    }

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

}

package com.dentalclinic.capstone.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.activities.NewsActivity;
import com.dentalclinic.capstone.activities.NewsDetailActivity;
import com.dentalclinic.capstone.adapter.NewsAdapter;
import com.dentalclinic.capstone.adapter.NewsPageAdapter;
import com.dentalclinic.capstone.adapter.SmallNewsAdapter;
import com.dentalclinic.capstone.adapter.TreatmentDetailAdapter;
import com.dentalclinic.capstone.animation.EndlessRecyclerOnScrollListener;
import com.dentalclinic.capstone.animation.MyGridView;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.services.NewsService;
import com.dentalclinic.capstone.api.services.TreatmentCategoryService;
import com.dentalclinic.capstone.models.News;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.OnLoadMoreListener;
import com.dentalclinic.capstone.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class NewsFragment extends BaseFragment {
    public NewsFragment() {
    }
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        tabLayout = v.findViewById(R.id.sliding_tabs);
        viewPager = v.findViewById(R.id.view_pager);
        NewsPageAdapter adapter = new NewsPageAdapter(getContext(),getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }

}

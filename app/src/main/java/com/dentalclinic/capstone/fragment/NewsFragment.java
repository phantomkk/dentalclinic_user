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
import com.dentalclinic.capstone.adapter.PageAdapter;
import com.dentalclinic.capstone.adapter.SmallNewsAdapter;
import com.dentalclinic.capstone.adapter.TreatmentDetailAdapter;
import com.dentalclinic.capstone.animation.EndlessRecyclerOnScrollListener;
import com.dentalclinic.capstone.animation.MyGridView;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.CombineClass;
import com.dentalclinic.capstone.api.responseobject.ErrorResponse;
import com.dentalclinic.capstone.api.services.NewsService;
import com.dentalclinic.capstone.api.services.TreatmentCategoryService;
import com.dentalclinic.capstone.models.News;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.OnLoadMoreListener;
import com.dentalclinic.capstone.utils.Utils;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class NewsFragment extends BaseFragment {
    public NewsFragment() {
    }

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public NewsPageViewFragment newsPageViewFragment;
    public NewsPageViewFragment promotionPageViewFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        tabLayout = v.findViewById(R.id.sliding_tabs);
        viewPager = v.findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callAPI();
    }

    PageAdapter adapter;
    private void setupViewPager(ViewPager viewPager) {
        adapter = new PageAdapter(getChildFragmentManager());
        newsPageViewFragment = new NewsPageViewFragment();
        promotionPageViewFragment = new NewsPageViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", 1);
        newsPageViewFragment.setArguments(bundle);
        adapter.addFragment(newsPageViewFragment, getResources().getString(R.string.news_tab_title));
        Bundle bundle2 = new Bundle();
        bundle2.putInt("TYPE", 2);
        promotionPageViewFragment.setArguments(bundle2);
        adapter.addFragment(promotionPageViewFragment, getResources().getString(R.string.promotion_tab_titile));
        viewPager.setAdapter(adapter);
    }

    private Disposable disposable;
    private static int num =0;
    private void callAPI() {
        showLoading();
        logError("callAPI", num++ +"" );
        Single observable1 = APIServiceManager.getService(NewsService.class)
                .loadMoreByType(0, 3, 1).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Single observable2 = APIServiceManager.getService(NewsService.class)
                .loadMoreByType(0, 3, 2).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Single<CombineClass> combined = Single.zip(observable1, observable2,
                new BiFunction<Response<List<News>>, Response<List<News>>, CombineClass>() {
                    @Override
                    public CombineClass apply(Response<List<News>> listResponseNews, Response<List<News>> listResponsePromotion) throws Exception {
                        return new CombineClass(listResponseNews, listResponsePromotion);
                    }
                });
        combined.subscribe(new SingleObserver<CombineClass>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(CombineClass combineClass) {
                if (combineClass.getListNews() != null) {
                    if (combineClass.getListNews().isSuccessful()) {
                        newsPageViewFragment.notificationAdapter(combineClass.getListNews().body());
                    } else {
                        showErrorMessage(getResources().getString(R.string.error_message_api));
                    }
                }
                if (combineClass.getListPromotion() != null) {
                    if (combineClass.getListPromotion().isSuccessful()) {
                        promotionPageViewFragment.notificationAdapter(combineClass.getListPromotion().body());
                    } else {
                        showErrorMessage(getResources().getString(R.string.error_message_api));
                    }
                }
                hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                hideLoading();
                e.printStackTrace();
                showWarningMessage(getResources().getString(R.string.error_on_error_when_call_api));
//                Toast.makeText(getContext(), ), Toast.LENGTH_SHORT).show();
            }
        });


    }

//    class CombineClass {
//        private Response<List<News>> listNews;
//        private Response<List<News>> listPromotion;
//
//        public CombineClass(Response<List<News>> listNews, Response<List<News>> listPromotion) {
//            this.listNews = listNews;
//            this.listPromotion = listPromotion;
//        }
//
//        public Response<List<News>> getListNews() {
//            return listNews;
//        }
//
//        public void setListNews(Response<List<News>> listNews) {
//            this.listNews = listNews;
//        }
//
//        public Response<List<News>> getListPromotion() {
//            return listPromotion;
//        }
//
//        public void setListPromotion(Response<List<News>> listPromotion) {
//            this.listPromotion = listPromotion;
//        }
//    }
}

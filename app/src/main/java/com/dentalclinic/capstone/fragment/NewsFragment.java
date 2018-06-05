package com.dentalclinic.capstone.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.adapter.NewsAdapter;
import com.dentalclinic.capstone.models.News;
import com.dentalclinic.capstone.utils.AppConst;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    List<News> listNews = new ArrayList<>();
    private NewsAdapter adapter;
    private RecyclerView rcvNews;



    public NewsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(AppConst.DEBUG_TAG, "onCreateView");

        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new NewsAdapter(listNews);
        rcvNews = view.findViewById(R.id.rcv_news);
        rcvNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvNews.setItemAnimator(new DefaultItemAnimator());
        rcvNews.setAdapter(adapter);
        preparedData();
        Log.d(AppConst.DEBUG_TAG, "onViewCreated");

    }

    public void preparedData(){
        News news;
        for(int i =0; i < 30; i++) {
            news = new News();
            news.setContent("CLLLLLLLLLLLLLLLLLLLLLLLLLLLLLCLLCLCLCLCLCLLCLCL");
            news.setId(i);
            news.setImgUrl("https://baomoi-photo-1-td.zadn.vn/w700_r1/17/12/18/25/24336735/1_586031.png");
            news.setTitle("A powerful image downloading and caching library for Android");
            listNews.add(news);
        }
        adapter.notifyDataSetChanged();
    }

    public interface OnFragmentInteractionListener {
        void OnInteraction();
    }

}

package com.dentalclinic.capstone.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.models.News;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SmallNewsAdapter extends RecyclerView.Adapter<SmallNewsAdapter.ViewHolder>{
    List<News> list;

    public SmallNewsAdapter(List<News> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SmallNewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_small_news, parent, false);
        return new SmallNewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmallNewsAdapter.ViewHolder holder, int position) {
        News news = list.get(position);
//        Picasso.get().load(news.getImgUrl()).resize(1200, 600).centerCrop().into(holder.imgNews);
        holder.txtTitle.setText(news.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView imgNews;
        TextView txtTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title_news);
//            imgNews = itemView.findViewById(R.id.img_news);
        }
    }
}

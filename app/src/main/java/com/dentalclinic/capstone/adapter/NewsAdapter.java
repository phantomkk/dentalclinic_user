package com.dentalclinic.capstone.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.models.News;
import com.dentalclinic.capstone.utils.OnLoadMoreListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<News> news;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private Activity activity;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public interface OnItemClickListener {
        void onItemClick(News item);
    }

    public NewsAdapter(List<News> news, OnItemClickListener listener) {
        this.news = news;
        this.listener = listener;
    }

    private OnItemClickListener listener;

    public NewsAdapter(RecyclerView recyclerView, List<News> news, Activity activity, OnItemClickListener listener) {
        this.news = news;
        this.activity = activity;
        this.listener = listener;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return news.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
//        return new ViewHolder(view);

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_news, parent, false);
            return new ListViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        holder.bind(news.get(position), listener);
        if (holder instanceof ListViewHolder) {
//            Contact contact = contacts.get(position);
//            UserViewHolder userViewHolder = (UserViewHolder) holder;
//            userViewHolder.phone.setText(contact.getEmail());
//            userViewHolder.email.setText(contact.getPhone());
            ListViewHolder viewHolder = (ListViewHolder) holder;
            viewHolder.bind(news.get(position), listener);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

//    @Override
//    public int getItemCount() {
//        return news.size();
//    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgNews;
        TextView txtTitle;

        public ListViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title_news);
            imgNews = itemView.findViewById(R.id.img_news);
        }

        public void bind(final News item, final OnItemClickListener listener) {
            txtTitle.setText(item.getTitle());
            Picasso.get().load(item.getImgUrl()).resize(1200, 600).centerCrop().into(imgNews);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return news == null ? 0 : news.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }
}

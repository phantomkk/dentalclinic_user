package com.dentalclinic.capstone.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.models.News;
import com.dentalclinic.capstone.utils.PicassoImageGetter;
import com.squareup.picasso.Picasso;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;

public class NewsActivity extends BaseActivity {
    TextView txtContent, txtTitile, txtAuthor, txtCreateDate;
//    ImageView imgHeader;
    News news;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        News news = (News) intent.getSerializableExtra("news");

        txtContent = findViewById(R.id.txt_news_content);
        txtTitile = findViewById(R.id.txt_title);
        txtCreateDate = findViewById(R.id.txt_date_create);
        txtAuthor = findViewById(R.id.txt_author);
        if(news!=null){
            setTitle(news.getTitle());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                txtContent.setMovementMethod(new LinkMovementMethod());
                txtContent.setText(Html.fromHtml(news.getContent(),
                        new PicassoImageGetter(txtContent), null));
            } else {
                txtContent.setText(Html.fromHtml(news.getContent()));
            }
            if(news.getTitle()!=null){
                txtTitile.setVisibility(View.VISIBLE);
                txtTitile.setText(news.getTitle());
            }
            if(news.getCreateDate()!=null){
                txtCreateDate.setVisibility(View.VISIBLE);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                txtCreateDate.setText(dateFormat.format(news.getCreateDate()));
            }
            if(news.getAuthor()!=null){
                txtAuthor.setVisibility(View.VISIBLE);
                txtAuthor.setText(news.getAuthor().getName());
            }

//            txtContent.setText(news.getContent());
//            Picasso.get().load(news.getImgUrl()).into(imgHeader);
        }
    }

    @Override
    public String getMainTitle() {
        return null;
    }

    @Override
    public void onCancelLoading() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}

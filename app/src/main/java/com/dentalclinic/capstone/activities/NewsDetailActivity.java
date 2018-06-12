package com.dentalclinic.capstone.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.models.News;
import com.dentalclinic.capstone.utils.PicassoImageGetter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;

public class NewsDetailActivity extends BaseActivity {
    TextView txtContent, txtAuthor, txtCreateDate;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        News news = (News) intent.getSerializableExtra("news");
        imageView = findViewById(R.id.img_header_news);
        txtContent = findViewById(R.id.txt_news_content);
        txtCreateDate = findViewById(R.id.txt_date_create);
        txtAuthor = findViewById(R.id.txt_author);
        if(news!=null){
            if(news.getNewsImage()!=null){
                Picasso.get().load(news.getNewsImage()).into(imageView);
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                txtContent.setMovementMethod(new LinkMovementMethod());
                txtContent.setText(Html.fromHtml(news.getContent(),
                        new PicassoImageGetter(txtContent), null));
            } else {
                txtContent.setText(Html.fromHtml(news.getContent()));
            }
            if(news.getTitle()!=null){
                setTitle(news.getTitle());
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
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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

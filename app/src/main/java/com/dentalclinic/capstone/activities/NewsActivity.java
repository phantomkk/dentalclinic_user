package com.dentalclinic.capstone.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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

public class NewsActivity extends BaseActivity {
    TextView txtContent;
    ImageView imgHeader;
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
        imgHeader = findViewById(R.id.img_image_news);
        if(news!=null){
            setTitle(news.getTitle());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                txtContent.setText(Html.fromHtml(news.getContent(),Html.FROM_HTML_MODE_LEGACY));
                txtContent.setMovementMethod(new LinkMovementMethod());
                txtContent.setText(Html.fromHtml(news.getContent(),
                        new PicassoImageGetter(txtContent), null));
            } else {
                txtContent.setText(Html.fromHtml(news.getContent()));
            }
//            txtContent.setText(news.getContent());
            Picasso.get().load(news.getImgUrl()).into(imgHeader);
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

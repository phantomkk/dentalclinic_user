package com.dentalclinic.capstone.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.models.TreatmentImage;
import com.dentalclinic.capstone.utils.AppConst;
//import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;

//import uk.co.senab.photoview.PhotoView;


public class PhotoViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.side_nav_bar));
        }
        Bundle bundle = getIntent().getBundleExtra(AppConst.BUNDLE);
        TreatmentImage image = (TreatmentImage) bundle.getSerializable(AppConst.IMAGE_OBJ);
        if(image!=null){
            PhotoView photoView = findViewById(R.id.iv_photo);
            Picasso.get().load(image.getImageLink()).into(photoView);
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

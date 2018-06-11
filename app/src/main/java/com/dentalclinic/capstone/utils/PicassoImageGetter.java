package com.dentalclinic.capstone.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.dentalclinic.capstone.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
public class PicassoImageGetter implements Html.ImageGetter {
    private final TextView mTextView;

    /**
     * Construct an instance of {@link android.text.Html.ImageGetter}
     * @param view      {@link android.widget.TextView} that holds HTML which contains $lt;img&gt; tag to load
     */
    public PicassoImageGetter(TextView view) {
        mTextView = view;
    }

    @Override
    public Drawable getDrawable(String source) {
        if (TextUtils.isEmpty(source)) {
            return null;
        }
        final Uri uri = Uri.parse(source);
        if (uri.isRelative()) {
            return null;
        }
        final URLDrawable urlDrawable = new URLDrawable(mTextView.getResources(), null);
        new LoadFromUriAsyncTask(mTextView, urlDrawable).execute(uri);
        return urlDrawable;
    }
}

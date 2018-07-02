package com.dentalclinic.capstone.activities;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.responseobject.FeedbackResponse;
import com.dentalclinic.capstone.api.services.FeedbackService;
import com.dentalclinic.capstone.models.Feedback;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.DateTimeFormat;
import com.dentalclinic.capstone.utils.DateUtils;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FeedbackActivity extends Activity {

    Dialog dialog = null;

    private TextView txtName;
    private CircleImageView imgAvatar;
    private TextView txtTreatmentContent;
    private RatingBar ratingBar;
    private AutoCompleteTextView contentFeedback;
    private Button btnOK;
    private Button btnCancel;
    private Disposable apiDisposable;
    private FeedbackResponse response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  // Make us non-modal, so that others can receive touch events.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        // ...but notify us that it happened.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_feedback);
        setTitle("Đánh giá dịch vụ");
        response = (FeedbackResponse) getIntent().getExtras().get(AppConst.TREATMENT_DETAIL_BUNDLE);
        initDialog(response);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
//            finish();
            return false;
        }

        // Delegate everything else to Activity.
        return super.onTouchEvent(event);
    }

    protected void initDialog(FeedbackResponse response) {
//        dialog = new Dialog(this);
//        dialog.setContentView(R.layout.dialog_feedback);
        txtName = findViewById(R.id.txt_dentist_name);
        imgAvatar = findViewById(R.id.img_dentist_avatar);
        txtTreatmentContent = findViewById(R.id.treatment_content_feedback);
        ratingBar = findViewById(R.id.ratingBar);

        contentFeedback = findViewById(R.id.edit_content);
        btnOK = findViewById(R.id.btn_submit_feedback);
        btnCancel = findViewById(R.id.btn_cancel_feedback);
        btnOK.setOnClickListener((View view) -> {
            attemptFeedback();
        });
        btnCancel.setOnClickListener((View view) -> {
            finish();
        });
        if (response != null) {
            txtName.setText(response.getDentistName());
            String result = response.getTreatmentName();
            String dateInAppFormat = DateUtils.changeDateFormat(response.getCreatedDate(), DateTimeFormat.DATE_TIME_DB_2,
                    DateTimeFormat.DATE_APP);
            dateInAppFormat = dateInAppFormat == null ? "" : dateInAppFormat;
            result += " " + dateInAppFormat;
            if (!result.isEmpty()) {
                txtTreatmentContent.setText(result);
            }
            Picasso.get().load(response.getDentistAvatar()).into(imgAvatar);
        }

    }

    public void attemptFeedback() {
        float rating = ratingBar.getRating();
        String content = contentFeedback.getText().toString().trim();
        int patientId = response.getPatientId();
        int treatmentDetailID = response.getTreatmentDetailId();
        String dateCreated = DateUtils.getCurrentDate(DateTimeFormat.DATE_TIME_DB);
        if (rating == 0) {
            Toast.makeText(FeedbackActivity.this, "Vui lòng chọn sao", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Feedback fb = new Feedback();
            fb.setContent(content);
            fb.setDateFeedback(dateCreated);
            fb.setPatientId(patientId);
            fb.setTreatmentDetailId(treatmentDetailID);
            fb.setNumOfStars(rating);

            FeedbackService feedbackService = APIServiceManager.getService(FeedbackService.class);
            feedbackService.createFeedback(fb).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Response<String>>() {
                                   @Override
                                   public void onSubscribe(Disposable d) {
                                       apiDisposable = d;
                                   }

                                   @Override
                                   public void onSuccess(Response<String> stringResponse) {
                                       if (stringResponse.isSuccessful()) {
                                           String response = stringResponse.body();
                                           Toast.makeText(FeedbackActivity.this, response, Toast.LENGTH_SHORT).show();
                                           finish();
                                       } else if (stringResponse.code() == 500) {

                                           Toast.makeText(FeedbackActivity.this,
                                                   "Lỗi server",
                                                   Toast.LENGTH_SHORT).show();
                                       } else {
                                           Toast.makeText(FeedbackActivity.this,
                                                   "Có lỗi xảy ra",
                                                   Toast.LENGTH_SHORT).show();

                                       }
                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       e.printStackTrace();
                                       Toast.makeText(FeedbackActivity.this,
                                               "Không thể kết nối đến server",
                                               Toast.LENGTH_SHORT).show();
                                   }
                               }
                    );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (apiDisposable != null) {
            apiDisposable.dispose();
        }
    }
}

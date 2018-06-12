package com.dentalclinic.capstone.activities;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.adapter.TreatmentDetailAdapter;
import com.dentalclinic.capstone.models.Medicine;
import com.dentalclinic.capstone.models.Prescription;
import com.dentalclinic.capstone.models.Staff;
import com.dentalclinic.capstone.models.Treatment;
import com.dentalclinic.capstone.models.TreatmentDetail;
import com.dentalclinic.capstone.models.TreatmentDetailStep;
import com.dentalclinic.capstone.models.TreatmentHistory;
import com.dentalclinic.capstone.models.TreatmentImage;
import com.dentalclinic.capstone.models.TreatmentStep;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDetailActivity extends BaseActivity {
    ListView listView;
    TreatmentHistory treatmentHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.side_nav_bar));
        prepareData();
        listView = findViewById(R.id.lv_treatment_detail);
        TreatmentDetailAdapter adapter = new TreatmentDetailAdapter(this, treatmentHistory.getTreatmentDetails());
        listView.setAdapter(adapter);

    }

    @Override
    public String getMainTitle() {
        return getResources().getString(R.string.treatment_detail_title_activity);
    }

    @Override
    public void onCancelLoading() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void prepareData() {
        treatmentHistory = new TreatmentHistory();
        List<TreatmentDetail> details = new ArrayList<>();
        TreatmentDetail detail = new TreatmentDetail();
        //note
        detail.setNote("hahaha");
        List<TreatmentImage> treatmentImages = new ArrayList<>();
        treatmentImages.add(new TreatmentImage("http://nhakhoalouis.com.vn/wp-content/uploads/2016/10/chup-xquang-rang-1.jpg"));
        treatmentImages.add(new TreatmentImage("http://nhakhoalouis.com.vn/wp-content/uploads/2016/10/chup-xquang-rang-1.jpg"));
        treatmentImages.add(new TreatmentImage("http://nhakhoalouis.com.vn/wp-content/uploads/2016/10/chup-xquang-rang-1.jpg"));
        treatmentImages.add(new TreatmentImage("http://nhakhoalouis.com.vn/wp-content/uploads/2016/10/chup-xquang-rang-1.jpg"));
        treatmentImages.add(new TreatmentImage("http://nhakhoalouis.com.vn/wp-content/uploads/2016/10/chup-xquang-rang-1.jpg"));
        treatmentImages.add(new TreatmentImage("http://nhakhoalouis.com.vn/wp-content/uploads/2016/10/chup-xquang-rang-1.jpg"));
        //list image
        detail.setTreatmentImages(treatmentImages);
        //dentist name
        detail.setDentist(new Staff("Huynh Vo Thien Phuc"));
        String dtStart = "30-06-1996";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            //date
            detail.setCreatedDate(format.parse(dtStart));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<TreatmentDetailStep> detailSteps = new ArrayList<>();
        TreatmentDetailStep detailStep = new TreatmentDetailStep();
        detailStep.setTreatmentStep(new TreatmentStep("nao voi rang"));
        TreatmentDetailStep detailStep2 = new TreatmentDetailStep();
        detailStep2.setTreatmentStep(new TreatmentStep("trám răng"));
        detailSteps.add(detailStep);
        detailSteps.add(detailStep2);
        //step
        detail.setTreatmentDetailSteps(detailSteps);

        List<Prescription> prescriptions = new ArrayList<>();
        Prescription prescription = new Prescription(new Medicine("giam dau"));
        prescription.setQualtity(30);
        Prescription prescription2 = new Prescription(new Medicine("chong sung"));
        prescription2.setQualtity(40);
        Prescription prescription3 = new Prescription(new Medicine("aprical analink 500gam"));
        prescription3.setQualtity(40);
        prescriptions.add(prescription);
        prescriptions.add(prescription2);
        prescriptions.add(prescription3);
        detail.setPrescriptions(prescriptions);


        details.add(detail);
        details.add(detail);
        details.add(detail);
        treatmentHistory.setTreatmentDetails(details);
    }
}

package com.dentalclinic.capstone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.animation.MyGridView;
import com.dentalclinic.capstone.models.Prescription;
import com.dentalclinic.capstone.models.TreatmentDetail;
import com.dentalclinic.capstone.models.TreatmentDetailStep;
import com.dentalclinic.capstone.models.TreatmentImage;
import com.dentalclinic.capstone.models.TreatmentStep;
import com.dentalclinic.capstone.utils.Utils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDetailAdapter extends ArrayAdapter<TreatmentDetail> {
    private List<TreatmentDetail> treatmentDetails;

    public TreatmentDetailAdapter(Context context, List<TreatmentDetail> treatmentDetails) {
        super(context, 0, treatmentDetails);
        this.treatmentDetails = treatmentDetails;
    }

    private static class ViewHolder {
        TextView mDentistName, mDate, mTreatmentStep, mNote, mPrescription;
        MyGridView gridViewListimage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = convertView;
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_treatment_detail, parent, false);
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image, parent, false);
            viewHolder.mDentistName = convertView.findViewById(R.id.txt_dentist);
            viewHolder.mDate = convertView.findViewById(R.id.txt_date);
            viewHolder.mTreatmentStep = convertView.findViewById(R.id.txt_treatment_step);
            viewHolder.mNote = convertView.findViewById(R.id.txt_note);
            viewHolder.mPrescription = convertView.findViewById(R.id.txt_medicine);
            viewHolder.gridViewListimage = convertView.findViewById(R.id.gv_list_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TreatmentDetail treatmentDetail = treatmentDetails.get(position);
        if (treatmentDetail != null) {
//            ImageView iv = (ImageView) convertView.findViewById(R.id.img_image_treatment);
//            Picasso.get().load(image.getImageLink()).into(viewHolder.imageView);
            viewHolder.mDentistName.setText(treatmentDetail.getDentist().getName());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            viewHolder.mDate.setText(dateFormat.format(treatmentDetail.getCreatedDate()));
            String step = "";
            for (TreatmentDetailStep stepObj : treatmentDetail.getTreatmentDetailSteps()) {
                step += stepObj.getTreatmentStep().getName() + "\n";
            }
            viewHolder.mTreatmentStep.setText(step);
            viewHolder.mNote.setText(treatmentDetail.getNote());
            String prescription = "";
            for (Prescription prescriptionObj : treatmentDetail.getPrescriptions()) {
                prescription += Utils.getMedicineLine(prescriptionObj.getMedicine().getName(), prescriptionObj.getQualtity(), 50) + "\n";
            }
            viewHolder.mPrescription.setText(prescription);
            ImageAdapter imageAdapter = new ImageAdapter(getContext(), treatmentDetail.getTreatmentImages());
            viewHolder.gridViewListimage.setAdapter(imageAdapter);
        }
        return convertView;
    }

}

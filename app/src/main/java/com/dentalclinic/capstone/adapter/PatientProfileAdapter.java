package com.dentalclinic.capstone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.TreatmentImage;

import java.util.List;

public class PatientProfileAdapter  extends ArrayAdapter<Patient> {
     private List<Patient> patients;

    public PatientProfileAdapter(Context context, List<Patient> patients) {
        super(context, 0, patients);
        this.patients = patients;
    }

    private static class ViewHolder {
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = convertView;
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_patient_profile, parent, false);
//            viewHolder.imageView = convertView.findViewById(R.id.img_image_treatment);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        TreatmentImage image = treatmentImages.get(position);
//        if (image != null) {
////            ImageView iv = (ImageView) convertView.findViewById(R.id.img_image_treatment);
//                Picasso.get().load(image.getImageLink()).into(viewHolder.imageView);
//        }
        return convertView;
    }
}

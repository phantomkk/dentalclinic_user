package com.dentalclinic.capstone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.models.Treatment;
import com.dentalclinic.capstone.models.TreatmentCategory;
import com.dentalclinic.capstone.models.TreatmentDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ServiceAdapter extends ArrayAdapter<TreatmentCategory> {
    private List<TreatmentCategory> treatmentCategories;

    public ServiceAdapter(Context context, List<TreatmentCategory> treatmentCategories) {
        super(context, 0, treatmentCategories);
        this.treatmentCategories = treatmentCategories;
    }

    private static class ViewHolder {
//        TextView mDentistName, mDate, mTreatmentStep, mNote, mPrescription;
//        MyGridView gridViewListimage;
        CircleImageView mIcon;
        TextView mName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = convertView;
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_service, parent, false);
            viewHolder.mIcon = convertView.findViewById(R.id.img_icon_service);
            viewHolder.mName = convertView.findViewById(R.id.txt_service_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TreatmentCategory treatmentCategory = treatmentCategories.get(position);
        if (treatmentCategory != null) {
//            ImageView iv = (ImageView) convertView.findViewById(R.id.img_image_treatment);
            Picasso.get().load(treatmentCategory.getIconLink()).into(viewHolder.mIcon);
            viewHolder.mName.setText(treatmentCategory.getName());
        }
        return convertView;
    }
}

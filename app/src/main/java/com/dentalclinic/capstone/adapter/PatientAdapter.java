package com.dentalclinic.capstone.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.User;


import java.util.List;

/**
 * Created by lucky on 07-Oct-17.
 */

public class PatientAdapter extends ArrayAdapter<Patient> {
    private int resourceID;
    private int lastPosition = -1;
    public PatientAdapter(@NonNull Context context, @LayoutRes int resourceID, @NonNull List<Patient> list) {
        super(context, resourceID, list);
        this.resourceID = resourceID;
    }

    private static class ViewHolder{
        TextView txtProductCode;
        TextView txtProductName;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        Patient patient = getItem(position);
        ViewHolder viewHolder;
        final View result;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(resourceID, parent, false);
            viewHolder.txtProductCode = (TextView)convertView.findViewById(R.id.txt_code_item_history);
            viewHolder.txtProductName = (TextView) convertView.findViewById(R.id.txt_name_item_history);
            result = convertView;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
//              Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
//        lastPosition = position;

        viewHolder.txtProductName.setText(patient.getName());
        viewHolder.txtProductCode.setText(patient.getAddress());
        return convertView;
    }
}

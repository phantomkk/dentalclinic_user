package com.dentalclinic.capstone.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.animation.AnimatedExpandableListView;
import com.dentalclinic.capstone.models.City;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.TreatmentDetail;
import com.dentalclinic.capstone.models.TreatmentHistory;
import com.dentalclinic.capstone.models.TreatmentImage;
import com.dentalclinic.capstone.models.User;
import com.dentalclinic.capstone.utils.Utils;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lucky on 07-Oct-17.
 */

public class PatientAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter{
    private Context context;
    private List<Patient> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Patient, List<TreatmentDetail>> _listDataChild;
//
//    private int resourceID;
//    private int lastPosition = -1;
//    public PatientAdapter(@NonNull Context context, @LayoutRes int resourceID, @NonNull List<Patient> list) {
//        super(context, resourceID, list);
//        this.resourceID = resourceID;
//    }

    public PatientAdapter(Context context, List<Patient> listDataHeader,
                                 HashMap<Patient, List<TreatmentDetail>> listChildData) {
        this.context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }



//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
////        return super.getView(position, convertView, parent);
//        Patient patient = getItem(position);
//        ViewHolder viewHolder;
//        final View result;
//        if(convertView == null){
//            viewHolder = new ViewHolder();
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(resourceID, parent, false);
//            viewHolder.txtProductCode = (TextView)convertView.findViewById(R.id.txt_code_item_history);
//            viewHolder.txtProductName = (TextView) convertView.findViewById(R.id.txt_name_item_history);
//            result = convertView;
//            convertView.setTag(viewHolder);
//        }else{
//            viewHolder = (ViewHolder) convertView.getTag();
//            result = convertView;
//        }
////              Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
////        result.startAnimation(animation);
////        lastPosition = position;
//
//        viewHolder.txtProductName.setText(patient.getName());
//        viewHolder.txtProductCode.setText(patient.getAddress());
//        return convertView;
//    }





    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TreatmentDetail treatmentDetail = (TreatmentDetail) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_treatment, null);
        }

        TextView txtTreatment = (TextView) convertView.findViewById(R.id.txt_treatment_name);
        txtTreatment.setText(treatmentDetail.getNote());
        GridView gridView = convertView.findViewById(R.id.gv_list_image);
        List<TreatmentImage> treatmentImages = new ArrayList<>();
        treatmentImages.add(new TreatmentImage("http://nhakhoalouis.com.vn/wp-content/uploads/2016/10/chup-xquang-rang-1.jpg"));
        treatmentImages.add(new TreatmentImage("http://nhakhoalouis.com.vn/wp-content/uploads/2016/10/chup-xquang-rang-1.jpg"));
        treatmentImages.add(new TreatmentImage("http://nhakhoalouis.com.vn/wp-content/uploads/2016/10/chup-xquang-rang-1.jpg"));
        treatmentImages.add(new TreatmentImage("http://nhakhoalouis.com.vn/wp-content/uploads/2016/10/chup-xquang-rang-1.jpg"));
        treatmentImages.add(new TreatmentImage("http://nhakhoalouis.com.vn/wp-content/uploads/2016/10/chup-xquang-rang-1.jpg"));
        treatmentImages.add(new TreatmentImage("http://nhakhoalouis.com.vn/wp-content/uploads/2016/10/chup-xquang-rang-1.jpg"));
        treatmentDetail.setTreatmentImages(treatmentImages);
        ImageAdapter imageAdapter = new ImageAdapter(context,treatmentDetail.getTreatmentImages());
        gridView.setAdapter(imageAdapter);
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

//    @Override
//    public View getChildView(int groupPosition, final int childPosition,
//                             boolean isLastChild, View convertView, ViewGroup parent) {
//
//        final City child = (City) getChild(groupPosition, childPosition);
//
//        if (convertView == null) {
//            LayoutInflater infalInflater = (LayoutInflater) this.context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = infalInflater.inflate(R.layout.item_treatment, null);
//        }
//
//        TextView txtListChild = (TextView) convertView
//                .findViewById(R.id.txt_code_item_history);
//
//        txtListChild.setText(child.getName());
//        return convertView;
//    }

//    @Override
//    public int getChildrenCount(int groupPosition) {
//        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
//                .size();
//    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Patient patient = (Patient) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_patient, null);
        }
        CircleImageView circleImageView = convertView.findViewById(R.id.img_avatar);
        Picasso.get().load(patient.getAvatar()).into(circleImageView);
        ImageView imageView = convertView.findViewById(R.id.img_expand_icon);
        if (isExpanded) {
            imageView.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        } else {
            imageView.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        }
        TextView mTxtName = (TextView) convertView.findViewById(R.id.txt_name);
        mTxtName.setTypeface(null, Typeface.BOLD);
        mTxtName.setText(Utils.upperCaseFirst(patient.getName()));
        TextView mTxtDateOfBirth = (TextView) convertView.findViewById(R.id.txt_date_of_birth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        mTxtDateOfBirth.setText(dateFormat.format(patient.getDateOfBirth()));
        TextView mTxtAddress = (TextView) convertView.findViewById(R.id.txt_address);
        mTxtAddress.setText(patient.getAddress());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}

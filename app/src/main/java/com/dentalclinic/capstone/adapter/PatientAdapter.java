package com.dentalclinic.capstone.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.animation.AnimatedExpandableListView;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.TreatmentHistory;
import com.dentalclinic.capstone.utils.Utils;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lucky on 07-Oct-17.
 */

public class PatientAdapter extends BaseExpandableListAdapter{
    private Context context;
    private List<Patient> listDataHeader; // header titles
    // child data in format of header title, child title
//
//    private int resourceID;
//    private int lastPosition = -1;
//    public PatientAdapter(@NonNull Context context, @LayoutRes int resourceID, @NonNull List<Patient> list) {
//        super(context, resourceID, list);
//        this.resourceID = resourceID;
//    }



    public PatientAdapter(Context context, List<Patient> listDataHeader) {
        this.context = context;
        this.listDataHeader = listDataHeader;
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
        return this.listDataHeader.get(groupPosition).getTreatmentHistories().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
//
//    @Override
//    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        TreatmentHistory treatmentHistory = (TreatmentHistory) getChild(groupPosition, childPosition);
//        if (convertView == null) {
//            LayoutInflater infalInflater = (LayoutInflater) this.context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = infalInflater.inflate(R.layout.item_treatment_history, null);
//        }
//        TextView mTreatmentName = convertView.findViewById(R.id.txt_treatment_name);
//        mTreatmentName.setText(treatmentHistory.getTreatment().getName());
//        TextView mPrice = convertView.findViewById(R.id.txt_price);
//        mPrice.setText(treatmentHistory.getPrice()+"đ");
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        TextView mStartDate = (TextView) convertView.findViewById(R.id.txt_start_date);
//        mStartDate.setText(dateFormat.format(treatmentHistory.getCreateDate()));
//        TextView mFinishDate = (TextView) convertView.findViewById(R.id.txt_finish_date);
//        mFinishDate.setText(dateFormat.format(treatmentHistory.getFinishDate()));
//        TextView mToothName = (TextView) convertView.findViewById(R.id.txt_tooth_name);
//        mToothName.setText(treatmentHistory.getTooth().getToothName());
//        TextView mDiscount = (TextView) convertView.findViewById(R.id.txt_discount);
//        mDiscount.setText(treatmentHistory.getTreatment().getEvent().getDiscount()+"%");
//        TextView mTotal = (TextView) convertView.findViewById(R.id.txt_total);
//        mTotal.setText(treatmentHistory.getTotalPrice()+"đ");
//
//        return convertView;
//    }
//
//    @Override
//    public int getRealChildrenCount(int groupPosition) {
//        return this.listDataHeader.get(groupPosition).getTreatmentHistories().size();
//    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        TreatmentHistory treatmentHistory = (TreatmentHistory) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_treatment_history, null);
        }
        TextView mTreatmentName = convertView.findViewById(R.id.txt_treatment_name);
        mTreatmentName.setText(treatmentHistory.getTreatment().getName());
        TextView mPrice = convertView.findViewById(R.id.txt_price);
        mPrice.setText(treatmentHistory.getPrice()+"đ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        TextView mStartDate = (TextView) convertView.findViewById(R.id.txt_start_date);
        mStartDate.setText(dateFormat.format(treatmentHistory.getCreateDate()));
        TextView mFinishDate = (TextView) convertView.findViewById(R.id.txt_finish_date);
        mFinishDate.setText(dateFormat.format(treatmentHistory.getFinishDate()));
        TextView mToothName = (TextView) convertView.findViewById(R.id.txt_tooth_name);
        mToothName.setText(treatmentHistory.getTooth().getToothName());
        TextView mDiscount = (TextView) convertView.findViewById(R.id.txt_discount);
        mDiscount.setText(treatmentHistory.getTreatment().getEvent().getDiscount()+"%");
        TextView mTotal = (TextView) convertView.findViewById(R.id.txt_total);
        mTotal.setText(treatmentHistory.getTotalPrice()+"đ");

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataHeader.get(groupPosition).getTreatmentHistories().size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        if(listDataHeader!=null) {
            return this.listDataHeader.size();
        }return 0;
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

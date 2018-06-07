package com.dentalclinic.capstone.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.animation.AnimatedExpandableListView;
import com.dentalclinic.capstone.models.Treatment;
import com.dentalclinic.capstone.models.TreatmentCategory;
import com.dentalclinic.capstone.models.TreatmentDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ServiceAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private Context context;
    private List<TreatmentCategory> listDataHeader; // header titles
    private List<TreatmentCategory> listDataHeaderOriginal = new ArrayList<>(); // header titles
    // child data in format of header title, child title
//    private HashMap<TreatmentCategory, List<Treatment>> listDataChild;

    public ServiceAdapter(Context context, List<TreatmentCategory> listDataHeader) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataHeaderOriginal.addAll(listDataHeader);
//        this.listDataChild = listChildData;
    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return listDataHeader.get(groupPosition).getTreatments().get(childPosititon);
//        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
//                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Treatment treatment = (Treatment) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_treatment, null);
        }
        TextView mName = convertView.findViewById(R.id.txt_name);
        mName.setText(treatment.getName());
        TextView mPrice = convertView.findViewById(R.id.txt_price);
        mPrice.setText(treatment.getMinPrice() + "-" + treatment.getMaxPrice() + "VND");
        TextView mDescription = convertView.findViewById(R.id.txt_description);
        if (treatment.getDescription() != null) {
            mDescription.setVisibility(View.VISIBLE);
            mDescription.setText(treatment.getDescription());
        }
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return this.listDataHeader.get(groupPosition).getTreatments().size();
//        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
//                .size();
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
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        TreatmentCategory treatmentCategory = (TreatmentCategory) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_service, null);
        }
        CircleImageView circleImageView = convertView.findViewById(R.id.img_icon_service);
        Picasso.get().load(treatmentCategory.getIconLink()).into(circleImageView);
        ImageView imageView = convertView.findViewById(R.id.img_expand_icon);
        if (isExpanded) {
            imageView.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        } else {
            imageView.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        }
        TextView mTxtName = (TextView) convertView.findViewById(R.id.txt_service_name);
        mTxtName.setText(treatmentCategory.getName());
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


    public void filterData(String query) {

        query = query.toLowerCase();
        Log.v("ServiceAdapter", String.valueOf(listDataHeader.size()));
        Log.v("original", String.valueOf(listDataHeaderOriginal.size()));
        Log.v("query", query);
        listDataHeader.clear();
        if (query.isEmpty()) {
            listDataHeader.addAll(listDataHeaderOriginal);
        } else {
            for (TreatmentCategory treatmentCategory : listDataHeaderOriginal) {
                List<Treatment> treatments = treatmentCategory.getTreatments();
                Log.v("treatments", String.valueOf(treatments.size()));
                List<Treatment> newTreatments = new ArrayList<Treatment>();
                for (Treatment treatment : treatments) {
                    if (treatment.getName().toLowerCase().contains(query.toLowerCase())) {
                        newTreatments.add(treatment);
                    }
                }
                if (newTreatments.size() > 0) {
                    Log.v("newTreatments", String.valueOf(newTreatments.size()));
                    TreatmentCategory category = new TreatmentCategory();
                    if (treatmentCategory.getId() != -1) {
                        category.setId(treatmentCategory.getId());
                    }
                    if (treatmentCategory.getDescription() != null) {
                        category.setDescription(category.getDescription());
                    }
                    if(treatmentCategory.getIconLink()!=null){
                        category.setIconLink(treatmentCategory.getIconLink());
                    }
                    category.setName(category.getName());
                    category.setTreatments(newTreatments);
                    listDataHeader.add(category);
                }
            }
        }

        Log.v("ServiceAdapter", String.valueOf(listDataHeader.size()));
        notifyDataSetChanged();
    }


}

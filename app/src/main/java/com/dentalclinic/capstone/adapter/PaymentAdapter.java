package com.dentalclinic.capstone.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.models.Payment;
import com.dentalclinic.capstone.models.PaymentDetail;
import com.dentalclinic.capstone.models.Staff;
import com.dentalclinic.capstone.models.Treatment;
import com.dentalclinic.capstone.models.TreatmentHistory;
import com.dentalclinic.capstone.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PaymentAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Payment> listDataHeader; // header titles
    private List<Payment> listDataHeaderOriginal = new ArrayList<>();

    public PaymentAdapter(Context context, List<Payment> listDataHeader) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataHeaderOriginal.addAll(listDataHeader);
    }

    public List<Payment> getListDataHeaderOriginal() {
        return listDataHeaderOriginal;
    }

    public void setListDataHeaderOriginal(List<Payment> listDataHeaderOriginal) {
        this.listDataHeaderOriginal = listDataHeaderOriginal;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return listDataHeader.get(groupPosition).getPaymentDetails().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        PaymentDetail paymentDetail = (PaymentDetail) getChild(groupPosition, childPosition);
        ChildViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ChildViewHolder();
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_payment_detail, null);
            TextView txtStaffName = convertView.findViewById(R.id.txt_staff_name_1);
            TextView txtDate = convertView.findViewById(R.id.txt_date);
            TextView txtPrepaidMoney = convertView.findViewById(R.id.txt_prepaid_money);
            ImageView imgStaffAvatar = convertView.findViewById(R.id.img_staff_avatar);

//        else{
//            viewHolder = (ChildViewHolder) convertView.getTag();
//        }


            if (paymentDetail.getReceivedMoney() != null) {
                Staff staff = paymentDetail.getReceptionist();
                if (staff.getName() != null) {
                    txtStaffName.setText(staff.getName());
                }
                if (staff.getAvatar() != null) {
                    Picasso.get().load(staff.getAvatar()).into(imgStaffAvatar);
                }
            }
            if (paymentDetail.getDateCreate() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                txtDate.setText(dateFormat.format(paymentDetail.getDateCreate()));
            }
            if (paymentDetail.getReceivedMoney() != null) {
                txtPrepaidMoney.setText(Utils.formatMoney(paymentDetail.getReceivedMoney()) + context.getResources().getString(R.string.current_unit));
            }
        }
        return convertView;
    }


    public class ChildViewHolder {
        TextView txtStaffName, txtDate, txtPrepaidMoney;
        CircleImageView imgStaffAvatar;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataHeader.get(groupPosition).getPaymentDetails().size();

    }

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
        Payment payment = (Payment) getGroup(groupPosition);
        GroupViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new GroupViewHolder();
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_payment, null);
            viewHolder.txtTreatmentName = convertView.findViewById(R.id.txt_treatment_name);
            viewHolder.txtTotal = convertView.findViewById(R.id.txt_total);
            viewHolder.txtPrepaid = convertView.findViewById(R.id.txt_prepaid);
            viewHolder.txtNotePayable = convertView.findViewById(R.id.txt_not_payable);
            viewHolder.txtStatus = convertView.findViewById(R.id.txt_status);
            viewHolder.imgExpIcon = convertView.findViewById(R.id.img_expand_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        if (isExpanded) {
            viewHolder.imgExpIcon.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        } else {
            viewHolder.imgExpIcon.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        }
        if (payment.getTreatmentHistories() != null) {
            String treatmentName = "";
            for (TreatmentHistory treatmentHistory : payment.getTreatmentHistories()) {
                treatmentName += treatmentHistory.getTreatment().getName() + "\n";
            }
            viewHolder.txtTreatmentName.setText(treatmentName);
        }
        if (payment.getTotalPrice() != null) {
            viewHolder.txtTotal.setText(Utils.formatMoney(payment.getTotalPrice()) + context.getResources().getString(R.string.current_unit));
        }
        if (payment.getPrepaid() != null) {
            viewHolder.txtPrepaid.setText(Utils.formatMoney(payment.getPrepaid()) + context.getResources().getString(R.string.current_unit));
        }
        if (payment.getPrepaid() != null) {
            viewHolder.txtNotePayable.setText(Utils.formatMoney(payment.getNotePayable()) + context.getResources().getString(R.string.current_unit));
        }
        if (payment.isDone()) {
            viewHolder.txtStatus.setText(context.getResources().getString(R.string.status_done));
            viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.color_green_500));
        } else {
            viewHolder.txtStatus.setText(context.getResources().getString(R.string.status_not_done));
            viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.color_red_500));
        }
        return convertView;
    }

    public class GroupViewHolder {
        TextView txtTreatmentName, txtTotal, txtPrepaid, txtNotePayable, txtStatus;
        ImageView imgExpIcon;
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
            for (Payment payment : listDataHeaderOriginal) {
                List<PaymentDetail> paymentDetails = payment.getPaymentDetails();
                Log.v("paymentDetails", String.valueOf(paymentDetails.size()));
                List<PaymentDetail> newPaymentDetails = new ArrayList<PaymentDetail>();
                for (PaymentDetail paymentDetail : paymentDetails) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String date = dateFormat.format(paymentDetail.getDateCreate());
                    if (date.contains(query.toLowerCase())) {
                        newPaymentDetails.add(paymentDetail);
                    }
                }
                if (newPaymentDetails.size() > 0) {
                    Log.v("newPaymentDetails", String.valueOf(newPaymentDetails.size()));
                    Payment tPayment = new Payment();
                    if (payment.getId() != -1) {
                        tPayment.setId(payment.getId());
                    }
                    if (payment.getUser() != null) {
                        tPayment.setUser(payment.getUser());
                    }
                    if (payment.getPrepaid() != null) {
                        tPayment.setPrepaid(payment.getPrepaid());
                    }
                    if (payment.getTotalPrice() != null) {
                        tPayment.setTotalPrice(payment.getTotalPrice());
                    }
                    if (payment.getTreatmentHistories() != null) {
                        tPayment.setTreatmentHistories(payment.getTreatmentHistories());
                    }
                    if(payment.getNotePayable() != null){
                        tPayment.setNotePayable(payment.getNotePayable());
                    }

                    tPayment.setPaymentDetails(newPaymentDetails);
                    listDataHeader.add(tPayment);
                }
            }
        }

        Log.v("ServiceAdapter", String.valueOf(listDataHeader.size()));
        notifyDataSetChanged();
    }


}

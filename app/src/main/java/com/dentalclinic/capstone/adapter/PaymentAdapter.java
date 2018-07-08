package com.dentalclinic.capstone.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.responseobject.QuotesResponse;
import com.dentalclinic.capstone.api.services.ConverseService;
import com.dentalclinic.capstone.models.Payment;
import com.dentalclinic.capstone.models.PaymentDetail;
import com.dentalclinic.capstone.models.Staff;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.Config;
import com.dentalclinic.capstone.utils.DateTimeFormat;
import com.dentalclinic.capstone.utils.DateUtils;
import com.dentalclinic.capstone.utils.Utils;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Payment> listDataHeader; // header titles
    private List<Payment> listDataHeaderOriginal = new ArrayList<>();
    private ExpandableListView listView;
    private Fragment fragment;

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

    public PaymentAdapter(Context context, List<Payment> listDataHeader, ExpandableListView listView,Fragment  fragment) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listView = listView;
        this.fragment = fragment;
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
                if (staff != null) {
                    txtStaffName.setText(staff.getName());
                    Picasso.get().load(staff.getAvatar()).into(imgStaffAvatar);
                }
            }
            if (paymentDetail.getDateCreate() != null) {
//                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                txtDate.setText(DateUtils.changeDateFormat(paymentDetail.getDateCreate(), DateTimeFormat.DATE_TIME_DB, DateTimeFormat.DATE_APP_2));
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

    private static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(Config.PAYPAL_ENVIRONMENT).clientId(
                    Config.PAYPAL_CLIENT_ID);

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
            viewHolder.btnCheckOut = convertView.findViewById(R.id.btn_check_out_paypal);
            viewHolder.lnPayPal = convertView.findViewById(R.id.ln_paypal);
            viewHolder.lnStatus = convertView.findViewById(R.id.ln_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isExpanded) {
                    listView.expandGroup(groupPosition);

                } else {
                    listView.collapseGroup(groupPosition);
                }
            }
        });

        viewHolder.btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                launchPayPalPayment();
                ConverseService service = APIServiceManager.getCurencyService(ConverseService.class);
                Call<QuotesResponse> call = service.getConvers(Config.ACCESS_KEY);
                call.enqueue(new Callback<QuotesResponse>() {
                    @Override
                    public void onResponse(Call<QuotesResponse> call, Response<QuotesResponse> response) {
                        if (response.isSuccessful()) {
                            QuotesResponse quotes = response.body();
                            String dolaToVietNamDong = quotes == null ? "" : quotes.getQuotes().getVND();
                            Double money = Double.valueOf(dolaToVietNamDong);
                            Double dola =
                                    new BigDecimal(
                                            Double.parseDouble(
                                                    payment.getNotePayable() == null ? "0" : payment.getNotePayable().toString()
                                            ) / money)
                                            .setScale(2, RoundingMode.UP).doubleValue();
//                            PayPalItem item = new PayPalItem("payment", 1,
//                                    new BigDecimal(dola), Config.DEFAULT_CURRENCY, "123");
//                            productsInCart.add(item);
//                            launchPayPalPayment(dola);
                            launchPayPalPayment(payment.getId(),(double)1);
                        } else {
                            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<QuotesResponse> call, Throwable t) {
                        Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        if (isExpanded) {
            viewHolder.imgExpIcon.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        } else {
            viewHolder.imgExpIcon.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        }
        if (payment.getTreatmentNames() != null) {
            String treatmentName = "";
            for (int i = 0; i < payment.getTreatmentNames().size(); i++) {
                if (i == payment.getTreatmentNames().size() - 1) {
                    treatmentName += payment.getTreatmentNames().get(i);
                } else {
                    treatmentName += payment.getTreatmentNames().get(i) + ", ";
                }
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
            viewHolder.txtNotePayable.setText(Utils.formatMoney(payment.getNotePayable()) + context.getString(R.string.current_unit));
        }
        if (payment.isDone() == 1) {
            viewHolder.lnStatus.setVisibility(View.VISIBLE);
            viewHolder.txtStatus.setText(context.getResources().getString(R.string.status_done));
            viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.color_green_500));
        } else {
            viewHolder.lnPayPal.setVisibility(View.VISIBLE);
            viewHolder.txtStatus.setText(context.getResources().getString(R.string.status_not_done));
            viewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.color_red_500));
        }
        return convertView;
    }

    private static final int REQUEST_CODE_PAYMENT = 1;

    public void clearPayPalList() {
        productsInCart.clear();
    }

    private void launchPayPalPayment(int localPaymentId, Double money) {

        PayPalPayment thingsToBuy = prepareFinalCart(money);

        Intent intent = new Intent(context, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        intent.putExtra(AppConst.EXTRA_LOCAL_PAYMENT_ID, localPaymentId);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingsToBuy);

         this.fragment.startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private List<PayPalItem> productsInCart = new ArrayList<PayPalItem>();

    private PayPalPayment prepareFinalCart(Double money) {

        PayPalItem[] items = new PayPalItem[productsInCart.size()];
        items = productsInCart.toArray(items);

        // Total amount
        BigDecimal subtotal = new BigDecimal(money);

        // If you have shipping cost, add it here
        BigDecimal shipping = new BigDecimal("0.0");

        // If you have tax, add it here
        BigDecimal tax = new BigDecimal("0.0");

        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(
                shipping, subtotal, tax);

        BigDecimal amount = subtotal.add(shipping).add(tax);

        PayPalPayment payment = new PayPalPayment(
                amount,
                Config.DEFAULT_CURRENCY,
                "Số tiền bản phải trả là:",
                Config.PAYMENT_INTENT);

//        payment.items(items).paymentDetails(paymentDetails);

        // Custom field like invoice_number etc.,
//        payment.custom("This is text that will be associated with the payment that the app can use.");

        return payment;
    }

    public class GroupViewHolder {
        TextView txtTreatmentName, txtTotal, txtPrepaid, txtNotePayable, txtStatus;
        ImageView imgExpIcon;
        Button btnCheckOut;
        LinearLayout lnPayPal, lnStatus;
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
                    String date = DateUtils.changeDateFormat(paymentDetail.getDateCreate(), DateTimeFormat.DATE_TIME_DB, DateTimeFormat.DATE_APP_2);
                    if (paymentDetail.getDateCreate().contains(query.toLowerCase())) {
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
                    if (payment.getNotePayable() != null) {
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

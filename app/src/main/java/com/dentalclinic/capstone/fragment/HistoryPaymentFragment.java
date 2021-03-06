package com.dentalclinic.capstone.fragment;


import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.activities.MainActivity;
import com.dentalclinic.capstone.adapter.PaymentAdapter;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.responseobject.ErrorResponse;
import com.dentalclinic.capstone.api.responseobject.QuotesResponse;
import com.dentalclinic.capstone.api.services.ConverseService;
import com.dentalclinic.capstone.api.services.PaymentService;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.Payment;
import com.dentalclinic.capstone.models.PaymentDetail;
import com.dentalclinic.capstone.models.Staff;
import com.dentalclinic.capstone.models.TreatmentHistory;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.Config;
import com.dentalclinic.capstone.utils.CoreManager;
import com.dentalclinic.capstone.utils.Utils;
import com.google.android.gms.common.api.internal.ApiExceptionMapper;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryPaymentFragment extends BaseFragment implements MenuItem.OnActionExpandListener
        , SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    TextView txtEmptyList;

    private Disposable paymentDisposable;

    private List<PayPalItem> productsInCart = new ArrayList<PayPalItem>();

    public HistoryPaymentFragment() {
        // Required empty public constructor
    }

    private List<Payment> payments = new ArrayList<>();

    ExpandableListView expandableListView;
    private PaymentAdapter adapter;
    private static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(Config.PAYPAL_ENVIRONMENT).clientId(
                    Config.PAYPAL_CLIENT_ID);
    private Payment localPayment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Intent intent = new Intent(getContext(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        getContext().startService(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history_payment, container, false);
//        prepareData();
//        showLoading();
//        callApiGetAllTreatmentCategories();
        expandableListView = v.findViewById(R.id.eplv_list_payment);
        if (payments.isEmpty()) {
//            prepareData();
        }
        txtEmptyList = v.findViewById(R.id.txt_list_empty);

        adapter = new PaymentAdapter(
                getContext(),
                payments,
                expandableListView,
                this,
                new PaymentAdapter.BtnCheckoutListenter() {

                    @Override
                    public void onClick(View view, Payment payment) {
                        localPayment = payment;
                        ConverseService service = APIServiceManager.getCurencyService(ConverseService.class);
                        Call<QuotesResponse> call = service.getConvers(Config.ACCESS_KEY);
                        call.enqueue(new Callback<QuotesResponse>() {
                            @Override
                            public void onResponse(Call<QuotesResponse> call, retrofit2.Response<QuotesResponse> response) {
                                if (response.isSuccessful()) {
                                    QuotesResponse quotes = response.body();
                                    String dolaToVietNamDong = quotes == null ? "" : quotes.getQuotes().getVND();
                                    Double money = Double.valueOf(dolaToVietNamDong);
                                    Double dola =
                                            new BigDecimal(
                                                    Double.parseDouble(
                                                             (payment.getTotalPrice() - payment.getPaid())+""
                                                    ) / money)
                                                    .setScale(2, RoundingMode.UP).doubleValue();
//                            PayPalItem item = new PayPalItem("payment", 1,
//                                    new BigDecimal(dola), Config.DEFAULT_CURRENCY, "123");
//                            productsInCart.add(item);
                                    launchPayPalPayment(dola);
                                } else {
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<QuotesResponse> call, Throwable t) {
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });

        expandableListView.setAdapter(adapter);
        return v;
    }

    private void launchPayPalPayment(Double money) {

        PayPalPayment thingsToBuy = prepareFinalCart(money);

        Intent intent = new Intent(getContext(), PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
//        intent.putExtra(AppConst.EXTRA_LOCAL_PAYMENT_ID, localPaymentId);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingsToBuy);

        this.startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

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

    private void expandAll() {
        int count = adapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expandableListView.expandGroup(i);
        }
    }

    private void colpanlAll() {
        int count = adapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expandableListView.collapseGroup(i);
        }
    }

    public void notificationAdapter(List<Payment> payments) {
        if(!payments.isEmpty()){
            txtEmptyList.setVisibility(View.GONE);
        }
        this.payments.addAll(payments);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        adapter.filterData(s);
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        adapter.filterData(query);
        expandAll();
        return false;
    }

    @Override
    public boolean onClose() {
        adapter.filterData("");
        colpanlAll();
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.search_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setOnQueryTextListener(this);
//        searchView.setQueryHint("Tìm Ngày");
        super.onCreateOptionsMenu(menu, inflater);
    }

    private static final int REQUEST_CODE_PAYMENT = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.e(TAG, confirm.toJSONObject().toString(4));
                        Log.e(TAG, confirm.getPayment().toJSONObject()
                                .toString(4));

                        String paymentId = confirm.toJSONObject()
                                .getJSONObject("response").getString("id");

                        String paymentClient = confirm.getPayment()
                                .toJSONObject().toString();

                        // Now verify the payment on the server side
                        if (localPayment != null) {
                            verifyPaymentOnServer(localPayment.getId(), paymentId, paymentClient);
                        } else {
                            logError("onActivityResult", "Payment null");
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ",
                                e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e(TAG,
                        "An invalid Payment or PayPalConfiguration was submitted.");
            }
        }
    }

    private void verifyPaymentOnServer(int localPaymentId, final String paymentId,
                                       final String paymentClient) {
        showLoading();
        PaymentService service = APIServiceManager.getService(PaymentService.class);
        service.verifyPayment(localPaymentId, paymentId, paymentClient).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<Payment>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<List<Payment>> response) {
//                        logError("getPayment", data);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                showSuccessMessage("Thanh Toán Thành Công");
                                payments.clear();
                                payments.addAll(response.body());
                                adapter.notifyDataSetChanged();
                            }
                        } else if (response.code() == 500) {
                            showFatalError(response.errorBody(),"verifyPaymentOnServer");
                        } else if (response.code() == 401) {
                            showErrorUnAuth();
                        } else if (response.code() == 400) {
                            showBadRequestError(response.errorBody(),"verifyPaymentOnServer");
                        }
                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showErrorMessage("Có lỗi xảy ra");
                        hideLoading();
                    }
                });
        // Adding request to request queue
    }

//    public void prepareData() {
//        Payment payment = new Payment();
//        List<PaymentDetail> paymentDetails = new ArrayList<>();
//        PaymentDetail paymentDetail = new PaymentDetail(new Staff("Vo Quoc Trinh", "https://cdn3.vectorstock.com/i/1000x1000/30/97/flat-business-man-user-profile-avatar-icon-vector-4333097.jpg")
//                , Long.parseLong("100000"));
//        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//        String dtStart2 = "2018-06-19 00:00:00";
//        paymentDetail.setDateCreate(dtStart2);
//        paymentDetails.add(paymentDetail);
//        paymentDetails.add(paymentDetail);
//        paymentDetails.add(paymentDetail);
//        paymentDetails.add(paymentDetail);
//        payment.setPaymentDetails(paymentDetails);
//        payment.setDone(1);
//        payments.add(payment);
//        payments.add(payment);
//        payments.add(payment);
//
//
//        Patient currentPatient = CoreManager.getCurrentPatient(getContext());
//        if (currentPatient != null) {
//            PaymentService paymentService = APIServiceManager.getService(PaymentService.class);
//            paymentService.getByPhone(currentPatient.getPhone())
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new SingleObserver<Response<List<Payment>>>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            paymentDisposable = d;
//                        }
//
//                        @Override
//                        public void onSuccess(Response<List<Payment>> paymentResponse) {
//                            if (paymentResponse.isSuccessful()) {
//                                List<Payment> list = paymentResponse.body();
//                                if (list != null && list.size() > 0) {
//                                    payments.addAll(list);
//                                    adapter.notifyDataSetChanged();
//                                } else {
//
//                                }
//                            } else if (paymentResponse.code() == 500) {
//                                try {
//                                    String error = paymentResponse.errorBody().string();
//                                    showErrorMessage(getString(R.string.error_server));
//                                    logError("CallApi",
//                                            "success but fail: " + error);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                try {
//                                    String error = paymentResponse.errorBody().string();
//                                    ErrorResponse errorResponse = Utils.parseJson(error, ErrorResponse.class);
//                                    showErrorMessage(errorResponse.getErrorMessage());
//                                    logError("CallApi",
//                                            "success but fail: " + error);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            logError("Call API History payment", e.getMessage());
//                            showWarningMessage(getResources().getString(R.string.error_on_error_when_call_api));
//
//                        }
//                    });
//
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (paymentDisposable != null)
            paymentDisposable.dispose();
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return true;
    }


}

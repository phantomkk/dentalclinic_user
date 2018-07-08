package com.dentalclinic.capstone.fragment;


import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.activities.MainActivity;
import com.dentalclinic.capstone.adapter.PaymentAdapter;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.responseobject.ErrorResponse;
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
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryPaymentFragment extends BaseFragment implements MenuItem.OnActionExpandListener
        , SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Disposable paymentDisposable;

    public HistoryPaymentFragment() {
        // Required empty public constructor
    }

    private List<Payment> payments = new ArrayList<>();

    ExpandableListView expandableListView;
    private PaymentAdapter adapter;
    private static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(Config.PAYPAL_ENVIRONMENT).clientId(
                    Config.PAYPAL_CLIENT_ID);

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
        adapter = new PaymentAdapter(getContext(), payments, expandableListView,this);
        expandableListView.setAdapter(adapter);
        return v;
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
                Bundle bundle = data.getExtras();
                if (confirm != null) {
                    try {
                        Log.e(TAG, confirm.toJSONObject().toString(4));
                        Log.e(TAG, confirm.getPayment().toJSONObject()
                                .toString(4));

                        String paymentId = confirm.toJSONObject()
                                .getJSONObject("response").getString("id");

                        String paymentClient = confirm.getPayment()
                                .toJSONObject().toString();
                        int localPaymentId = bundle == null ? 0 : bundle.getInt(AppConst.EXTRA_LOCAL_PAYMENT_ID);
                        Log.e(TAG, "paymentId: " + paymentId
                                + ", payment_json: " + paymentClient);

                        // Now verify the payment on the server side
                        verifyPaymentOnServer(localPaymentId,paymentId, paymentClient);

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
        // Showing progress dialog before making request
//        showLoading();
//        StringRequest verifyReq = new StringRequest(Request.Method.POST,
//                Config.URL_VERIFY_PAYMENT, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "verify payment: " + response.toString());
//
//                try {
//                    JSONObject res = new JSONObject(response);
//                    boolean error = res.getBoolean("error");
//                    String message = res.getString("message");
//                    // user error boolean flag to check for errors
//                    Toast.makeText(getContext(), message,
//                            Toast.LENGTH_SHORT).show();
//                    if (!error) {
//                        // empty the cart
//                        adapter.clearPayPalList();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                hideLoading();
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Verify Error: " + error.getMessage());
//                Toast.makeText(getContext(),
//                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                // hiding the progress dialog
//                hideLoading();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("paymentId", paymentId);
//                params.put("paymentClientJson", paymentClient);
//
//                return params;
//            }
//        };
//
//        // Setting timeout to volley request as verification request takes sometime
//        int socketTimeout = 60000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        verifyReq.setRetryPolicy(policy);
//my request service
        PaymentService service = APIServiceManager.getService(PaymentService.class);
        service.verifyPayment(localPaymentId, paymentId, paymentClient).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<retrofit2.Response<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(retrofit2.Response<String> stringResponse) {
                        String data = stringResponse.body();
                        logError("getPayment", data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showErrorMessage("Có lỗi xảy ra");
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

//    private TreatmentCategoryService treatmentCategory = APIServiceManager.getService(TreatmentCategoryService.class);
//    private Disposable treatmentCategoriesServiceDisposable;
//
//    public void callApiGetAllTreatmentCategories() {
//        treatmentCategory.getAll().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleObserver<Response<List<TreatmentCategory>>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        hideLoading();
//                        treatmentCategoriesServiceDisposable = d;
//                    }
//
//                    @Override
//                    public void onSuccess(Response<List<TreatmentCategory>> listResponse) {
//                        hideLoading();
//                        if (listResponse.isSuccessful()) {
//                            treatmentCategories.addAll(listResponse.body());
//                            adapter.getListDataHeaderOriginal().addAll(treatmentCategories);
//                            adapter.notifyDataSetChanged();
//                            logError("treatmentCategories", String.valueOf(treatmentCategories.size()));
//                        } else {
////                            String erroMsg = Utils.getErrorMsg(listResponse.errorBody());
////                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
////                                    .setMessage(erroMsg)
////                                    .setPositiveButton("Thử lại", (DialogInterface dialogInterface, int i) -> {
////                                    }) ;
////                            alertDialog.show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        hideLoading();
//                        e.printStackTrace();
//                        Toast.makeText(getActivity(), getResources().getString(R.string.error_on_error_when_call_api), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}

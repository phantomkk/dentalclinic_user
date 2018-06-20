package com.dentalclinic.capstone.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.adapter.PaymentAdapter;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.responseobject.ErrorResponse;
import com.dentalclinic.capstone.api.services.PaymentService;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.Payment;
import com.dentalclinic.capstone.models.PaymentDetail;
import com.dentalclinic.capstone.models.Staff;
import com.dentalclinic.capstone.utils.CoreManager;
import com.dentalclinic.capstone.utils.Utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryPaymentFragment extends BaseFragment implements MenuItem.OnActionExpandListener
        , SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private Disposable paymentDisposable;

    public HistoryPaymentFragment() {
        // Required empty public constructor
    }

    List<Payment> payments = new ArrayList<>();

    ExpandableListView expandableListView;
    PaymentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
            prepareData();
        }
        adapter = new PaymentAdapter(getContext(), payments);
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
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Tìm Ngày");
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void prepareData() {
        Payment payment = new Payment();
        List<PaymentDetail> paymentDetails = new ArrayList<>();
        PaymentDetail paymentDetail = new PaymentDetail(new Staff("Vo Quoc Trinh","https://cdn3.vectorstock.com/i/1000x1000/30/97/flat-business-man-user-profile-avatar-icon-vector-4333097.jpg")
        ,Long.parseLong("100000"));
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dtStart2 = "2018-06-19 00:00:00";
        paymentDetail.setDateCreate(dtStart2);
        paymentDetails.add(paymentDetail);
        paymentDetails.add(paymentDetail);
        paymentDetails.add(paymentDetail);
        paymentDetails.add(paymentDetail);
        payment.setPaymentDetails(paymentDetails);
        payment.setDone(true);
        payments.add(payment);
        payments.add(payment);
        payments.add(payment);


//        Patient currentPatient = CoreManager.getCurrentPatient();
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
//                                }else{
//
//                                }
//                            } else {
//                                try {
//                                    String error = paymentResponse.errorBody().string();
//                                    logError("CallApi",
//                                            "success but fail: " + error);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            logError(HistoryPaymentFragment.class.getSimpleName(), e.getMessage());
//                        }
//                    });
//
//        }
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

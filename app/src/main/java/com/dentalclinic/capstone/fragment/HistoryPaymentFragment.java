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
import com.dentalclinic.capstone.adapter.ServiceAdapter;
import com.dentalclinic.capstone.models.Payment;
import com.dentalclinic.capstone.models.PaymentDetail;
import com.dentalclinic.capstone.models.Staff;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryPaymentFragment extends BaseFragment implements MenuItem.OnActionExpandListener
        , SearchView.OnQueryTextListener, SearchView.OnCloseListener {


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
        String dtStart2 = "01-01-1996";
        try {
            paymentDetail.setDateCreate(format.parse(dtStart2));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        paymentDetails.add(paymentDetail);
        paymentDetails.add(paymentDetail);
        paymentDetails.add(paymentDetail);
        paymentDetails.add(paymentDetail);
        payment.setPaymentDetails(paymentDetails);
        payment.setDone(true);
        payments.add(payment);
        payments.add(payment);
        payments.add(payment);

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
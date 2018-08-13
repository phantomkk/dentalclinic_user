package com.dentalclinic.capstone.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import io.reactivex.functions.BiFunction;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.adapter.HistoryPageAdapter;
import com.dentalclinic.capstone.adapter.PageAdapter;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.CombineClass;
import com.dentalclinic.capstone.api.CombineHistoryClass;
import com.dentalclinic.capstone.api.services.AppointmentService;
import com.dentalclinic.capstone.api.services.HistoryTreatmentService;
import com.dentalclinic.capstone.api.services.PaymentService;
import com.dentalclinic.capstone.models.Appointment;
import com.dentalclinic.capstone.models.News;
import com.dentalclinic.capstone.models.Payment;
import com.dentalclinic.capstone.models.TreatmentHistory;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.CoreManager;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends BaseFragment {

    public HistoryFragment() {
    }

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        tabLayout = v.findViewById(R.id.sliding_tabs);
        viewPager = v.findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);
        callAPI();
        return v;
    }

    private HistoryAppointmentFragment historyAppointmentFragment = new HistoryAppointmentFragment();
    private HistoryTreatmentFragment historyTreatmentFragment = new HistoryTreatmentFragment();
    private HistoryPaymentFragment historyPaymentFragment = new HistoryPaymentFragment();

    private void setupViewPager(ViewPager viewPager) {
        PageAdapter adapter = new PageAdapter(getChildFragmentManager());
        adapter.addFragment(historyTreatmentFragment, getResources().getString(R.string.history_treatment_fragment_title));
        adapter.addFragment(historyPaymentFragment, getResources().getString(R.string.history_payment_fragment_title));
        adapter.addFragment(historyAppointmentFragment, getResources().getString(R.string.history_appointment_title));
        viewPager.setAdapter(adapter);
    }

    private Disposable disposable;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppConst.REQUEST_CODE_REMINDER){
            viewPager.setCurrentItem(2);
        }
    }
    private void callAPI() {
        showLoading();
        if(CoreManager.getCurrentPatient(getContext()) !=null){
            Single appointment = APIServiceManager.getService(AppointmentService.class)
                    .getByPhone(CoreManager.getUser(getContext()).getPhone()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            Single payment = APIServiceManager.getService(PaymentService.class)
                    .getByPhone(CoreManager.getUser(getContext()).getPhone()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            Single treatmentHistories = APIServiceManager.getService(HistoryTreatmentService.class)
                    .getHistoryTreatmentById(CoreManager.getCurrentPatient(getContext()).getId()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

            Single<CombineHistoryClass> combine = Single.zip(treatmentHistories, payment, appointment,
                    new Function3<Response<List<TreatmentHistory>>, Response<List<Payment>>, Response<List<Appointment>>, CombineHistoryClass>() {
                        @Override
                        public CombineHistoryClass apply(Response<List<TreatmentHistory>> treatmentHistoriesResponse, Response<List<Payment>> paymentsResponse, Response<List<Appointment>> appointmentRespone) throws Exception {
                            return new CombineHistoryClass(treatmentHistoriesResponse, paymentsResponse, appointmentRespone);
                        }
                    });
            combine.subscribe(new SingleObserver<CombineHistoryClass>() {
                @Override
                public void onSubscribe(Disposable d) {
                    disposable = d;
                }

                @Override
                public void onSuccess(CombineHistoryClass combineClass) {
                    if (combineClass.getTreatmentHistories() != null) {
                        if (combineClass.getTreatmentHistories().isSuccessful()) {
                            historyTreatmentFragment.notificationAdapter(combineClass.getTreatmentHistories().body());
                        } else if (combineClass.getTreatmentHistories().code() == 500) {
                            showFatalError(combineClass.getTreatmentHistories().errorBody(), "callAPI.getTreatmentHistories");
                        } else if (combineClass.getTreatmentHistories().code() == 401) {
                            showErrorUnAuth();
                        } else if (combineClass.getTreatmentHistories().code() == 400) {
                            showBadRequestError(combineClass.getTreatmentHistories().errorBody(), "callAPI.getTreatmentHistories");
                        } else {
                            showDialog(getContext().getResources().getString(R.string.error_message_api));
                        }
                    }
                    if (combineClass.getPayments() != null) {
                        if (combineClass.getPayments().isSuccessful()) {
                            //
                            historyPaymentFragment.notificationAdapter(combineClass.getPayments().body());
                        } else if (combineClass.getPayments().code() == 500) {
                            showFatalError(combineClass.getPayments().errorBody(), "callAPI.getPayments");
                        } else if (combineClass.getPayments().code() == 401) {
                            showErrorUnAuth();
                        } else if (combineClass.getPayments().code() == 400) {
                            showBadRequestError(combineClass.getPayments().errorBody(), "callAPI.getPayments");
                        } else {
                            showDialog(getContext().getResources().getString(R.string.error_message_api));
                        }
                    }
                    if (combineClass.getAppointment() != null) {
                        if (combineClass.getAppointment().isSuccessful()) {
                            historyAppointmentFragment.notificationAdapter(combineClass.getAppointment().body());
                        } else if (combineClass.getAppointment().code() == 500) {
                            showFatalError(combineClass.getAppointment().errorBody(), "callAPI.getAppointment");
                        } else if (combineClass.getAppointment().code() == 401) {
                            showErrorUnAuth();
                        } else if (combineClass.getAppointment().code() == 400) {
                            showBadRequestError(combineClass.getAppointment().errorBody(), "callAPI.getAppointment");
                        } else {
                            showDialog(getContext().getResources().getString(R.string.error_message_api));
                        }
                    }
                    hideLoading();
                }

                @Override
                public void onError(Throwable e) {
                    hideLoading();
                    e.printStackTrace();
                    logError("Error", e.getMessage());
                    Context context = getContext();
                    if(context!=null) {
                        Toast.makeText(context, getString(R.string.error_on_error_when_call_api), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Single appointment = APIServiceManager.getService(AppointmentService.class)
                    .getByPhone(CoreManager.getUser(getContext()).getPhone()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            Single payment = APIServiceManager.getService(PaymentService.class)
                    .getByPhone(CoreManager.getUser(getContext()).getPhone()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
//            Single treatmentHistories = APIServiceManager.getService(HistoryTreatmentService.class)
//                    .getHistoryTreatmentById(CoreManager.getCurrentPatient(getContext()).getId()).subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread());

            Single<CombineHistoryClass> combine = Single.zip(payment, appointment,
                    new BiFunction<Response<List<Payment>>, Response<List<Appointment>>, CombineHistoryClass>() {
                        @Override
                        public CombineHistoryClass apply(Response<List<Payment>> paymentsResponse, Response<List<Appointment>> appointmentRespone) throws Exception {
                            return new CombineHistoryClass(paymentsResponse, appointmentRespone);
                        }
                    });
            combine.subscribe(new SingleObserver<CombineHistoryClass>() {
                @Override
                public void onSubscribe(Disposable d) {
                    disposable = d;
                }

                @Override
                public void onSuccess(CombineHistoryClass combineClass) {
                    if (combineClass.getPayments() != null) {
                        if (combineClass.getPayments().isSuccessful()) {
                            //
                            historyPaymentFragment.notificationAdapter(combineClass.getPayments().body());
                        } else if (combineClass.getPayments().code() == 500) {
                            showFatalError(combineClass.getPayments().errorBody(), "callAPI.getPayments");
                        } else if (combineClass.getPayments().code() == 401) {
                            showErrorUnAuth();
                        } else if (combineClass.getPayments().code() == 400) {
                            showBadRequestError(combineClass.getPayments().errorBody(), "callAPI.getPayments");
                        } else {
                            showDialog(getContext().getResources().getString(R.string.error_message_api));
                        }
                    }
                    if (combineClass.getAppointment() != null) {
                        if (combineClass.getAppointment().isSuccessful()) {
                            historyAppointmentFragment.notificationAdapter(combineClass.getAppointment().body());
                        } else if (combineClass.getAppointment().code() == 500) {
                            showFatalError(combineClass.getAppointment().errorBody(), "callAPI.getAppointment");
                        } else if (combineClass.getAppointment().code() == 401) {
                            showErrorUnAuth();
                        } else if (combineClass.getAppointment().code() == 400) {
                            showBadRequestError(combineClass.getAppointment().errorBody(), "callAPI.getAppointment");
                        } else {
                            showDialog(getContext().getResources().getString(R.string.error_message_api));
                        }
                    }
                    hideLoading();
                }

                @Override
                public void onError(Throwable e) {
                    hideLoading();
                    e.printStackTrace();
                    logError("Error", e.getMessage());
                    Context context = getContext();
                    if(context!=null) {
                        Toast.makeText(context, getString(R.string.error_on_error_when_call_api), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

}

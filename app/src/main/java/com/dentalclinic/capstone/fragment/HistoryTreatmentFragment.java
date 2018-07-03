package com.dentalclinic.capstone.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.activities.TreatmentDetailActivity;
import com.dentalclinic.capstone.adapter.PatientAdapter;
import com.dentalclinic.capstone.adapter.TreatmentHistoryAdapter;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.responseobject.ErrorResponse;
import com.dentalclinic.capstone.api.services.HistoryTreatmentService;
import com.dentalclinic.capstone.models.Event;
import com.dentalclinic.capstone.models.News;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.Tooth;
import com.dentalclinic.capstone.models.Treatment;
import com.dentalclinic.capstone.models.TreatmentDetail;
import com.dentalclinic.capstone.models.TreatmentHistory;
import com.dentalclinic.capstone.models.User;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.CoreManager;
import com.dentalclinic.capstone.utils.DateTimeFormat;
import com.dentalclinic.capstone.utils.DateUtils;
import com.dentalclinic.capstone.utils.Utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.internal.Util;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryTreatmentFragment extends BaseFragment {
    public ListView listView;
    public TreatmentHistoryAdapter adapter;
    private List<TreatmentHistory> treatmentHistories = new ArrayList<TreatmentHistory>();
    private static HistoryTreatmentFragment instance = new HistoryTreatmentFragment();

    public static HistoryTreatmentFragment newInstance() {
        instance = new HistoryTreatmentFragment();
        return instance;
    }

    public static HistoryTreatmentFragment getInstance() {
        return instance;
    }

    public HistoryTreatmentFragment() {
        // Required empty public constructor
    }

    public void notificationAdapter(List<TreatmentHistory> treatmentHistories) {
        this.treatmentHistories.addAll(treatmentHistories);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history_treatment, container, false);
        listView = (ListView) v.findViewById(R.id.list_profile);
//        prepareListData();
        adapter = new TreatmentHistoryAdapter(getContext(), treatmentHistories);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                List<TreatmentDetail> details = treatmentHistories.get(i).getTreatmentDetails();
                if (details != null) {
                    if (!details.isEmpty()) {
                        Intent intent = new Intent(getContext(), TreatmentDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppConst.TREATMENT_HISTORY_OBJ, treatmentHistories.get(i));
                        intent.putExtra(AppConst.BUNDLE, bundle);
                        startActivity(intent);
                    }
                }

            }
        });


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Patient p = CoreManager.getCurrentPatient(getContext());
        if (p != null) {
//            callApi(p.getId());
        }
    }

//    HashMap<Patient, List<TreatmentHistory>> listDataChild;
//    List<Patient> patientList;
//    List<Patient> patientList;

    private void prepareListData() {

//        listDataChild = new HashMap<Patient, List<TreatmentHistory>>();
//        patientList = new ArrayList<Patient>();
//        Patient patient = new Patient();
//        patient.setName("vo quoc trinh");
//        patient.setAddress("go vap");
//        patient.setPhone("01685149049");
//        String dtStart = "30-06-1996";
//        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//        try {
//            patient.setDateOfBirth(format.parse(dtStart));
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        patient.setAvatar("https://scontent.fsgn5-5.fna.fbcdn.net/v/t1.0-9/27545086_1520633358052034_8513856773051240273_n.jpg?_nc_cat=0&_nc_eui2=AeHYSp5Hio8m6PKlSmzZfQXfME6SBJSaL_gi0QNCyxngl3PUW9JlHbLJFMv8Jn3x8nnkAP1xgtEsRjsrun9WOY_px9vNetVWiuSOy_Uvv5NGJw&oh=6c8421d07717bb804b0059471ded6cd0&oe=5BB3A0B1");
//
//        Patient patient2 = new Patient();
//        patient2.setName("nhieu sy luc");
//        patient2.setAddress("quan 12");
//        patient2.setPhone("01685149049");
//        String dtStart2 = "01-01-1996";
//        try {
////            patient2.setDateOfBirth(format.parse(dtStart2));
//            patient2.setDateOfBirth(DateUtils.changeDateFormat(dtStart2, DateTimeFormat.DATE_TIME_DB, DateTimeFormat.DATE_APP_2));
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        patient2.setAvatar("https://scontent.fsgn5-5.fna.fbcdn.net/v/t1.0-9/21106763_661579387371492_6919408620920338286_n.jpg?_nc_cat=0&_nc_eui2=AeFS24o42DjWOdszwhxK3fB8ztBLi1_14lzqdUPhz2P8iesHrofix5GpCo4bNdwV1f7W3cfkRM4k0TqlcNvwqWzUPhy4yKMlJ_gOD0adW4C5-g&oh=d3b2103d7ec405372c1f7518676e00f7&oe=5BB82FD4");

        // Adding child data
        TreatmentHistory treatmentHistory = new TreatmentHistory();
        treatmentHistory.setTreatment(new Treatment("Trám Răng", new Event(10)));
        treatmentHistory.setTooth(new Tooth("răng cửa"));
        treatmentHistory.setTotalPrice(Long.valueOf("100000"));
        treatmentHistory.setPrice(Long.valueOf("100000"));
        String dtStart3 = "1996-06-30 00:00:00";
        treatmentHistory.setCreateDate(dtStart3);
        treatmentHistory.setFinishDate(dtStart3);
//        treatmentHistory.setCreateDate(DateUtils.changeDateFormat(dtStart3, DateTimeFormat.DATE_TIME_DB, DateTimeFormat.DATE_APP_2));
//        treatmentHistory.setFinishDate(DateUtils.changeDateFormat(dtStart3, DateTimeFormat.DATE_TIME_DB, DateTimeFormat.DATE_APP_2));
        treatmentHistories.add(treatmentHistory);
        treatmentHistories.add(treatmentHistory);
//        patient.setTreatmentHistories(treatmentHistories);
//        patient2.setTreatmentHistories(treatmentHistories);

//        patientList.add(patient);
//        patientList.add(patient2);
    }

    private void callApi(int patientID) {
        HistoryTreatmentService service = APIServiceManager.getService(HistoryTreatmentService.class);
        service.getHistoryTreatmentById(patientID)
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<TreatmentHistory>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<List<TreatmentHistory>> listResponse) {
                        if (listResponse.isSuccessful()) {
                            if (listResponse.body() != null) {
                                List<TreatmentHistory> list = listResponse.body();
                                Patient patient = CoreManager.getCurrentPatient(getContext());
                                if (patient != null) {
                                    patient.setTreatmentHistories(list);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } else if (listResponse.code() == 500) {
                            try {
                                String error = listResponse.errorBody().string();
                                logError("CallAPI", error);
                                showErrorMessage(getString(R.string.error_server));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ErrorResponse errorResponse = null;
                            try {
                                errorResponse = Utils.parseJson(listResponse.errorBody().string(),
                                        ErrorResponse.class);
                                showErrorMessage(errorResponse.getErrorMessage());
                                logError("CallAPI HISTORY TREATMENT", errorResponse.getExceptionMessage());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showWarningMessage(getResources().getString(R.string.error_on_error_when_call_api));
//                        logError("CallAPI", e.getMessage());
                    }
                });
    }
}

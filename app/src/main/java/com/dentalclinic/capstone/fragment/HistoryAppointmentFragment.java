package com.dentalclinic.capstone.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.activities.QuickBookActivity;
import com.dentalclinic.capstone.adapter.AppointmentAdapter;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.services.AppointmentService;
import com.dentalclinic.capstone.models.Appointment;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.utils.CoreManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryAppointmentFragment extends BaseFragment implements View.OnClickListener {


    public HistoryAppointmentFragment() {
        // Required empty public constructor
    }

    ListView lvHistoryAppoint;
    AppointmentAdapter adapter;
    List<Appointment> appointments = new ArrayList<>();
    FloatingActionButton button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_appointment, container, false);
        lvHistoryAppoint = view.findViewById(R.id.lv_history_appointment);
        button = view.findViewById(R.id.fab_button);
        button.setOnClickListener(this);
        adapter = new AppointmentAdapter(getContext(), appointments);
//        prepareData();
        lvHistoryAppoint.setAdapter(adapter);
        registerForContextMenu(lvHistoryAppoint);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.lv_history_appointment) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int selectpos = info.position; //position in the adapter
        switch(item.getItemId()) {
            case R.id.edit:
                // edit stuff here
                return true;
            case R.id.delete:
                // remove stuff here
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareData();
    }

    public void prepareData() {
//        Patient patient = CoreManager.getCurrentPatient();
//        showLoading();
//        AppointmentService appointmentService = APIServiceManager.getService(AppointmentService.class);
//        appointmentService.getByPhone("0915469963")
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleObserver<Response<List<Appointment>>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(Response<List<Appointment>> listResponse) {
//                        if (listResponse.body() != null) {
////                            List<Appointment> appointments = listResponse.body();
////                            appointments.sort(new Comparator<Appointment>() {
////                                @Override
////                                public int compare(Appointment appointment, Appointment t1) {
////                                    return appointment.getStartTime() - appointment.getStartTime();
////                                }
////                            });
//                            appointments.addAll(listResponse.body());
//                            adapter.notifyDataSetChanged();
//                        } else {
//                            Toast.makeText(getContext(), "SUCCESS but else", Toast.LENGTH_SHORT).show();
//                        }
//                        hideLoading();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        Toast.makeText(getContext(), "On Error", Toast.LENGTH_SHORT).show();
//                        hideLoading();
//                    }
//                });
        Appointment appointment = new Appointment();
        appointment.setNumericalOrder(12);
        appointment.setStartTime("2018-06-21 00:00:00");
        appointment.setId(1);
        appointments.add(appointment);
        appointment.setId(2);
        appointments.add(appointment);
        appointment.setId(3);
        appointments.add(appointment);
        appointment.setId(4);
        appointments.add(appointment);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_button:
                Intent myIntent = new Intent(getActivity(), QuickBookActivity.class);
                startActivity(myIntent);
        }
    }
}

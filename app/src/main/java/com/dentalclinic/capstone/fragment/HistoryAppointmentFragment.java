package com.dentalclinic.capstone.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.activities.QuickBookActivity;
import com.dentalclinic.capstone.adapter.AppointmentAdapter;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.responseobject.ErrorResponse;
import com.dentalclinic.capstone.api.services.AppointmentService;
import com.dentalclinic.capstone.models.Appointment;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.TreatmentHistory;
import com.dentalclinic.capstone.utils.CoreManager;
import com.dentalclinic.capstone.utils.Utils;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
    TextView txtEmptyList;
    public AppointmentAdapter adapter;
    public List<Appointment> appointments = new ArrayList<>();
    FloatingActionButton button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_appointment, container, false);
        lvHistoryAppoint = view.findViewById(R.id.lv_history_appointment);
        button = view.findViewById(R.id.fab_button);
        txtEmptyList = view.findViewById(R.id.txt_list_empty);
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
        if (v.getId() == R.id.lv_history_appointment) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int selectpos = info.position; //position in the adapter
        switch (item.getItemId()) {
            case R.id.edit:
                showDatePicker();

                // edit stuff here
                return true;
            case R.id.delete:
                showConfirmDialog();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void notificationAdapter(List<Appointment> appointments2) {
        if (appointments2.size() > 0) {
            txtEmptyList.setVisibility(View.GONE);
        } else {
            txtEmptyList.setVisibility(View.VISIBLE);
        }
        appointments.addAll(appointments2);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        prepareData();
    }

    private Disposable appointmentDisposable;

    public void prepareData() {
//        Patient patient = CoreManager.getCurrentPatient();
        showLoading();
        AppointmentService appointmentService = APIServiceManager.getService(AppointmentService.class);
        appointmentService.getByPhone(CoreManager.getUser(getContext()).getPhone())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<Appointment>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        appointmentDisposable = d;
                    }

                    @Override
                    public void onSuccess(Response<List<Appointment>> listResponse) {
                        if (listResponse.body() != null) {
                            if (listResponse.body().size() > 0) {
                                txtEmptyList.setVisibility(View.GONE);
                            } else {
                                txtEmptyList.setVisibility(View.VISIBLE);
                            }
                            appointments.addAll(listResponse.body());
                            adapter.notifyDataSetChanged();
                        }  else if (listResponse.code() == 500) {
                            try {
                                String error = listResponse.errorBody().string();
                                showErrorMessage(getString(R.string.error_server));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                String error = listResponse.errorBody().string();
                                ErrorResponse errorResponse = Utils.parseJson(error, ErrorResponse.class);
                                showErrorMessage(errorResponse.getErrorMessage());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "On Error", Toast.LENGTH_SHORT).show();
                        hideLoading();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_button:
                Intent myIntent = new Intent(getActivity(), QuickBookActivity.class);
                startActivity(myIntent);
        }
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage("Bạn có chắc muốn xóa lịch đặt này?").setTitle("Xác nhận xóa")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Yes-code

                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void showDatePicker(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                (DatePicker datePicker, int iYear, int iMonth, int iDay) -> {
                    String date = iDay + "/" + (iMonth+1) + "/" + iYear;
                    c.set(iYear, iMonth, iDay);
                    Calendar currentDay = Calendar.getInstance();
                    if (currentDay.after(c)) {
                        showWarningMessage("Chọn ngày không hợp lệ. Vui lòng chọn ngày khác.");
                    } else {
                        //call api
//                        tvDateError.setText("");
                    }
                }, year, month, day);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }



}

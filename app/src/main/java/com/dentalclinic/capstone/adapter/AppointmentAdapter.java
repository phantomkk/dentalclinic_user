package com.dentalclinic.capstone.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.models.Appointment;
import com.dentalclinic.capstone.utils.DateTimeFormat;
import com.dentalclinic.capstone.utils.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.internal.Util;

public class AppointmentAdapter extends ArrayAdapter<Appointment> {
    private List<Appointment> appointments;

    public AppointmentAdapter(Context context, List<Appointment> appointments) {
        super(context, 0, appointments);
        this.appointments = appointments;
    }

    private static class ViewHolder {
        TextView txtDateTime, txtNumber, txtStatus;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = convertView;
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_appointment, parent, false);
            viewHolder.txtDateTime = convertView.findViewById(R.id.txt_date_time);
            viewHolder.txtNumber = convertView.findViewById(R.id.txt_number);
            viewHolder.txtStatus = convertView.findViewById(R.id.txt_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Appointment appointment = appointments.get(position);
        if (appointment != null) {
            String dateTime = "";
            if (appointment.getStartTime() != null) {
                dateTime += appointment.getStartTime().toString();
            }

            //status có 3 trạng thái : hết hạn: red_500, đến giờ (trong ngày): xanh lá green_500 , sắp diễn ra :cam
        }

        String startTimeDBFormat = appointment.getStartTime();
        if (startTimeDBFormat != null && startTimeDBFormat.length() > 0) {
            String strStartTimeAppFormat = DateUtils.changeDateFormat(
                    appointment.getStartTime(),
                    DateTimeFormat.DATE_TIME_DB,
                    DateTimeFormat.DATE_TIME_APP);
            Date startTime = DateUtils.getDate(strStartTimeAppFormat, DateTimeFormat.DATE_TIME_APP);
            if (startTime != null) {
                Calendar currentDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();
                endDate.set(
                        endDate.get(Calendar.YEAR),
                        endDate.get(Calendar.MONTH),
                        endDate.get(Calendar.DAY_OF_MONTH),
                        18,
                        0
                );
                Calendar startTimeOfCrrDay = Calendar.getInstance();
                startTimeOfCrrDay.set(
                        startTimeOfCrrDay.get(Calendar.YEAR),
                        startTimeOfCrrDay.get(Calendar.MONTH),
                        startTimeOfCrrDay.get(Calendar.DAY_OF_MONTH),
                        6,
                        0
                );
                String status = "Đến giờ";
                if (startTime.after(startTimeOfCrrDay.getTime())
                        && startTime.before(currentDate.getTime())
                        && currentDate.before(endDate)) {
                    viewHolder.txtStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_yellow_500));
                    status = "Đang diễn ra";
                } else if (startTime.before(currentDate.getTime())) {
                    status = "Hết hạn";
                    viewHolder.txtStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_red_500));
                } else if (startTime.after(currentDate.getTime())) {
                    viewHolder.txtStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.color_green_500));
                    status = "Sắp diễn ra";
                }

                viewHolder.txtStatus.setText(status);
            }
            viewHolder.txtDateTime.setText(strStartTimeAppFormat);
            viewHolder.txtNumber.setText(appointment.getNumericalOrder()+"");
        }



        //        String stringDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(date);

        return convertView;
    }

}

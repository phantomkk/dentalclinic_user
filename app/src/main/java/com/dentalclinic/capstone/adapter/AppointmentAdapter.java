package com.dentalclinic.capstone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.animation.MyGridView;
import com.dentalclinic.capstone.models.Appointment;
import com.dentalclinic.capstone.models.Prescription;
import com.dentalclinic.capstone.models.TreatmentDetail;
import com.dentalclinic.capstone.models.TreatmentDetailStep;
import com.dentalclinic.capstone.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
            String dateTime ="";
            if(appointment.getStartTime()!=null){
                dateTime+= appointment.getStartTime().toString();
            }
            if(appointment.getDateBooking()!=null){
                dateTime+=appointment.getDateBooking();
            }
            //status có 3 trạng thái : hết hạn: red_500, đến giờ (trong ngày): xanh lá green_500 , sắp diễn ra :cam
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = dateFormat.parse("09:30 pm 2018-06-14 01:16:10");
            viewHolder.txtDateTime.setText(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //        String stringDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(date);

        return convertView;
    }

}

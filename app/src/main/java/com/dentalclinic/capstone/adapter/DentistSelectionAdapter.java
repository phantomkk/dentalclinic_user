package com.dentalclinic.capstone.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.models.Staff;

import java.util.List;

public class DentistSelectionAdapter extends RecyclerView.Adapter<DentistSelectionAdapter.ViewHolder> {
    private List<Staff> list;
    private RadioButton previousRbt;

    public DentistSelectionAdapter(List<Staff> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dentist_sltdentist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Staff staff = list.get(position);
        holder.tvDentistName.setText(staff.getName());
        holder.rbtDentist.setOnCheckedChangeListener((CompoundButton compoundButton, boolean b) -> {
            RadioButton crrRbt = compoundButton.findViewById(R.id.rbt_dentist_sltdentist);
            if (previousRbt != null) {
                previousRbt.setChecked(false);
            }
            previousRbt = crrRbt;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDentistName;
        RadioButton rbtDentist;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDentistName = itemView.findViewById(R.id.txt_dentist_name);
            rbtDentist = itemView.findViewById(R.id.rbt_dentist_sltdentist);
            itemView.setOnClickListener((view -> {
                    RadioButton crrRbt = view.findViewById(R.id.rbt_dentist_sltdentist);
                    crrRbt.setChecked(true);
//                rbtDentist.setOnCheckedChangeListener((CompoundButton compoundButton, boolean b) -> {
//                    if (previousRbt != null) {
//                        previousRbt.setChecked(false);
//                    }
//                    previousRbt = crrRbt;
//                });
            }));
        }
    }
}

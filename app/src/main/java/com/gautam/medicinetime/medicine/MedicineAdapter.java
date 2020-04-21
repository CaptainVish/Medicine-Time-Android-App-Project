package com.gautam.medicinetime.medicine;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gautam.medicinetime.R;
import com.gautam.medicinetime.data.source.MedicineAlarm;
import com.gautam.medicinetime.views.RobotoBoldTextView;
import com.gautam.medicinetime.views.RobotoRegularTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gautam on 13/07/17.
 */

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private List<MedicineAlarm> medicineAlarmList;
    private OnItemClickListener onItemClickListener;

    public MedicineAdapter(List<MedicineAlarm> medicineAlarmList) {
        this.medicineAlarmList = medicineAlarmList;
    }

    public void replaceData(List<MedicineAlarm> medicineAlarmList) {
        this.medicineAlarmList = medicineAlarmList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        final MedicineAlarm medicineAlarm = medicineAlarmList.get(position);
        if (medicineAlarm == null) {
            return;
        }
        holder.tvMedTime.setText(medicineAlarm.getStringTime());
        holder.tvMedicineName.setText(medicineAlarm.getPillName());
        holder.tvDoseDetails.setText(medicineAlarm.getFormattedDose());
        holder.ivAlarmDelete.setVisibility(View.VISIBLE);
        holder.ivAlarmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onMedicineDeleteClicked(medicineAlarm);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (medicineAlarmList != null && !medicineAlarmList.isEmpty()) ? medicineAlarmList.size() : 0;
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_med_time)
        RobotoBoldTextView tvMedTime;

        @BindView(R.id.tv_medicine_name)
        RobotoBoldTextView tvMedicineName;

        @BindView(R.id.tv_dose_details)
        RobotoRegularTextView tvDoseDetails;

        @BindView(R.id.iv_medicine_action)
        ImageView ivMedicineAction;

        @BindView(R.id.iv_alarm_delete)
        ImageView ivAlarmDelete;

        MedicineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface OnItemClickListener {
        void onMedicineDeleteClicked(MedicineAlarm medicineAlarm);
    }
}

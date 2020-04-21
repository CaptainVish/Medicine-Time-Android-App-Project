package com.gautam.medicinetime.medicine;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.gautam.medicinetime.addmedicine.AddMedicineActivity;
import com.gautam.medicinetime.alarm.ReminderActivity;
import com.gautam.medicinetime.alarm.ReminderFragment;
import com.gautam.medicinetime.data.source.MedicineAlarm;
import com.gautam.medicinetime.data.source.MedicineDataSource;
import com.gautam.medicinetime.data.source.MedicineRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by gautam on 13/07/17.
 */

public class MedicinePresenter implements MedicineContract.Presenter {

    private final MedicineRepository mMedicineRepository;

    private final MedicineContract.View mMedView;


    MedicinePresenter(@NonNull MedicineRepository medicineRepository, @NonNull MedicineContract.View medView) {
        this.mMedicineRepository = medicineRepository;
        this.mMedView = medView;
        medView.setPresenter(this);
    }

    @Override
    public void loadMedicinesByDay(int day, boolean showIndicator) {
        loadListByDay(day, showIndicator);
    }

    @Override
    public void deleteMedicineAlarm(MedicineAlarm medicineAlarm, Context context) {
        List<MedicineAlarm> alarms = mMedicineRepository.getAllAlarms(medicineAlarm.getPillName());
        for (MedicineAlarm alarm : alarms) {
            mMedicineRepository.deleteAlarm(alarm.getId());
            /** This intent invokes the activity ReminderActivity, which in turn opens the AlertAlarm window */
            Intent intent = new Intent(context, ReminderActivity.class);
            intent.putExtra(ReminderFragment.EXTRA_ID, alarm.getAlarmId());

            PendingIntent operation = PendingIntent.getActivity(context, alarm.getAlarmId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);


            /** Getting a reference to the System Service ALARM_SERVICE */
            AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(context).getSystemService(ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.cancel(operation);
            }
        }
        mMedView.showMedicineDeletedSuccessfully();
    }

    @Override
    public void start() {

    }

    @Override
    public void onStart(int day) {
        Log.d("TAG", "onStart: " + day);
        loadMedicinesByDay(day, true);
    }

    @Override
    public void reload(int day) {
        Log.d("TAG", "reload: " + day);
        loadListByDay(day, true);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        if (AddMedicineActivity.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {
            mMedView.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void addNewMedicine() {
        mMedView.showAddMedicine();
    }

    private void loadListByDay(int day, final boolean showLoadingUi) {
        if (showLoadingUi)
            mMedView.showLoadingIndicator(true);

        mMedicineRepository.getMedicineListByDay(day, new MedicineDataSource.LoadMedicineCallbacks() {
            @Override
            public void onMedicineLoaded(List<MedicineAlarm> medicineAlarmList) {
                processMedicineList(medicineAlarmList);
                // The view may not be able to handle UI updates anymore
                if (!mMedView.isActive()) {
                    return;
                }
                if (showLoadingUi) {
                    mMedView.showLoadingIndicator(false);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (!mMedView.isActive()) {
                    return;
                }
                if (showLoadingUi) {
                    mMedView.showLoadingIndicator(false);
                }

                mMedView.showNoMedicine();
            }
        });
    }

    private void processMedicineList(List<MedicineAlarm> medicineAlarmList) {

        if (medicineAlarmList.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            mMedView.showNoMedicine();
        } else {
            //Show the list of Medicines
            Collections.sort(medicineAlarmList);
            mMedView.showMedicineList(medicineAlarmList);
        }
    }

}

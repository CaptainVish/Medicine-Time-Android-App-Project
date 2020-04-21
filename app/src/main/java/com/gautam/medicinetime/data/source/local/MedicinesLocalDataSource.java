package com.gautam.medicinetime.data.source.local;

import android.content.Context;

import com.gautam.medicinetime.data.source.History;
import com.gautam.medicinetime.data.source.MedicineAlarm;
import com.gautam.medicinetime.data.source.MedicineDataSource;
import com.gautam.medicinetime.data.source.Pills;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by gautam on 13/07/17.
 */

public class MedicinesLocalDataSource implements MedicineDataSource {

    private static MedicinesLocalDataSource mInstance;

    private MedicineDBHelper mDbHelper;


    private MedicinesLocalDataSource(Context context) {
        mDbHelper = new MedicineDBHelper(context);
    }

    public static MedicinesLocalDataSource getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MedicinesLocalDataSource(context);
        }
        return mInstance;
    }


    @Override
    public void getMedicineHistory(LoadHistoryCallbacks loadHistoryCallbacks) {
        List<History> historyList = mDbHelper.getHistory();
        loadHistoryCallbacks.onHistoryLoaded(historyList);
    }

    @Override
    public void getMedicineAlarmById(long id, GetTaskCallback callback) {

        try {
            MedicineAlarm medicineAlarm = getAlarmById(id);
            if (medicineAlarm != null) {
                callback.onTaskLoaded(medicineAlarm);
            } else {
                callback.onDataNotAvailable();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            callback.onDataNotAvailable();
        }

    }

    @Override
    public void saveMedicine(MedicineAlarm medicineAlarm, Pills pill) {
        mDbHelper.createAlarm(medicineAlarm, pill.getPillId());
    }

    @Override
    public void getMedicineListByDay(int day, LoadMedicineCallbacks callbacks) {
        List<MedicineAlarm> medicineAlarmList = mDbHelper.getAlarmsByDay(day);
        callbacks.onMedicineLoaded(medicineAlarmList);
    }

    @Override
    public boolean medicineExits(String pillName) {
        for (Pills pill : getPills()) {
            if (pill.getPillName().equals(pillName))
                return true;
        }
        return false;
    }

    @Override
    public List<Long> tempIds() {
        return null;
    }

    @Override
    public void deleteAlarm(long alarmId) {
        deleteAlarmById(alarmId);
    }

    @Override
    public List<MedicineAlarm> getMedicineByPillName(String pillName) {
        try {
            return getMedicineByPill(pillName);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MedicineAlarm> getAllAlarms(String pillName) {
        try {
            return getAllAlarmsByName(pillName);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Pills getPillsByName(String pillName) {
        return getPillByName(pillName);
    }

    @Override
    public long savePills(Pills pills) {
        return savePill(pills);
    }

    @Override
    public void saveToHistory(History history) {
        mDbHelper.createHistory(history);
    }

    private List<Pills> getPills() {
        return mDbHelper.getAllPills();
    }

    private long savePill(Pills pill) {
        long pillId = mDbHelper.createPill(pill);
        pill.setPillId(pillId);
        return pillId;
    }

    private Pills getPillByName(String pillName) {
        return mDbHelper.getPillByName(pillName);
    }

    private List<MedicineAlarm> getMedicineByPill(String pillName) throws URISyntaxException {
        return mDbHelper.getAllAlarmsByPill(pillName);
    }

    private List<MedicineAlarm> getAllAlarmsByName(String pillName) throws URISyntaxException {
        return mDbHelper.getAllAlarms(pillName);
    }

    public void deletePill(String pillName) throws URISyntaxException {
        mDbHelper.deletePill(pillName);
    }

    private void deleteAlarmById(long alarmId) {
        mDbHelper.deleteAlarm(alarmId);
    }

    public void addToHistory(History h) {
        mDbHelper.createHistory(h);
    }

    public List<History> getHistory() {
        return mDbHelper.getHistory();
    }

    private MedicineAlarm getAlarmById(long alarm_id) throws URISyntaxException {
        return mDbHelper.getAlarmById(alarm_id);
    }

    public int getDayOfWeek(long alarm_id) throws URISyntaxException {
        return mDbHelper.getDayOfWeek(alarm_id);
    }


}

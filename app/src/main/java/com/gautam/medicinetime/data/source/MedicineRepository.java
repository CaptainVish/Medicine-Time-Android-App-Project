package com.gautam.medicinetime.data.source;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by gautam on 13/07/17.
 */

public class MedicineRepository implements MedicineDataSource {

    private static MedicineRepository mInstance = null;

    private final MedicineDataSource localDataSource;


    private MedicineRepository(@NonNull MedicineDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }


    public static MedicineRepository getInstance(MedicineDataSource localDataSource) {
        if (mInstance == null) {
            mInstance = new MedicineRepository(localDataSource);
        }
        return mInstance;
    }

    @Override
    public void getMedicineHistory(final LoadHistoryCallbacks loadHistoryCallbacks) {
        localDataSource.getMedicineHistory(new LoadHistoryCallbacks() {
            @Override
            public void onHistoryLoaded(List<History> historyList) {
                loadHistoryCallbacks.onHistoryLoaded(historyList);
            }

            @Override
            public void onDataNotAvailable() {
                loadHistoryCallbacks.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getMedicineAlarmById(long id, final GetTaskCallback callback) {
        localDataSource.getMedicineAlarmById(id, new GetTaskCallback() {
            @Override
            public void onTaskLoaded(MedicineAlarm medicineAlarm) {
                if (medicineAlarm == null) {
                    return;
                }
                callback.onTaskLoaded(medicineAlarm);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void saveMedicine(MedicineAlarm medicineAlarm, Pills pills) {
        localDataSource.saveMedicine(medicineAlarm, pills);
    }


    @Override
    public void getMedicineListByDay(int day, final LoadMedicineCallbacks callbacks) {
        localDataSource.getMedicineListByDay(day, new LoadMedicineCallbacks() {
            @Override
            public void onMedicineLoaded(List<MedicineAlarm> medicineAlarmList) {
                callbacks.onMedicineLoaded(medicineAlarmList);
            }

            @Override
            public void onDataNotAvailable() {
                callbacks.onDataNotAvailable();
            }
        });
    }

    @Override
    public boolean medicineExits(String pillName) {
        return false;
    }

    @Override
    public List<Long> tempIds() {
        return null;
    }

    @Override
    public void deleteAlarm(long alarmId) {
        localDataSource.deleteAlarm(alarmId);
    }

    @Override
    public List<MedicineAlarm> getMedicineByPillName(String pillName) {
        return localDataSource.getMedicineByPillName(pillName);
    }

    @Override
    public Pills getPillsByName(String pillName) {
        return localDataSource.getPillsByName(pillName);
    }

    @Override
    public long savePills(Pills pills) {
        return localDataSource.savePills(pills);
    }

    @Override
    public void saveToHistory(History history) {
        localDataSource.saveToHistory(history);
    }
}

package com.gautam.medicinetime.data;


import android.support.annotation.VisibleForTesting;

import com.gautam.medicinetime.data.source.History;
import com.gautam.medicinetime.data.source.MedicineAlarm;
import com.gautam.medicinetime.data.source.MedicineDataSource;
import com.gautam.medicinetime.data.source.Pills;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by gautam on 13/05/17.
 */

public class FakeMedicineLocalDataSource implements MedicineDataSource {

    private static FakeMedicineLocalDataSource INSTANCE;

    private static final Map<String, MedicineAlarm> MEDICINE_SERVICE_DATA;

    private static final Map<String, History> HISTORY_SERVICE_DATA;

    private static final Map<String, Pills> PILLS_SERVICE_DATA;


    //Prevent from direct Instantiation
    private FakeMedicineLocalDataSource() {

    }

    static {
        MEDICINE_SERVICE_DATA = new LinkedHashMap<>();
        HISTORY_SERVICE_DATA = new LinkedHashMap<>();
        PILLS_SERVICE_DATA = new LinkedHashMap<>();

        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);

        Date date = mCurrentTime.getTime();
        String dateString = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(date);

        addPills("Paracetamol", 1);
        addPills("Crocin", 2);

        addMedicine(1, hour, minute, "Paracetamol", "1.0", "tablet(s)");
        addMedicine(2, hour + 2, minute + 1, "Crocin", "2.0", "capsule(s)");

        addHistory(hour, minute, dateString, "Crocin", 2, "2.0", "capsule(s)");
        addHistory(hour + 2, minute + 1, dateString, "Paracetamol", 1, "1.0", "tablet(s)");
    }


    private static void addMedicine(long id, int hour, int minute, String pillName, String doseQuantity, String doseUnit) {
        MedicineAlarm medicineAlarm = new MedicineAlarm(id, hour, minute, pillName, doseQuantity, doseUnit);
        MEDICINE_SERVICE_DATA.put(String.valueOf(id), medicineAlarm);
    }

    private static void addHistory(int hourTaken, int minuteTaken, String dateString, String pillName, int action, String doseQuantity, String doseUnit) {
        History history = new History(hourTaken, minuteTaken, dateString, pillName, action, doseQuantity, doseUnit);
        HISTORY_SERVICE_DATA.put(pillName, history);
    }

    private static void addPills(String pillName, long pillId) {
        Pills pills = new Pills(pillName, pillId);
        PILLS_SERVICE_DATA.put(String.valueOf(pillId), pills);
    }

    public static FakeMedicineLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeMedicineLocalDataSource();
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public void addMedicines(MedicineAlarm... medicineAlarms) {
        for (MedicineAlarm medicineAlarm : medicineAlarms) {
            MEDICINE_SERVICE_DATA.put(String.valueOf(medicineAlarm.getId()), medicineAlarm);
        }
    }

    public void addMedicine(MedicineAlarm... medicineAlarms) {
        for (MedicineAlarm medicineAlarm : medicineAlarms) {
            MEDICINE_SERVICE_DATA.put(String.valueOf(medicineAlarm.getId()), medicineAlarm);
        }
    }

    @Override
    public void getMedicineHistory(LoadHistoryCallbacks loadHistoryCallbacks) {
        loadHistoryCallbacks.onHistoryLoaded(new ArrayList<History>(HISTORY_SERVICE_DATA.values()));
    }

    @Override
    public void getMedicineAlarmById(long id, GetTaskCallback callback) {
        callback.onTaskLoaded(MEDICINE_SERVICE_DATA.get(String.valueOf(id)));
    }

    @Override
    public void saveMedicine(MedicineAlarm medicineAlarm, Pills pills) {
        medicineAlarm.addId(pills.getPillId());
        MEDICINE_SERVICE_DATA.put(String.valueOf(pills.getPillId()), medicineAlarm);
    }

    @Override
    public void getMedicineListByDay(int day, LoadMedicineCallbacks callbacks) {
        callbacks.onMedicineLoaded(new ArrayList<>(MEDICINE_SERVICE_DATA.values()));
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
        MEDICINE_SERVICE_DATA.remove(String.valueOf(alarmId));
    }

    @Override
    public List<MedicineAlarm> getMedicineByPillName(String pillName) {
        List<MedicineAlarm> medicineAlarms = new ArrayList<>();
        for (Map.Entry<String, MedicineAlarm> entry : MEDICINE_SERVICE_DATA.entrySet()) {
            MedicineAlarm medicineAlarm = entry.getValue();
            if (medicineAlarm.getPillName().equalsIgnoreCase(pillName)) {
                medicineAlarms.add(medicineAlarm);
            }
        }
        return medicineAlarms;
    }

    @Override
    public Pills getPillsByName(String pillName) {
        for (Map.Entry<String, Pills> entry : PILLS_SERVICE_DATA.entrySet()) {
            Pills pills = entry.getValue();
            if (pills.getPillName().equalsIgnoreCase(pillName)) {
                return pills;
            }
        }
        return null;
    }

    @Override
    public long savePills(Pills pills) {
        PILLS_SERVICE_DATA.put(String.valueOf(pills.getPillId()), pills);
        return pills.getPillId();
    }

    @Override
    public void saveToHistory(History history) {
        HISTORY_SERVICE_DATA.put(String.valueOf(history.getPillName()), history);
    }
}

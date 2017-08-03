package com.gautam.medicinetime.data.source;

import com.gautam.medicinetime.data.source.MedicineAlarm;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gautam on 13/07/17.
 */

public class Pills {

    private String pillName;

    private long pillId;

    private List<MedicineAlarm> medicineAlarms = new LinkedList<MedicineAlarm>();

    public Pills(){

    }

    public Pills(String pillName, long pillId) {
        this.pillName = pillName;
        this.pillId = pillId;
    }

    public String getPillName() { return pillName; }

    public void setPillName(String pillName) { this.pillName = pillName; }

    /**
     *
     * @param medicineAlarm
     * allows a new medicineAlarm sto be added to a preexisting medicineAlarm
     */
    public void addAlarm(MedicineAlarm medicineAlarm) {
        medicineAlarms.add(medicineAlarm);
        Collections.sort(medicineAlarms);
    }

    public long getPillId() {
        return pillId;
    }

    public void setPillId(long pillID) {
        this.pillId = pillID;
    }
}

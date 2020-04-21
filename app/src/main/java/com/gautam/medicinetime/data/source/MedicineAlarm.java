package com.gautam.medicinetime.data.source;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by gautam on 12/07/17.
 */

public class MedicineAlarm implements Comparable<MedicineAlarm> {

    private long id;  // DB id number

    private int hour; //

    private int minute;

    private String pillName;

    private String doseQuantity;

    private String doseUnit;

    private String dateString;

    private  int alarmId;

    public MedicineAlarm() {

    }

    public MedicineAlarm(long id, int hour, int minute, String pillName, String doseQuantity, String doseUnit, int alarmId) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.pillName = pillName;
        this.doseQuantity = doseQuantity;
        this.doseUnit = doseUnit;
        this.alarmId = alarmId;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getDoseQuantity() {
        return doseQuantity;
    }

    public String getDoseUnit() {
        return doseUnit;
    }

    public void setDoseQuantity(String doseQuantity) {
        this.doseQuantity = doseQuantity;
    }

    public void setDoseUnit(String doseUnit) {
        this.doseUnit = doseUnit;
    }

    private List<Long> ids = new LinkedList<Long>();

    private boolean[] dayOfWeek = new boolean[7];

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public List<Long> getIds() {
        return Collections.unmodifiableList(ids);
    }

    public boolean[] getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(boolean[] dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void addId(long id) {
        ids.add(id);
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    private String getAm_pm() {
        return (hour < 12) ? "am" : "pm";
    }

    public String getPillName() {
        return pillName;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    /**
     * Overrides the compareTo() method so that alarms can be sorted by time of day from earliest to
     * latest.
     */
    @Override
    public int compareTo(@NonNull MedicineAlarm medicineAlarm) {
        if (hour < medicineAlarm.getHour())
            return -1;
        else if (hour > medicineAlarm.getHour())
            return 1;
        else {
            if (minute < medicineAlarm.getMinute())
                return -1;
            else if (minute > medicineAlarm.getMinute())
                return 1;
            else
                return 0;
        }
    }

    /**
     * A helper method which returns the time of the alarm in string form
     * hour:minutes am/pm
     */
    public String getStringTime() {
        int nonMilitaryHour = hour % 12;
        if (nonMilitaryHour == 0)
            nonMilitaryHour = 12;
        String min = Integer.toString(minute);
        if (minute < 10)
            min = "0" + minute;
        return String.format(Locale.getDefault(), "%d:%s %s", nonMilitaryHour, min, getAm_pm());
    }

    /**
     * A helper method which returns the formatted medicine dose
     * doseQuantity doseUnit
     */
    public String getFormattedDose() {
        return String.format(Locale.getDefault(), "%s %s", doseQuantity, doseUnit);
    }

}

package com.gautam.medicinetime.data.source;

import java.util.Locale;

/**
 * Created by gautam on 13/07/17.
 */

public class History {

    private int hourTaken;

    private int minuteTaken;

    private String dateString;

    private String pillName;

    private int action;

    private String doseQuantity;

    private String doseUnit;

    public History() {
    }

    public History(int hourTaken, int minuteTaken, String dateString, String pillName, int action, String doseQuantity, String doseUnit) {
        this.hourTaken = hourTaken;
        this.minuteTaken = minuteTaken;
        this.dateString = dateString;
        this.pillName = pillName;
        this.action = action;
        this.doseQuantity = doseQuantity;
        this.doseUnit = doseUnit;
    }

    public void setDoseUnit(String doseUnit) {
        this.doseUnit = doseUnit;
    }

    public void setDoseQuantity(String doseQuantity) {
        this.doseQuantity = doseQuantity;
    }

    public String getDoseUnit() {
        return doseUnit;
    }

    public String getDoseQuantity() {
        return doseQuantity;
    }

    public int getHourTaken() {
        return hourTaken;
    }

    public void setHourTaken(int hourTaken) {
        this.hourTaken = hourTaken;
    }

    public int getMinuteTaken() {
        return minuteTaken;
    }

    public void setMinuteTaken(int minuteTaken) {
        this.minuteTaken = minuteTaken;
    }

    public String getAm_pmTaken() {
        return (hourTaken < 12) ? "am" : "pm";
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getPillName() {
        return pillName;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    /**
     * A helper method which returns the time of the alarm in string form
     * hour:minutes am/pm
     */
    public String getStringTime() {
        int nonMilitaryHour = hourTaken % 12;
        if (nonMilitaryHour == 0)
            nonMilitaryHour = 12;
        String min = Integer.toString(minuteTaken);
        if (minuteTaken < 10)
            min = "0" + minuteTaken;
        return String.format(Locale.getDefault(), "%d:%s %s", nonMilitaryHour, min, getAm_pmTaken());
    }

    public String getFormattedDate() {
        return String.format(Locale.getDefault(), "%s %s", dateString, getStringTime());
    }

    /**
     * A helper method which returns the formatted medicine dose
     * doseQuantity doseUnit
     */
    public String getFormattedDose() {
        return String.format(Locale.getDefault(), "%s %s", doseQuantity, doseUnit);
    }
}

package com.gautam.medicinetime.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gautam.medicinetime.data.source.History;
import com.gautam.medicinetime.data.source.MedicineAlarm;
import com.gautam.medicinetime.data.source.Pills;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by gautam on 13/07/17.
 */

public class MedicineDBHelper extends SQLiteOpenHelper {

    /**
     * Database name
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Database version
     */
    private static final String DATABASE_NAME = "MedicineAlarm.db";

    /**
     * Table names
     */
    private static final String PILL_TABLE = "pills";
    private static final String ALARM_TABLE = "alarms";
    private static final String PILL_ALARM_LINKS = "pill_alarm";
    private static final String HISTORIES_TABLE = "histories";

    /**
     * Common column name and location
     */
    public static final String KEY_ROWID = "id";

    /**
     * Pill table columns, used by History Table
     */
    private static final String KEY_PILLNAME = "pillName";

    /**
     * Alarm table columns, Hour & Minute used by History Table
     */
    private static final String KEY_INTENT = "intent";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";
    private static final String KEY_DAY_WEEK = "day_of_week";
    private static final String KEY_ALARMS_PILL_NAME = "pillName";
    private static final String KEY_DOSE_QUANTITY = "dose_quantity";
    private static final String KEY_DOSE_UNITS = "dose_units";


    /**
     * Pill-Alarm link table columns
     */
    private static final String KEY_PILLTABLE_ID = "pill_id";
    private static final String KEY_ALARMTABLE_ID = "alarm_id";

    /**
     * History Table columns, some used above
     */
    private static final String KEY_DATE_STRING = "date";
    private static final String KEY_ACTION = "action";

    /**
     * Pill Table: create statement
     */
    private static final String CREATE_PILL_TABLE =
            "create table " + PILL_TABLE + "("
                    + KEY_ROWID + " integer primary key not null,"
                    + KEY_PILLNAME + " text not null" + ")";

    /**
     * Alarm Table: create statement
     */
    private static final String CREATE_ALARM_TABLE =
            "create table " + ALARM_TABLE + "("
                    + KEY_ROWID + " integer primary key,"
                    + KEY_INTENT + " text,"
                    + KEY_HOUR + " integer,"
                    + KEY_MINUTE + " integer,"
                    + KEY_ALARMS_PILL_NAME + " text not null,"
                    + KEY_DATE_STRING + " text,"
                    + KEY_DOSE_QUANTITY + " text,"
                    + KEY_DOSE_UNITS + " text,"
                    + KEY_DAY_WEEK + " integer" + ")";

    /**
     * Pill-Alarm link table: create statement
     */
    private static final String CREATE_PILL_ALARM_LINKS_TABLE =
            "create table " + PILL_ALARM_LINKS + "("
                    + KEY_ROWID + " integer primary key not null,"
                    + KEY_PILLTABLE_ID + " integer not null,"
                    + KEY_ALARMTABLE_ID + " integer not null" + ")";

    /**
     * Histories Table: create statement
     */
    private static final String CREATE_HISTORIES_TABLE =
            "CREATE TABLE " + HISTORIES_TABLE + "("
                    + KEY_ROWID + " integer primary key, "
                    + KEY_PILLNAME + " text not null, "
                    + KEY_DOSE_QUANTITY + " text,"
                    + KEY_DOSE_UNITS + " text,"
                    + KEY_DATE_STRING + " text, "
                    + KEY_HOUR + " integer, "
                    + KEY_ACTION + " integer, "
                    + KEY_MINUTE + " integer " + ")";

    /**
     * Constructor
     */
    public MedicineDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    /** Creating tables */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PILL_TABLE);
        db.execSQL(CREATE_ALARM_TABLE);
        db.execSQL(CREATE_PILL_ALARM_LINKS_TABLE);
        db.execSQL(CREATE_HISTORIES_TABLE);
    }

    @Override
    // TODO: change this so that updating doesn't delete old data
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PILL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PILL_ALARM_LINKS);
        db.execSQL("DROP TABLE IF EXISTS " + HISTORIES_TABLE);
        onCreate(db);
    }


// ############################## create methods ###################################### //


    /**
     * createPill takes a pill object and inserts the relevant data into the database
     *
     * @param pill a model pill object
     * @return the long row_id generate by the database upon entry into the database
     */
    public long createPill(Pills pill) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PILLNAME, pill.getPillName());
        return db.insert(PILL_TABLE, null, values);
    }

    /**
     * takes in a model alarm object and inserts a row into the database
     * for each day of the week the alarm is meant to go off.
     *
     * @param alarm   a model alarm object
     * @param pill_id the id associated with the pill the alarm is for
     * @return a array of longs that are the row_ids generated by the database when the rows are inserted
     */
    public long[] createAlarm(MedicineAlarm alarm, long pill_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long[] alarm_ids = new long[7];

        /** Create a separate row in the table for every day of the week for this alarm */
        int arrayPos = 0;
        for (boolean day : alarm.getDayOfWeek()) {
            if (day) {
                ContentValues values = new ContentValues();
                values.put(KEY_HOUR, alarm.getHour());
                values.put(KEY_MINUTE, alarm.getMinute());
                values.put(KEY_DAY_WEEK, arrayPos + 1);
                values.put(KEY_ALARMS_PILL_NAME, alarm.getPillName());
                values.put(KEY_DOSE_QUANTITY, alarm.getDoseQuantity());
                values.put(KEY_DOSE_UNITS, alarm.getDoseUnit());
                values.put(KEY_DATE_STRING, alarm.getDateString());

                /** Insert row */
                long alarm_id = db.insert(ALARM_TABLE, null, values);
                alarm_ids[arrayPos] = alarm_id;

                /** Link alarm to a pill */
                createPillAlarmLink(pill_id, alarm_id);
            }
            arrayPos++;
        }
        return alarm_ids;
    }

    /**
     * private function that inserts a row into a table that links pills and alarms
     *
     * @param pill_id  the row_id of the pill that is being added to or edited
     * @param alarm_id the row_id of the alarm that is being added to the pill
     * @return returns the row_id the database creates when a row is created
     */
    private long createPillAlarmLink(long pill_id, long alarm_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PILLTABLE_ID, pill_id);
        values.put(KEY_ALARMTABLE_ID, alarm_id);
        return db.insert(PILL_ALARM_LINKS, null, values);
    }

    /**
     * uses a history model object to store histories in the DB
     *
     * @param history a history model object
     */
    public void createHistory(History history) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PILLNAME, history.getPillName());
        values.put(KEY_DATE_STRING, history.getDateString());
        values.put(KEY_HOUR, history.getHourTaken());
        values.put(KEY_MINUTE, history.getMinuteTaken());
        values.put(KEY_DOSE_QUANTITY, history.getDoseQuantity());
        values.put(KEY_DOSE_UNITS, history.getDoseUnit());
        values.put(KEY_ACTION, history.getAction());

        /** Insert row */
        db.insert(HISTORIES_TABLE, null, values);
    }

// ############################# get methods ####################################### //

    /**
     * allows pillBox to retrieve a row from pill table in Db
     *
     * @param pillName takes in a string of the pill Name
     * @return returns a pill model object
     */
    public Pills getPillByName(String pillName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String dbPill = "select * from "
                + PILL_TABLE + " where "
                + KEY_PILLNAME + " = "
                + "'" + pillName + "'";

        Cursor c = db.rawQuery(dbPill, null);

        Pills pill = new Pills();

        if (c.moveToFirst() && c.getCount() >= 1) {
            pill.setPillName(c.getString(c.getColumnIndex(KEY_PILLNAME)));
            pill.setPillId(c.getLong(c.getColumnIndex(KEY_ROWID)));
            c.close();
        }
        return pill;
    }

    /**
     * allows the pillBox to retrieve all the pill rows from database
     *
     * @return a list of pill model objects
     */
    public List<Pills> getAllPills() {
        List<Pills> pills = new ArrayList<>();
        String dbPills = "SELECT * FROM " + PILL_TABLE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(dbPills, null);

        /** Loops through all rows, adds to list */
        if (c.moveToFirst()) {
            do {
                Pills p = new Pills();
                p.setPillName(c.getString(c.getColumnIndex(KEY_PILLNAME)));
                p.setPillId(c.getLong(c.getColumnIndex(KEY_ROWID)));

                pills.add(p);
            } while (c.moveToNext());
        }
        c.close();
        return pills;
    }


    /**
     * Allows pillBox to retrieve all Alarms linked to a Pill
     * uses combineAlarms helper method
     *
     * @param pillName string
     * @return list of alarm objects
     * @throws URISyntaxException honestly do not know why, something about alarm.getDayOfWeek()
     */
    public List<MedicineAlarm> getAllAlarmsByPill(String pillName) throws URISyntaxException {
        List<MedicineAlarm> alarmsByPill = new ArrayList<>();

        /** HINT: When reading string: '.' are not periods ex) pill.rowIdNumber */
        String selectQuery = "SELECT * FROM " +
                ALARM_TABLE + " alarm, " +
                PILL_TABLE + " pill, " +
                PILL_ALARM_LINKS + " pillAlarm WHERE " +
                "pill." + KEY_PILLNAME + " = '" + pillName + "'" +
                " AND pill." + KEY_ROWID + " = " +
                "pillAlarm." + KEY_PILLTABLE_ID +
                " AND alarm." + KEY_ROWID + " = " +
                "pillAlarm." + KEY_ALARMTABLE_ID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                MedicineAlarm al = new MedicineAlarm();
                al.setId(c.getInt(c.getColumnIndex(KEY_ROWID)));
                al.setHour(c.getInt(c.getColumnIndex(KEY_HOUR)));
                al.setMinute(c.getInt(c.getColumnIndex(KEY_MINUTE)));
                al.setPillName(c.getString(c.getColumnIndex(KEY_ALARMS_PILL_NAME)));
                al.setDoseQuantity(c.getString(c.getColumnIndex(KEY_DOSE_QUANTITY)));
                al.setDoseUnit(c.getString(c.getColumnIndex(KEY_DOSE_UNITS)));
                al.setDateString(c.getString(c.getColumnIndex(KEY_DATE_STRING)));

                alarmsByPill.add(al);
            } while (c.moveToNext());
        }

        c.close();


        return combineAlarms(alarmsByPill);
    }

    /**
     * returns all individual alarms that occur on a certain day of the week,
     * alarms returned do not know of their counterparts that occur on different days
     *
     * @param day an integer that represents the day of week
     * @return a list of Alarms (not combined into full-model-alarms)
     */
    public List<MedicineAlarm> getAlarmsByDay(int day) {
        List<MedicineAlarm> daysAlarms = new ArrayList<>();

        String selectQuery = "SELECT * FROM " +
                ALARM_TABLE + " alarm WHERE " +
                "alarm." + KEY_DAY_WEEK +
                " = '" + day + "'";

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                MedicineAlarm al = new MedicineAlarm();
                al.setId(c.getInt(c.getColumnIndex(KEY_ROWID)));
                al.setHour(c.getInt(c.getColumnIndex(KEY_HOUR)));
                al.setMinute(c.getInt(c.getColumnIndex(KEY_MINUTE)));
                al.setPillName(c.getString(c.getColumnIndex(KEY_ALARMS_PILL_NAME)));
                al.setDoseQuantity(c.getString(c.getColumnIndex(KEY_DOSE_QUANTITY)));
                al.setDoseUnit(c.getString(c.getColumnIndex(KEY_DOSE_UNITS)));
                al.setDateString(c.getString(c.getColumnIndex(KEY_DATE_STRING)));

                daysAlarms.add(al);
            } while (c.moveToNext());
        }
        c.close();

        return daysAlarms;
    }

    /**
     * @param alarm_id
     * @return
     * @throws URISyntaxException
     */
    public MedicineAlarm getAlarmById(long alarm_id) throws URISyntaxException {

        String dbAlarm = "SELECT * FROM " +
                ALARM_TABLE + " WHERE " +
                KEY_ROWID + " = " + alarm_id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(dbAlarm, null);

        if (c != null)
            c.moveToFirst();

        MedicineAlarm al = new MedicineAlarm();
        al.setId(c.getInt(c.getColumnIndex(KEY_ROWID)));
        al.setHour(c.getInt(c.getColumnIndex(KEY_HOUR)));
        al.setMinute(c.getInt(c.getColumnIndex(KEY_MINUTE)));
        al.setPillName(c.getString(c.getColumnIndex(KEY_ALARMS_PILL_NAME)));
        al.setDoseQuantity(c.getString(c.getColumnIndex(KEY_DOSE_QUANTITY)));
        al.setDoseUnit(c.getString(c.getColumnIndex(KEY_DOSE_UNITS)));
        al.setDateString(c.getString(c.getColumnIndex(KEY_DATE_STRING)));

        c.close();

        return al;
    }

    /**
     * Private helper function that combines rows in the databse back into a
     * full model-alarm with a dayOfWeek array.
     *
     * @param dbAlarms a list of dbAlarms (not-full-alarms w/out day of week info)
     * @return a list of model-alarms
     * @throws URISyntaxException
     */
    private List<MedicineAlarm> combineAlarms(List<MedicineAlarm> dbAlarms) throws URISyntaxException {
        List<String> timesOfDay = new ArrayList<>();
        List<MedicineAlarm> combinedAlarms = new ArrayList<>();

        for (MedicineAlarm al : dbAlarms) {
            if (timesOfDay.contains(al.getStringTime())) {
                /** Add this db row to alarm object */
                for (MedicineAlarm ala : combinedAlarms) {
                    if (ala.getStringTime().equals(al.getStringTime())) {
                        int day = getDayOfWeek(al.getId());
                        boolean[] days = ala.getDayOfWeek();
                        days[day - 1] = true;
                        ala.setDayOfWeek(days);
                        ala.addId(al.getId());
                    }
                }
            } else {
                /** Create new Alarm object with day of week array */
                MedicineAlarm newAlarm = new MedicineAlarm();
                boolean[] days = new boolean[7];

                newAlarm.setPillName(al.getPillName());
                newAlarm.setMinute(al.getMinute());
                newAlarm.setHour(al.getHour());
                newAlarm.addId(al.getId());
                newAlarm.setDateString(al.getDateString());

                int day = getDayOfWeek(al.getId());
                days[day - 1] = true;
                newAlarm.setDayOfWeek(days);

                timesOfDay.add(al.getStringTime());
                combinedAlarms.add(newAlarm);
            }
        }

        Collections.sort(combinedAlarms);
        return combinedAlarms;
    }

    /**
     * Get a single pillapp.Model-Alarm
     * Used as a helper function
     */
    public int getDayOfWeek(long alarm_id) throws URISyntaxException {
        SQLiteDatabase db = this.getReadableDatabase();

        String dbAlarm = "SELECT * FROM " +
                ALARM_TABLE + " WHERE " +
                KEY_ROWID + " = " + alarm_id;

        Cursor c = db.rawQuery(dbAlarm, null);

        if (c != null)
            c.moveToFirst();

        int dayOfWeek = c.getInt(c.getColumnIndex(KEY_DAY_WEEK));
        c.close();

        return dayOfWeek;
    }

    /**
     * allows pillBox to retrieve from History table
     *
     * @return a list of all history objects
     */
    public List<History> getHistory() {
        List<History> allHistory = new ArrayList<>();
        String dbHist = "SELECT * FROM " + HISTORIES_TABLE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(dbHist, null);

        if (c.moveToFirst()) {
            do {
                History h = new History();
                h.setPillName(c.getString(c.getColumnIndex(KEY_PILLNAME)));
                h.setDateString(c.getString(c.getColumnIndex(KEY_DATE_STRING)));
                h.setHourTaken(c.getInt(c.getColumnIndex(KEY_HOUR)));
                h.setMinuteTaken(c.getInt(c.getColumnIndex(KEY_MINUTE)));
                h.setDoseQuantity(c.getString(c.getColumnIndex(KEY_DOSE_QUANTITY)));
                h.setDoseUnit(c.getString(c.getColumnIndex(KEY_DOSE_UNITS)));
                h.setAction(c.getInt(c.getColumnIndex(KEY_ACTION)));

                allHistory.add(h);
            } while (c.moveToNext());
        }
        c.close();
        return allHistory;
    }


// ############################### delete methods##################################### //


    private void deletePillAlarmLinks(long alarmId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PILL_ALARM_LINKS, KEY_ALARMTABLE_ID
                + " = ?", new String[]{String.valueOf(alarmId)});
    }

    public void deleteAlarm(long alarmId) {
        SQLiteDatabase db = this.getWritableDatabase();

        /** First delete any link in PillAlarmLink Table */
        deletePillAlarmLinks(alarmId);

        /* Then delete alarm */
        db.delete(ALARM_TABLE, KEY_ROWID
                + " = ?", new String[]{String.valueOf(alarmId)});
    }

    public void deletePill(String pillName) throws URISyntaxException {
        SQLiteDatabase db = this.getWritableDatabase();
        List<MedicineAlarm> pillsAlarms;

        /** First get all Alarms and delete them and their Pill-links */
        pillsAlarms = getAllAlarmsByPill(pillName);
        for (MedicineAlarm alarm : pillsAlarms) {
            long id = alarm.getId();
            deleteAlarm(id);
        }

        /** Then delete Pill */
        db.delete(PILL_TABLE, KEY_PILLNAME
                + " = ?", new String[]{pillName});
    }
}

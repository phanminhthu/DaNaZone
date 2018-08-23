package com.example.danazone04.danazone.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.danazone04.danazone.bean.Run;

import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "run_list";
    private static final String TABLE_NAME = "run";
    private static final String ID = "id";
    private static final String TIME = "time";
    private static final String DATE = "date";
    private static final String SPEED = "speed";
    private static final String DISTANCE = "distance";
    private static final String CALO = "calo";
    private static final String TIME_START = "time_start";

    private Context context;

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + TABLE_NAME + "(" + ID +
                " integer primary key, " +
                TIME + " TEXT, " +
                DATE + " TEXT, " +
                SPEED + " TEXT, " +
                DISTANCE + " TEXT, " +
                CALO + " TEXT, " +
                TIME_START + " TEXT)";
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Add new a student
    public void addHistory(Run run) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME, run.getTime());
        values.put(DATE, run.getDate());
        values.put(SPEED, run.getSpeed());
        values.put(DISTANCE, run.getDistance());
        values.put(CALO, run.getCalo());
        values.put(TIME_START, run.getTimeStart());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    /*
     Getting All Student
      */

    public List<Run> getAllHistory() {
        List<Run> listStudent = new ArrayList<Run>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Run run = new Run();
                run.setId(cursor.getInt(0));
                run.setTime(cursor.getString(1));
                run.setDate(cursor.getString(2));
                run.setSpeed(cursor.getString(3));
                run.setDistance(cursor.getString(4));
                run.setCalo(cursor.getString(5));
                run.setTimeStart(cursor.getString(6));
                listStudent.add(run);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listStudent;
    }

    /*
    Delete a student by ID
     */
    public void deleteStudent(Run run) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?",
                new String[]{String.valueOf(run.getId())});
        db.close();
    }
}

package com.example.list6_crime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "crimeDB_JAVA.db";

    public static final String TABLE_CRIME = "crime";

    public static final String COLUMN_ID = "Id";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_SOLVE = "Solved";

    public DBHandler(Context context){
        super(context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STUDENTS_TABLE = "CREATE TABLE " +
                TABLE_CRIME +
                "(" +
                COLUMN_ID +
                " INTEGER PRIMARY KEY," +
                COLUMN_TITLE +
                " TEXT," +
                COLUMN_DATE +
                " INTEGER," +
                COLUMN_SOLVE +
                " INTEGER" +
                ")";

        db.execSQL(CREATE_STUDENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRIME);
        onCreate(db);
    }

    public Cursor getCrime() {
        String query = "SELECT * FROM " + TABLE_CRIME;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }

    public void addCrime(Crime crime){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, crime.getmTitle());
        values.put(COLUMN_DATE, crime.getmDate().getTime());
        values.put(COLUMN_SOLVE, crime.ismSolved());



        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_CRIME, null, values);

        db.close();

    }

    public  void deleteStudent(int id){
        String query = "SELECT * FROM " +
                TABLE_CRIME +
                " WHERE " +
                COLUMN_ID +
                "= \"" +
                id +
                "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToNext()) {
            int currentID = Integer.parseInt(cursor.getString(0));
            db.delete(TABLE_CRIME, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(currentID)});
            cursor.close();
        }
        cursor.close();
    }

    public void updateStudent(int id, String name, boolean solved, Date date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (name != null)
            contentValues.put(COLUMN_TITLE, name);
        if (date != null)
            contentValues.put(COLUMN_DATE, date.getTime());

        contentValues.put(COLUMN_SOLVE, solved);

        db.update(TABLE_CRIME,
                contentValues,
                COLUMN_ID + "=" + id,
                null);

        db.close();

    }


}

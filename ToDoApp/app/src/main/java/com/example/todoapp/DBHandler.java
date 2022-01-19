package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todoDB_JAVA.db";

    public static final String TABLE_TODO = "todo";

    public static final String COLUMN_ID = "Id";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_SOLVE = "Completed";

    private static final String KEY_IMAGE = "Image";


    public DBHandler(Context context){
        super(context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STUDENTS_TABLE = "CREATE TABLE " +
                TABLE_TODO +
                "(" +
                COLUMN_ID +
                " INTEGER PRIMARY KEY," +
                COLUMN_TITLE +
                " TEXT," +
                COLUMN_DATE +
                " INTEGER," +
                COLUMN_SOLVE +
                " INTEGER," +
                KEY_IMAGE +
                " TEXT" +
                ")";

        db.execSQL(CREATE_STUDENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(db);
    }

    public Cursor getTask() {
        String query = "SELECT * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }

    public void addTask(Task task){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getmTitle());
        values.put(COLUMN_DATE, task.getmDate().getTime());
        values.put(COLUMN_SOLVE, task.ismSolved());
        values.put(KEY_IMAGE, task.getmPicture());



        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_TODO, null, values);

        db.close();

    }

    public  void deleteTask(int id){
        String query = "SELECT * FROM " +
                TABLE_TODO +
                " WHERE " +
                COLUMN_ID +
                "= \"" +
                id +
                "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToNext()) {
            int currentID = Integer.parseInt(cursor.getString(0));
            db.delete(TABLE_TODO, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(currentID)});
            cursor.close();
        }
        cursor.close();
    }

    public void updateTask(int id, String name, boolean solved, Date date, String picture){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (name != null)
            contentValues.put(COLUMN_TITLE, name);
        if (date != null)
            contentValues.put(COLUMN_DATE, date.getTime());

        contentValues.put(COLUMN_SOLVE, solved);

        if (picture != null)
            contentValues.put(KEY_IMAGE, picture);

        db.update(TABLE_TODO,
                contentValues,
                COLUMN_ID + "=" + id,
                null);

        db.close();

    }

}

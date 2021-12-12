package com.example.list6_crime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.*;


import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    Button delete;
    CheckBox checkBox;
    Button selectDate;
    Calendar date;
    EditText crime_Title;
    private DBHandler dbHandler;
    Intent intent;
    int currentCrimeId;
    String currentCrimeTitle;
    boolean currentCrimeIsSolved;
    String currentCrimeDate;



    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        crime_Title =findViewById(R.id.crime_title);;
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        selectDate = findViewById(R.id.btnDate);
        delete = findViewById(R.id.btnDelete);

        dbHandler = new DBHandler(this);

        intent = getIntent();
        currentCrimeId = intent.getIntExtra("id", 0);
        currentCrimeTitle = intent.getStringExtra("title");
        currentCrimeIsSolved = intent.getBooleanExtra("solved", false);
        currentCrimeDate = intent.getStringExtra("date");



        crime_Title.setText(currentCrimeTitle);
        selectDate.setText(currentCrimeDate);

        if (currentCrimeIsSolved)
            checkBox.setChecked(true);


        crime_Title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dbHandler.updateStudent(currentCrimeId, String.valueOf(s), true, null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler.updateStudent(currentCrimeId, null, ((CompoundButton) view).isChecked(), null);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.deleteStudent(currentCrimeId);
                finish();
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();
                date = Calendar.getInstance();
                new DatePickerDialog(DetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(DetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date.set(Calendar.MINUTE, minute);
                                dbHandler.updateStudent(currentCrimeId, null, currentCrimeIsSolved, date.getTime());
                                selectDate.setText(date.getTime().toString());

                            }
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });
    }
}

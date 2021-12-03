package com.example.list6_crime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import java.util.UUID;
import java.util.*;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class DetailActivity extends AppCompatActivity {

    Button delete;
    CrimeLab crimes;
    Crime crime;
    CheckBox checkBox;
    Button selectDate;
    Calendar date;
    EditText crime_Title;

    private ViewPager2 viewPager2;

    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view_pager2);

        viewPager2 = findViewById(R.id.activity_detail_viewpager2);


        //odbieranie kontentu i przeksztalcanie string(UUID) do UUID
        Intent intent = getIntent();
        String message = intent.getStringExtra("title");
        UUID number = UUID.fromString(message);


        Intent intent2 = getIntent();
        int currentCrime = intent2.getIntExtra("title3", 0);


        DetailPagerAdapter adapter = new DetailPagerAdapter(this);
        viewPager2.setAdapter(adapter);
        viewPager2.setCurrentItem(currentCrime);


        crimes = CrimeLab.get(this);
        crime = crimes.getCrime(number);

    }


    public void btnDeleteFunction(View view){
        crimes.getCrimes().remove(viewPager2.getCurrentItem());
        finish();
    }

    public void First(View view){
        viewPager2.setCurrentItem(0);
    }

    public void Last(View view){
        viewPager2.setCurrentItem(crimes.getCrimes().size());
    }



}

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

        crime_Title =findViewById(R.id.crime_title);;
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        selectDate = findViewById(R.id.btnDate);
        delete = findViewById(R.id.btnDelete);


        //odbieranie kontentu i przeksztalcanie string(UUID) do UUID
//        Intent intent = getIntent();
//        String message = intent.getStringExtra("title");
//        UUID number = UUID.fromString(message);

        // dlaczego nie moge po tym UUID w inta brać obiektu
//        int numberToInteger = Integer.parseInt(message);

        Intent intent2 = getIntent();
        int currentCrime = intent2.getIntExtra("title3", 0);


        DetailPagerAdapter adapter = new DetailPagerAdapter(this);
        viewPager2.setAdapter(adapter);
        viewPager2.setCurrentItem(currentCrime);


//        crimes = CrimeLab.get(this);
//        crime = crimes.getCrime(number);
//
//        if (crime.getmTitle() != null)
//            crime_Title.setText(crime.getmTitle());
//
//        if (crime.ismSolved())
//            checkBox.setChecked(true);
//
//        if (crime.getmDate() != null)
//            selectDate.setText(crime.getmDate().toString());
//
//
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                crimes.getCrimes().remove(crime);
//            }
//        });
//
//
//        crime_Title.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                if(crime.getmTitle().equals(String.valueOf(s))) {
//                    //
//                }else{
//                    crime.setmTitle(crime_Title.getText().toString());
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }

    public void onCheckBoxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        if (view.getId() == R.id.checkBox) {
            crime.setmSolved(checked);
        }
    }

//    public void changeText(View view){
//        if (!crime.getmTitle().equals(crime_Title))
//            crime.setmTitle(crime_Title.toString());
//
//    }

//class from stackoverflow
    public void showDateTimePicker(View view) {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog( DetailActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog( DetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        Log.v(TAG, "The choosen one " + date.getTime());
                        crime.setmDate(date.getTime());
//                        selectDate.setText(date.getTime().toString());


                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    public void btnDeleteFunction(View view){
        crimes.getCrimes().remove(viewPager2.getCurrentItem());
    }

    public void First(View view){
        viewPager2.setCurrentItem(0);
    }

    public void Last(View view){
        viewPager2.setCurrentItem(viewPager2.getAdapter().getItemCount());
    }



}

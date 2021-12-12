package com.example.list6_crime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class DetailPagerAdapter extends RecyclerView.Adapter<DetailPagerAdapter.ViewHolder> {

    private Context context;
    private LinkedList<Crime> crimes;
    Calendar date;

    public DetailPagerAdapter(Context context) {

        crimes = CrimeLab.mCrimes;
        this.context = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private EditText mTitleText;
        private Button mDataBtn;
        private CheckBox mCheckBox;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitleText = itemView.findViewById(R.id.crime_title);
            mDataBtn = itemView.findViewById(R.id.btnDate);
            mCheckBox = itemView.findViewById(R.id.checkBox);

        }

    }

    @NonNull
    @Override
    public DetailPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.activity_detail, viewGroup, false ));

    }

    @Override
    public void onBindViewHolder(@NonNull DetailPagerAdapter.ViewHolder viewHolder, int position) {
        Crime currentCrime = crimes.get(position);
        viewHolder.mTitleText.setText(currentCrime.getmTitle());
        viewHolder.mDataBtn.setText(currentCrime.getmDate().toString());
        viewHolder.mCheckBox.setChecked(currentCrime.ismSolved());

        viewHolder.mDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();
                date = Calendar.getInstance();
                new DatePickerDialog( context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog( context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date.set(Calendar.MINUTE, minute);
                                currentCrime.setmDate(date.getTime());
                                viewHolder.mDataBtn.setText(date.getTime().toString());


                            }
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });

        viewHolder.mTitleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(currentCrime.getmTitle().equals(String.valueOf(s))) {
                    //
                }else{
                    currentCrime.setmTitle(viewHolder.mTitleText.getText().toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checked = ((CheckBox) v).isChecked();

                if (v.getId() == R.id.checkBox) {
                    currentCrime.setmSolved(checked);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return crimes.size();
    }


}

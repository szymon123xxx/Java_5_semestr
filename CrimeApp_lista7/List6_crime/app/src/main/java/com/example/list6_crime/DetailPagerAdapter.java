package com.example.list6_crime;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;

public class DetailPagerAdapter extends RecyclerView.Adapter<DetailPagerAdapter.ViewHolder> {

    private Context context;
    private LinkedList<Crime> crimes;

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
//            itemView.setOnClickListener(this);

        }

//        @Override
//        public void onClick(View v) {
//            Crime currentCrime = crimes.get(getAbsoluteAdapterPosition());
//
//            if (!currentCrime.getmTitle().equals(mTitleText))
//                currentCrime.setmTitle(mTitleText.getText().toString());
//            mDataBtn.setText(currentCrime.getmDate().toString());
//        }

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

    }

    @Override
    public int getItemCount() {
        return crimes.size();
    }


}

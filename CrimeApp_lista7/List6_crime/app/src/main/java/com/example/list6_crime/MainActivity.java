package com.example.list6_crime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    CrimeLab crime = CrimeLab.get(this);
    LinkedList<Crime> ListCrime = crime.getCrimes();
    Date currentTime = Calendar.getInstance().getTime();

    private RecyclerView recyclerView;
    private WordListAdapter wordListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        wordListAdapter = new WordListAdapter(this, ListCrime);
        recyclerView.setAdapter(wordListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void createCrime(View view){
        Crime crime = new Crime();
        crime.setmId(UUID.randomUUID());
        crime.setmTitle("New crime");
        crime.setmDate(currentTime);
        ListCrime.add(crime);

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("title", crime.getmId().toString());
//        intent.putExtra("title2", crime.getmId2());
        intent.putExtra("title3", wordListAdapter.getItemCount());

        startActivity(intent);
        wordListAdapter.notifyDataSetChanged();

    }

}
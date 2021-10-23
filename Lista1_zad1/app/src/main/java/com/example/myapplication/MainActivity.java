package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private int count;

    private TextView showCounter;
    private Button countUp, countDown, reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showCounter = findViewById(R.id.showCount);
        if (savedInstanceState != null)
            count = savedInstanceState.getInt("counter_state");

        if (showCounter != null)
            showCounter.setText(Integer.toString(count));
    }

    public void countUp(View view) {
        count++;
        if (showCounter != null)
            showCounter.setText(Integer.toString(count));
    }

    public void countDown(View view) {
        count--;
        if (showCounter != null)
            showCounter.setText(Integer.toString(count));

    }

    public void Reset(View view) {
        count = 0;
        if (showCounter != null)
            showCounter.setText(Integer.toString(count));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("counter_state", count);
    }
}






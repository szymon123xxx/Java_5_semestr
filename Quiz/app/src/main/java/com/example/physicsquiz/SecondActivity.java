 package com.example.physicsquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;

 public class SecondActivity extends AppCompatActivity {

    TextView textView;
    TextView textView2;
    Button check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);

    }

     public void check2(View view) {
         Intent i = getIntent();
         String message = i.getStringExtra(MainActivity.EXTRA_MESSAGE);
         textView2.setText(""+message);
     }
 }
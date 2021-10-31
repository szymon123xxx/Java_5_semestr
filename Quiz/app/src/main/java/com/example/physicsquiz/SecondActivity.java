 package com.example.physicsquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;

 public class SecondActivity extends AppCompatActivity {

    TextView textView;
    boolean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        textView = findViewById(R.id.textView);

//        Intent intent = getIntent();
//        String text = intent.getParcelableExtra("parcel");
//        String pass = getIntent().getExtras().getString("parcel");

//        textView.setText(pass);

        Intent i = getIntent();
//        String[] mystring = i.getStringArrayExtra("parcel");
        Question siema = i.getParcelableExtra("parcel");


        if (siema !=null)
            result = siema.isAnswer();
        textView.setText(Boolean.toString(result));
    }
}
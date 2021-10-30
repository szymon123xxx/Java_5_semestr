package com.example.kalkulator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String oldNumber = "";
    String op = "+";
    TextView edl;
    boolean isNewOp = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edl = findViewById(R.id.editTextTextPersonName2);

    }

    @SuppressLint("NonConstantResourceId")
    public void Event(View view) {
        if (isNewOp)
            edl.setText("");
        isNewOp = false;
        String number = edl.getText().toString();
        switch (view.getId()) {
            case R.id.button7:
                number += "7";
                break;
            case R.id.button1:
                number += "1";
                break;
            case R.id.button2:
                number += "2";
                break;
            case R.id.button3:
                number += "3";
                break;
            case R.id.button4:
                number += "4";
                break;
            case R.id.button5:
                number += "5";
                break;
            case R.id.button6:
                number += "6";
                break;
            case R.id.button8:
                number += "8";
                break;
            case R.id.button9:
                number += "9";
                break;
            case R.id.button0:
                number += "0";
                break;
        }
        edl.setText(number);
    }

    @SuppressLint("NonConstantResourceId")
    public void OperatorEvent(View view) {
        isNewOp = true;
        oldNumber = edl.getText().toString();
        switch (view.getId()) {
            case R.id.divide: op = "/"; break;
            case R.id.add: op = "+"; break;
            case R.id.multiply: op = "*"; break;
            case R.id.subtract: op = "-"; break;
        }

    }

    @SuppressLint("SetTextI18n")
    public void equalEvent(View view) {
        String newNumber = edl.getText().toString();
        double result = 0.0;
        switch (op) {
            case "+":
                result = Double.parseDouble(oldNumber) + Double.parseDouble(newNumber);
                break;
            case "/":
                result = Double.parseDouble(oldNumber) / Double.parseDouble(newNumber);
                break;
            case "-":
                result = Double.parseDouble(oldNumber) - Double.parseDouble(newNumber);
                break;
            case "*":
                result = Double.parseDouble(oldNumber) * Double.parseDouble(newNumber);
                break;
        }

        if (Double.parseDouble(newNumber) == 0 && op.equals("/")) {
            edl.setText("Nie mozna dzielic przez zero");
        }
        else {
            edl.setText(result + "");
        }
    }

    public void ClearEvent(View view) {
        edl.setText("0");
        isNewOp = true;
    }
}

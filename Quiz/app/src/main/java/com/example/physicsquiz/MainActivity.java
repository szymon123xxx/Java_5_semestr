package com.example.physicsquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {


    private TextView textView;
    private TextView pointsView;
    boolean[] isAlreadyAnswer = new boolean[] {
            false,false,false
    };
    int i = 0;
    int points = 0;

    private final Question[] questions = new Question[] {
            new Question(R.string.question1, true),
            new Question(R.string.question2, false),
            new Question(R.string.question3, true),

    };

    int maxPoints = questions.length;
    Button restartButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.question_text);
        textView.setText(questions[0].getTextID());
        restartButton = findViewById(R.id.Restart);

        pointsView = findViewById(R.id.punctation);

        //proba strzoenia intent
        Button button = findViewById(R.id.check);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//            intent.putExtra("parcel", questions[i].isAnswer());
            intent.putExtra("parcel", questions[i].isAnswer());
            startActivity(intent);
        });

    }
    //sprawdzanie tablicy czy zawiera wszystkie booliny na true
    public static boolean areAllTrue(boolean[] array)
    {
        for(boolean b : array) if(!b) return false;
        return true;
    }


    public void Next(View view) {
        i++;
        if (i >= questions.length) {
            i = 0;
        }
        textView.setText(questions[i].getTextID());
    }

    public void Previous(View view) {
        i--;
        if (i < 0) {
            i = questions.length-1;
        }
        textView.setText(questions[i].getTextID());
    }

    public void AnswerTrue(View view) {
        if (questions[i].isAnswer() && !isAlreadyAnswer[i]) {
            isAlreadyAnswer[i] = true;
            points += 1;
        }
        else {
            isAlreadyAnswer[i] = true;
        }
        //wywolywanie klasy do sprawdzania
        if (areAllTrue(isAlreadyAnswer)) {
            Button restartButton = findViewById(R.id.Restart);
            restartButton.setVisibility(View.VISIBLE);
            pointsView.setText("Maksymalna ilosc punktow do zdobycia: " + maxPoints + "\n" + "Liczba poprawnych odpowiedzi: " + points + "\n" + "Liczba blednych odpowiedzi: " + (maxPoints - points));
        }
    }

    public void AnswerFalse(View view) {
        if ( !questions[i].isAnswer() && !isAlreadyAnswer[i] ){
            isAlreadyAnswer[i] = true;
            points += 1;
        }
        else {
            isAlreadyAnswer[i] = true;
        }
        //wywolywanie klasy do sprawdzania
        if (areAllTrue(isAlreadyAnswer)) {
//            Button restartButton = findViewById(R.id.Restart);
            restartButton.setVisibility(View.VISIBLE);
            pointsView.setText("Maksymalna ilosc punktow do zdobycia: " + maxPoints + "\n" + "Liczba poprawnych odpowiedzi: " + points + "\n" + "Liczba blednych odpowiedzi: " + (maxPoints - points));
        }
    }


    public void Restart(View view) {
        for (int i = 0; i < questions.length; i++) {
            isAlreadyAnswer[i] = false;
        }
        //sprawdzanie czy elementy tablicy sa na false
        if (!areAllTrue(isAlreadyAnswer)) {
            pointsView.setText("");
        }
        //ustawianie kolejki pytan od poczatku, resetowanie punktow i numerow tablicy
        points = 0;
        i = 0;
//        Button restartButton = findViewById(R.id.Restart);
        restartButton.setVisibility(View.INVISIBLE);
        textView.setText(questions[0].getTextID());

    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        i = savedInstanceState.getInt("iteration_variable");
        maxPoints = savedInstanceState.getInt("maxPoints");
        points = savedInstanceState.getInt("points");
        restartButton.setVisibility(savedInstanceState.getInt("Button_Restart"));
        textView.setText(questions[i].getTextID());
        pointsView.setText("Maksymalna ilosc punktow do zdobycia: " + maxPoints + "\n" + "Liczba poprawnych odpowiedzi: " + points + "\n" + "Liczba blednych odpowiedzi: " + (maxPoints - points));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("iteration_variable", i);
        outState.putInt("Button_Restart", restartButton.getVisibility());
        outState.putInt("maxPoints", maxPoints);
        outState.putInt("points", points);

    }
}
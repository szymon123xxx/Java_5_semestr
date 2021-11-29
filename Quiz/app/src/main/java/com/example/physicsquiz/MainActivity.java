package com.example.physicsquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.physicsquiz.MESSAGE";
    private TextView textView;
    private TextView pointsView;
    boolean[] isAlreadyAnswer = new boolean[] {
            false,false,false
    };
    int i = 0;
    double points = 0;
    double falsePoints = 0;
    double cheated = 0;


    private final Question[] questions = new Question[] {
            new Question(R.string.question1, true),
            new Question(R.string.question2, true),
            new Question(R.string.question3, true),
    };

    int maxPoints = questions.length;
    Button restartButton;
    Boolean clickedCheat = false;
    Button isTrue;
    Button isFalse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.question_text);
        textView.setText(questions[0].getTextID());
        restartButton = findViewById(R.id.Restart);

        pointsView = findViewById(R.id.punctation);

        isTrue = findViewById(R.id.answerTrue);
        isFalse = findViewById(R.id.answerFalse);


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
        isTrue.setEnabled(true);
        isFalse.setEnabled(true);
    }

    public void Previous(View view) {
        i--;
        if (i < 0) {
            i = questions.length-1;
        }
        textView.setText(questions[i].getTextID());
        isTrue.setEnabled(true);
        isFalse.setEnabled(true);
    }

    @SuppressLint("SetTextI18n")
    public void AnswerTrue(View view) {
        if ( !questions[i].isAnswer() && !isAlreadyAnswer[i]) {
            isAlreadyAnswer[i] = true;
            falsePoints += 1;
        }
        else if (questions[i].isAnswer() && !isAlreadyAnswer[i]) {
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
            pointsView.setText("Maksymalna ilosc punktow do zdobycia: " + maxPoints + "\n" + "Liczba poprawnych odpowiedzi: " + points + "\n" + "Liczba blednych odpowiedzi: " + falsePoints + "\n" + "Oszukano: " + cheated + "\n" + "Wynik koncowy: " + (points- (0.15*cheated*points)));
        }
    }

    @SuppressLint("SetTextI18n")
    public void AnswerFalse(View view) {
        if ( questions[i].isAnswer() && !isAlreadyAnswer[i]) {
            isAlreadyAnswer[i] = true;
            falsePoints += 1;
        }
        else if ( !questions[i].isAnswer() && !isAlreadyAnswer[i] ){
            isAlreadyAnswer[i] = true;
            points += 1;
        }
        else {
            isAlreadyAnswer[i] = true;
        }
        //wywolywanie klasy do sprawdzania
        if (areAllTrue(isAlreadyAnswer)) {
            restartButton.setVisibility(View.VISIBLE);
            pointsView.setText("Maksymalna ilosc punktow do zdobycia: " + maxPoints + "\n" + "Liczba poprawnych odpowiedzi: " + points + "\n" + "Liczba blednych odpowiedzi: " + falsePoints + "\n" + "Oszukano: " + cheated + "\n" + "Wynik koncowy: " + (points- (0.15*cheated*points)));
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
        cheated = 0;
        falsePoints = 0;
        isTrue.setEnabled(true);
        isFalse.setEnabled(true);
        restartButton.setVisibility(View.INVISIBLE);
        textView.setText(questions[0].getTextID());

    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        i = savedInstanceState.getInt("iteration_variable");
        maxPoints = savedInstanceState.getInt("maxPoints");
        points = savedInstanceState.getDouble("points");
        falsePoints = savedInstanceState.getDouble("falsePoints");
        cheated = savedInstanceState.getDouble("cheated");
        restartButton.setVisibility(savedInstanceState.getInt("Button_Restart"));
        isAlreadyAnswer = savedInstanceState.getBooleanArray("array");
        textView.setText(questions[i].getTextID());
        if (areAllTrue(isAlreadyAnswer))
            pointsView.setText("Maksymalna ilosc punktow do zdobycia: " + maxPoints + "\n" + "Liczba poprawnych odpowiedzi: " + points + "\n" + "Liczba blednych odpowiedzi: " + falsePoints + "\n" + "Oszukano: " + cheated + "\n" + "Wynik koncowy: " + (points- (0.15*cheated*points)));

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("iteration_variable", i);
        outState.putInt("Button_Restart", restartButton.getVisibility());
        outState.putInt("maxPoints", maxPoints);
        outState.putDouble("points", points);
        outState.putDouble("falsePoints", falsePoints);
        outState.putDouble("cheated", cheated);
        outState.putBooleanArray("array", isAlreadyAnswer);

    }

    @SuppressLint("SetTextI18n")
    public void cheat(View view) {

        if (!isAlreadyAnswer[i]) {
            cheated += 1;
            isAlreadyAnswer[i] = true;
        }

        clickedCheat = true;
        points += 0;
        isTrue.setEnabled(false);
        isFalse.setEnabled(false);
        //gdy jestem na ostatnim pytaniu i przejde do innej aktywnosci to jest po to zeby mi tekst sie ponownie wyswietlil jak wroce
        if (areAllTrue(isAlreadyAnswer)) {
            restartButton.setVisibility(View.VISIBLE);
            pointsView.setText("Maksymalna ilosc punktow do zdobycia: " + maxPoints + "\n" + "Liczba poprawnych odpowiedzi: " + points + "\n" + "Liczba blednych odpowiedzi: " + falsePoints + "\n" + "Oszukano: " + cheated + "\n" + "Wynik koncowy: " + (points- (0.15*cheated*points)));
        }
        String cheatAnswer = String.valueOf(questions[i].isAnswer());
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra(EXTRA_MESSAGE, cheatAnswer);
        startActivity(intent);

    }

    public void findBrowser(View view) {
        String sharedFact = textView.getText().toString();
        String url = "https://www.google.com/search?q=" + sharedFact;
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.d("error", "no activity");
        }
    }


}
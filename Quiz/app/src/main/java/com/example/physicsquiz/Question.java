package com.example.physicsquiz;

public class Question {
    private int textID;
    private boolean answer;

    public Question(int textID, boolean answer) {
        this.textID = textID;
        this.answer = answer;
    }

    public int getTextID() {
        return textID;
    }

    public void setTextID(int textID) {
        this.textID = textID;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}

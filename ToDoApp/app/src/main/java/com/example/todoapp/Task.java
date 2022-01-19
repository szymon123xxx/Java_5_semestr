package com.example.todoapp;

import java.util.Date;

public class Task {

    private int Id;
    private Date mDate;
    private boolean mSolved;
    private String mPicture;
    private String mTitle;


    public Task() {
    }

    public Task(int id, String mTitle, Date mDate, boolean mSolved, String mPicture) {
        this(mTitle, mDate, mSolved, mPicture);
        this.Id = id;
    }

    public Task(String mTitle, Date mDate, boolean mSolved, String mPicture) {
        this.mTitle = mTitle;
        this.mDate = mDate;
        this.mSolved = mSolved;
        this.mPicture = mPicture;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public String getmPicture() {
        return mPicture;
    }

    public void setmPicture(String mPicture) {
        this.mPicture = mPicture;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

}

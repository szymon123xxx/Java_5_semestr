package com.example.list6_crime;

import java.util.*;
import java.util.Date;

public class Crime {
    private int mId2;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;


    public Crime() {

    }

    public Crime(int id, String mTitle, Date mDate, boolean mSolved){
        this(mTitle, mDate, mSolved);
        this.mId2 = id;
    }

    public Crime(String mTitle, Date mDate, boolean mSolved) {
        this.mTitle = mTitle;
        this.mDate = mDate;
        this.mSolved = mSolved;
    }


    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
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

    public int getmId2() {

        return mId2;
    }

    public void setmId2(int mId2) {
        this.mId2 = mId2;
    }


}

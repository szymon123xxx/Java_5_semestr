package com.example.list6_crime;
import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;
import java.util.Date;

public class CrimeLab {

    Date currentTime = Calendar.getInstance().getTime();

    private static CrimeLab sCrimeLab;
    private LinkedList<Crime> mCrimes;

    public static CrimeLab get(Context context){
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimes = new LinkedList<>();
        for (int i = 0; i < 50; i++) {
            Crime crime = new Crime();
            crime.setmId(UUID.randomUUID());
            crime.setmTitle("Crime #" + i);
            crime.setmSolved(i % 2 == 0);
            crime.setmDate(currentTime);
            mCrimes.add(crime);
        }
    }

    public LinkedList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getmId().equals(id)) {
                return crime;
            }
        }
        return null;
    }

}

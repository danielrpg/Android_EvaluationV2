package com.gdfp.android_evaluationv2.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

/* Wrapper to show a managed date picker */
public class DatePickerFragment extends DialogFragment {

    private long mDueDate;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //TODO Use the current date as the default date in the picker
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        //TODO Create a new instance of DatePickerDialog and return it

        return new DatePickerDialog(getActivity(),null,year,month,day);
    }
}

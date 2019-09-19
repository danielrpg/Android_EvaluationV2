package com.gdfp.android_evaluationv2.views;

import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.gdfp.android_evaluationv2.R;
import java.util.Calendar;

/* Wrapper to show a managed date picker */
public class DatePickerFragment extends DialogFragment {

    private long mDueDate;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //TODO Use the current date as the default date in the picker
        Calendar c = Calendar.getInstance();


        //TODO Create a new instance of DatePickerDialog and return it

    }

}

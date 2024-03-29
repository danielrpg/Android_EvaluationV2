package com.gdfp.android_evaluationv2;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.gdfp.android_evaluationv2.data.DatabaseContract;
import com.gdfp.android_evaluationv2.data.TaskUpdateService;
import com.gdfp.android_evaluationv2.views.DatePickerFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        View.OnClickListener {

    //Selected due date, stored as a timestamp
    private long mDueDate = Long.MAX_VALUE;

    private TextInputEditText mDescriptionView;
    private SwitchCompat mPrioritySelect;
    private TextView mDueDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mDescriptionView = (TextInputEditText) findViewById(R.id.text_input_description);
        mPrioritySelect = (SwitchCompat) findViewById(R.id.switch_priority);
        mDueDateView = (TextView) findViewById(R.id.text_date);
        View mSelectDate = findViewById(R.id.select_date);

        mSelectDate.setOnClickListener(this);
        updateDateDisplay();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_save) {
            saveItem();
        }
        return super.onOptionsItemSelected(item);
    }


    public long getDateSelection() {
        return mDueDate;
    }

    /* Click events on Due Date */
    @Override
    public void onClick(View v) {
        DatePickerFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /* Date set events from dialog */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //Set to noon on the selected day
        Calendar c = Calendar.getInstance();
        c.set(year,month,day,12,30,0);
        setDateSelection(c.getTimeInMillis());
    }

    /* Manage the selected date value */
    public void setDateSelection(long selectedTimestamp) {
        mDueDate = selectedTimestamp;
        updateDateDisplay();
    }

    private void updateDateDisplay() {
        if (getDateSelection() == Long.MAX_VALUE) {
            mDueDateView.setText(R.string.date_empty);
        } else {
            Date d = new Date(mDueDate);
            mDueDateView.setText(d.toString());
        }
    }

    private void saveItem() {
        //TODO Insert a new item
        ContentValues values = new ContentValues(4);
        values.put(DatabaseContract.TaskColumns.DESCRIPTION, mDescriptionView.getText().toString());
        values.put(DatabaseContract.TaskColumns.IS_COMPLETE, 0);
        values.put(DatabaseContract.TaskColumns.IS_PRIORITY, mPrioritySelect.isChecked());
        values.put(DatabaseContract.TaskColumns.DUE_DATE, mDueDate);
        TaskUpdateService.insertNewTask(this, values);
        finish();
    }

}

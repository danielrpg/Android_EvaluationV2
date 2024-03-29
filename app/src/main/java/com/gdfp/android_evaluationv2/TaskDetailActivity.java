package com.gdfp.android_evaluationv2;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gdfp.android_evaluationv2.data.DatabaseContract;
import com.gdfp.android_evaluationv2.data.TaskUpdateService;
import com.gdfp.android_evaluationv2.reminders.AlarmScheduler;
import com.gdfp.android_evaluationv2.views.DatePickerFragment;
import com.gdfp.android_evaluationv2.views.TaskTitleView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TaskDetailActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener {

    public ContentValues values = new ContentValues(4);

    // Declare Custom TaskTitleView
    public TaskTitleView textDescription;

    // Declare TextView's
    private TextView textDate;

    // Declare ImageView's
    public ImageView imagePriority;

    // Declare field Uri
    Uri mUri;

    // Declare a cursor field
    private Cursor mDetailCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
        //TODO: Task must be passed to this activity as a valid provider Uri
        // Get the Uri from the Intent
        final Uri taskUri = getIntent().getData();

        //TODO: Persist the Uri as a field of the class
        mUri = taskUri;

        //TODO: Display attributes of the provided task in the UI
        //TODO: Initialize the view components of the Activity
        initView();

        getData();
        //TODO: Get the data from the database and display it in the UI

    }

    private void initView() {

        //TODO: Initialize the textDescription TextView
        textDescription = findViewById(R.id.text_description);

        //TODO: Initialize the textDate TextView
        textDate = findViewById(R.id.text_date);

        //TODO: Initialize the imagePriority ImageView
        imagePriority = findViewById(R.id.priority);
    }

    private void getData() {

        //TODO: Query the database, passing the Uri of a specific Task
        mDetailCursor = getContentResolver().query(mUri,
                null,
                null,
                null,
                null
        );

        //TODO: Assert that the cursor is not null
        if (mDetailCursor == null) {
            return;
        }

        //TODO Get the column index of the checkbox
        int indexCheckbox = mDetailCursor.getColumnIndex(DatabaseContract.TaskColumns.IS_COMPLETE);

        //TODO Get the column index of the priority
        int indexPriority = mDetailCursor.getColumnIndex(DatabaseContract.TaskColumns.IS_PRIORITY);

        //TODO Get the column index of the dueDate
        int indexDueDate = mDetailCursor.getColumnIndex(DatabaseContract.TaskColumns.DUE_DATE);

        //TODO Get the column index of the taskDescription
        int indexDescription = mDetailCursor.getColumnIndex(DatabaseContract.TaskColumns.DESCRIPTION);

        //TODO Move to the beginning of the cursor
        mDetailCursor.moveToFirst();


        //TODO Get the value of taskDescription from the cursor
        String taskDescription = mDetailCursor.getString(indexDescription);

        //TODO Get the value of isComplete from the cursor
        int checkbox = mDetailCursor.getInt(indexCheckbox);

        //TODO Get the value of priority from the cursor
        int priority = mDetailCursor.getInt(indexPriority);

        //TODO Get the value of dueDate from the cursor
        long dueDate = mDetailCursor.getLong(indexDueDate);

        //TODO If the Task is a priority...
        if (priority == 1) {

            //TODO Set the imagePriority ImageView with the priority icon
            imagePriority.setImageResource(R.drawable.ic_priority);
        } else {//TODO If the Task is a non-priority...
            //TODO Set the imagePriority ImageView with the non-priority icon
            imagePriority.setBackgroundResource(R.drawable.ic_not_priority);
        }
        //TODO If there is no due date...
        if (dueDate == Long.MAX_VALUE) {
            //TODO Display the "Not Set" text in the textDate TextView
            textDate.setText(R.string.date_empty);

        } else {//TODO If there is a due date...

            //TODO Format the dueDate
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String formatted = formatter.format(new Date(dueDate));
            // also you can do this
            // CharSequence formatted = DateUtils.getRelativeTimeSpanString(this, dueDate);

            //TODO Make the textDate TextView visible
            textDate.setVisibility(View.VISIBLE);

            //TODO Set the date of the Task on the textDate TextView
            textDate.setText(String.format("Due Date : %s", formatted));
        }

        //TODO Set the description of the Task on the textDate TextView
        textDescription.setText(taskDescription);
        persistTask(taskDescription,checkbox,priority,dueDate);
    }

    public void persistTask(String desc, int is_complete, int is_priority, long dueDate) {
        values.put(DatabaseContract.TaskColumns.DESCRIPTION, desc);
        values.put(DatabaseContract.TaskColumns.IS_COMPLETE, is_complete);
        values.put(DatabaseContract.TaskColumns.IS_PRIORITY, is_priority);
        values.put(DatabaseContract.TaskColumns.DUE_DATE, dueDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu_task_detail menu items
        getMenuInflater().inflate(R.menu.menu_task_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //TODO Get the Id of the selected menu item
        switch (item.getItemId()) {

            //TODO If the menu item selected was the delete item...
            case R.id.action_delete:
                // Delete the Task, passing the context and the URI
                // of the to be deleted Task
                TaskUpdateService.deleteTask(this, mUri);
                // Finish the current Activity and go back to the MainActivity
                finish();
                // Break from the switch statement
                break;

            // If the menu item selected was the reminder item...
            case R.id.action_reminder:

                // Create an instance of the DatePickerFragment
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                // Show the DatePickerFragment, setting a tag for identification
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
                // Break from the switch statement
                break;

            // In the case of default...
            default:

                // Break from the switch statement
                TaskUpdateService.updateTask(this, mUri,values);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //TODO: Handle date selection from a DatePickerFragment

        // Get the current time from the Calendar instance
        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.HOUR_OF_DAY, 12);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);

        // Get the selected time from the Calendar instance
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        // Set the data on the dialog
        view.setMinDate(c.getTimeInMillis());

        // If the selected time is previous to the current time...
        if (c.getTimeInMillis() < c2.getTimeInMillis()) {

            // Display Error Toast message
            Toast.makeText(this, R.string.date_error, Toast.LENGTH_SHORT).show();

            // Return from the method
            return;
        }

        // Display Alarm scheduled Toast
        Toast.makeText(this, "Alarm scheduled to " + DateUtils.getRelativeTimeSpanString(this, c.getTimeInMillis()), Toast.LENGTH_SHORT).show();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formatted = formatter.format(new Date(c.getTimeInMillis()));
        textDate.setVisibility(View.VISIBLE);
        textDate.setText(String.format("Due Date : %s", formatted));
        //update Due date
        values.remove(DatabaseContract.TaskColumns.DUE_DATE);
        values.put(DatabaseContract.TaskColumns.DUE_DATE,c.getTimeInMillis());

        // Schedule the alarm to the time selected by the user in milliseconds
        AlarmScheduler.scheduleAlarm(getApplicationContext(), c.getTimeInMillis(), mUri);
    }

}

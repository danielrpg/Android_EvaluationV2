package com.google.developer.taskmaker;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.developer.taskmaker.data.DatabaseContract;
import com.google.developer.taskmaker.data.TaskUpdateService;
import com.google.developer.taskmaker.reminders.AlarmScheduler;
import com.google.developer.taskmaker.views.DatePickerFragment;
import com.google.developer.taskmaker.views.TaskTitleView;

import java.util.Calendar;

public class TaskDetailActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener {

    // Declare Custom TaskTitleView
    private TaskTitleView textDescription;

    // Declare TextView's
    private TextView textDate;

    // Declare ImageView's
    private ImageView imagePriority;

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

        //TODO: Display attributes of the provided task in the UI

        //TODO: Initialize the view components of the Activity

        //TODO: Get the data from the database and display it in the UI

    }

    private void initView() {

        //TODO: Initialize the textDescription TextView


        //TODO: Initialize the textDate TextView


        //TODO: Initialize the imagePriority ImageView

    }

    private void getData() {

        //TODO: Query the database, passing the Uri of a specific Task


        //TODO: Assert that the cursor is not null


        //TODO Get the column index of the checkbox


        //TODO Get the column index of the priority


        //TODO Get the column index of the dueDate

        //TODO Get the column index of the taskDescription


        //TODO Move to the beginning of the cursor


        //TODO Get the value of isComplete from the cursor


        //TODO Get the value of priority from the cursor


        //TODO Get the value of dueDate from the cursor


        //TODO Get the value of taskDescription from the cursor


        //TODO If the Task is a priority...


            //TODO Set the imagePriority ImageView with the priority icon


            //TODO If the Task is a non-priority...

            //TODO Set the imagePriority ImageView with the non-priority icon


        //TODO If there is no due date...


            //TODO Display the "Not Set" text in the textDate TextView


         //TODO If there is a due date...

            //TODO Format the dueDate


            // Make the textDate TextView visible
            textDate.setVisibility(View.VISIBLE);

            // Set the date of the Task on the textDate TextView
            textDate.setText(String.format("Due Date : %s", formatted));
        }

        // Set the description of the Task on the textDate TextView
        textDescription.setText(taskDescription);
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

        // Schedule the alarm to the time selected by the user in milliseconds
        AlarmScheduler.scheduleAlarm(getApplicationContext(), c.getTimeInMillis(), mUri);
    }

}

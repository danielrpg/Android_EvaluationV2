package com.gdfp.android_evaluationv2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gdfp.android_evaluationv2.data.DatabaseContract;
import com.gdfp.android_evaluationv2.data.TaskAdapter;
import com.gdfp.android_evaluationv2.data.TaskUpdateService;

import static com.gdfp.android_evaluationv2.data.DatabaseContract.CONTENT_URI;
import static com.gdfp.android_evaluationv2.data.DatabaseContract.DATE_SORT;
import static com.gdfp.android_evaluationv2.data.DatabaseContract.DEFAULT_SORT;

// TODO: Make the Activity implement the LoaderCallbacks interface
public class MainActivity extends AppCompatActivity implements
        TaskAdapter.OnItemClickListener,
        View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    // TODO: Create TAG for logging
    private static final String TAG = "MainActivity";

    private TaskAdapter mAdapter;

    // TODO:Create ID for the specific Loader in this Activity
    private static final int ID_TASK_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAdapter = new TaskAdapter(null);
        mAdapter.setOnItemClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //TODO: Initialize the Loader
        LoaderManager.getInstance(this).initLoader(ID_TASK_LOADER, null, this);

    }

    //TODO: Override the onResume() lifecycle method
    @Override
    protected void onRestart() {
        super.onRestart();

        //TODO: Restart the Loader
        LoaderManager.getInstance(this).restartLoader(ID_TASK_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO: noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    /*  Click events in Floating Action Button */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }

    /* Click events in RecyclerView items */
    @Override
    public void onItemClick(View v, int position) {
        //TODO: Handle list item click event
        // Create an Intent to navigate to the TaskDetailActivity
        Intent detailsIntent = new Intent(this, TaskDetailActivity.class);

        //TODO: Set the data (URI and item Id) in the Intent
        detailsIntent.setData(ContentUris.withAppendedId(DatabaseContract.CONTENT_URI, mAdapter.getItemId(position)));
        //TODO: Start the Activity, passing the Intent
        startActivity(detailsIntent);

    }

    /* Click events on RecyclerView item checkboxes */
    @Override
    public void onItemToggled(boolean active, int position) {
        //TODO: Handle task item checkbox event
        // Create a ContentValues object
        ContentValues contentValues = new ContentValues();

        //TODO: If the Task is checked...
        if (active) {
            //TODO: Store the Task as inactive in the IS_COMPLETE column
            contentValues.put(DatabaseContract.TaskColumns.IS_COMPLETE, 1);
        }
        //TODO: If the Task is not checked...
        if (!active) {
            //TODO: Store the Task as active in the IS_COMPLETE column
            contentValues.put(DatabaseContract.TaskColumns.IS_COMPLETE, 0);
        }

        contentValues.put(DatabaseContract.TaskColumns.DESCRIPTION, mAdapter.getItem(position).description);
        contentValues.put(DatabaseContract.TaskColumns.DUE_DATE, mAdapter.getItem(position).dueDateMillis);
        contentValues.put(DatabaseContract.TaskColumns.IS_PRIORITY, mAdapter.getItem(position).isPriority);
        //TODO: Update the Task, passing the context, the ContentURI, the Task's Id,
        // and the ContentValues object
        TaskUpdateService.updateTask(this, ContentUris.withAppendedId(DatabaseContract.CONTENT_URI, mAdapter.getItemId(position)), contentValues);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        //TODO: Return a new CursorLoader object, passing the ContentURI,
        // and the sort order
        return new CursorLoader(this, CONTENT_URI, null, null, null, getOrder());
    }

    private final String getOrder() {
        //TODO: Retrieve the order from SharedPreferences
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        //TODO: Create orderToSend String
        String order = prefs.getString("sort", null);
        String orderToSend = null;
        //TODO: If the order is "default"...
        if (order.equals("default")) {
            //TODO: Set the sort order as "DEFAULT_SORT"
            orderToSend = DEFAULT_SORT;
        }
        //TODO: If the order is not "default"...
        else if (order.equals("dueDate")) {
            //TODO: Set the sort order as "DATE_SORT"
            orderToSend = DATE_SORT;
        }
        //TODO: Return the sort order to the query
        return orderToSend;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // Swap the old cursor in the adapter with a new cursor
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // Swap the old cursor in the adapter with a null cursor
        mAdapter.swapCursor(null);
    }
}

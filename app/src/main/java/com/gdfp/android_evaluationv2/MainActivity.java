package com.gdfp.android_evaluationv2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gdfp.android_evaluationv2.data.DatabaseContract;
import com.gdfp.android_evaluationv2.data.TaskAdapter;
import com.gdfp.android_evaluationv2.data.TaskDbHelper;

// TODO: Make the Activity implement the LoaderCallbacks interface
public class MainActivity extends AppCompatActivity implements
        TaskAdapter.OnItemClickListener,
        View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    // TODO: Create TAG for logging
    private static final String TAG = "MainActivity";

    private TaskAdapter mAdapter;
    private SQLiteDatabase mDatabase;

    // TODO:Create ID for the specific Loader in this Activity
    private static final int ID_TASK_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TaskDbHelper helper =new TaskDbHelper(this);
        mDatabase = helper.getWritableDatabase();
        setSupportActionBar(toolbar);

        mAdapter = new TaskAdapter(getAllItems(),this);
        mAdapter.setOnItemClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //TODO: Initialize the Loader

    }

    //TODO: Override the onResume() lifecycle method
    @Override
    protected void onResume() {
        super.onResume();

        //TODO: Restart the Loader

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //TODO: noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    /*  Click events in Floating Action Button */
    @Override
    public void onClick(View v) {

    }

    /* Click events in RecyclerView items */
    @Override
    public void onItemClick(View v, int position) {
        //TODO: Handle list item click event
        // Create an Intent to navigate to the TaskDetailActivity
        Intent intent = new Intent(this,TaskDetailActivity.class);

        //TODO: Set the data (URI and item Id) in the Intent
        //intent.putExtra();

        //TODO: Start the Activity, passing the Intent

    }

    /* Click events on RecyclerView item checkboxes */
    @Override
    public void onItemToggled(boolean active, int position) {
        //TODO: Handle task item checkbox event
        // Create a ContentValues object
        ContentValues cv = new ContentValues();

        //TODO: If the Task is checked...


        //TODO: Store the Task as inactive in the IS_COMPLETE column


        //TODO: If the Task is not checked...


        //TODO: Store the Task as active in the IS_COMPLETE column


        //TODO: Update the Task, passing the context, the ContentURI, the Task's Id,
        // and the ContentValues object

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        //TODO: Return a new CursorLoader object, passing the ContentURI,
        // and the sort order

    }

    private final String getOrder() {

        //TODO: Retrieve the order from SharedPreferences


        //TODO: Create orderToSend String


        //TODO: If the order is "default"...


        //TODO: Set the sort order as "DEFAULT_SORT"



        //TODO: If the order is "default"...

        //TODO: Set the sort order as "DATE_SORT"

        //TODO: Return the sort order to the query

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

    private Cursor getAllItems() {
        return mDatabase.query(
                DatabaseContract.TABLE_TASKS,
                null,
                null,
                null,
                null,
                null,
                DatabaseContract.TaskColumns.DUE_DATE+ " DESC"
        );
    }
}

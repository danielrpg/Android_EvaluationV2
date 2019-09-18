package com.google.developer.taskmaker.data;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.developer.taskmaker.R;
import com.google.developer.taskmaker.views.TaskTitleView;

import java.sql.Date;
import java.text.SimpleDateFormat;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private Cursor mCursor;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    public TaskAdapter(Cursor cursor, Context context) {
        mCursor = cursor;
        this.mContext = context;
    }

    /* Callback for list item click events */
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
        void onItemToggled(boolean active, int position);

    }

    /* ViewHolder for each task item */
    public class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TaskTitleView nameView;
        public TextView dateView;
        public ImageView priorityView;
        public CheckBox checkBox;


        public TaskHolder(View itemView) {
            super(itemView);

            nameView = (TaskTitleView) itemView.findViewById(R.id.text_description);
            dateView = (TextView) itemView.findViewById(R.id.text_date);
            priorityView = (ImageView) itemView.findViewById(R.id.priority);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);

            itemView.setOnClickListener(this);
            checkBox.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (v == checkBox) {
                completionToggled(this);
            } else {
                postItemClick(this);
            }
        }

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void completionToggled(TaskHolder holder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemToggled(holder.checkBox.isChecked(), holder.getAdapterPosition());
        }
    }

    private void postItemClick(TaskHolder holder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_task, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskHolder holder, int position) {
        
        //TODO: Bind the task data to the views

        //TODO Get the column index of the id
        int indexId = mCursor.getColumnIndex(DatabaseContract.TaskColumns._ID);

        //TODO Get the column index of the checkbox
        int indexCheckbox = mCursor.getColumnIndex(DatabaseContract.TaskColumns.IS_COMPLETE);

        //TODO Get the column index of the priority
        int indexPriority = mCursor.getColumnIndex(DatabaseContract.TaskColumns.IS_PRIORITY);

        //TODO Get the column index of the dueDate
        int indexDueDate = mCursor.getColumnIndex(DatabaseContract.TaskColumns.DUE_DATE);

        //TODO Get the column index of the taskDescription
        int indexDescription = mCursor.getColumnIndex(DatabaseContract.TaskColumns.DESCRIPTION);

        //TODO Move cursor to position
        mCursor.moveToPosition(position);

        //TODO Get the value of id from the cursor
        long id = mCursor.getLong(indexId);

        //TODO Get the value of isComplete from the cursor
        int isComplete = mCursor.getInt(indexCheckbox);

        //TODO Get the value of priority from the cursor
        int isPriority = mCursor.getInt(indexPriority);

        //TODO Get the value of dueDate from the cursor
        long dueDateMillis = mCursor.getLong(indexDueDate);

        //TODO Get the value of taskDescription from the cursor
        String description = mCursor.getString(indexDescription);

        //TODO Set a tag on the itemView with the id of the Task
        holder.itemView.setTag(id);

        //TODO If the Task is done...
        if (isComplete== 1) {
            //TODO Check the checkbox
            holder.checkBox.setChecked(true);

            //TODO Set the state of the nameView to "DONE"
            holder.nameView.setState(TaskTitleView.DONE);

            //TODO Strike through the text of th nameView
            holder.nameView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            //TODO If the Task is overdue...
        } else if (dueDateMillis < System.currentTimeMillis()) {

            //TODO Set the state of the nameView to "OVERDUE"
            holder.nameView.setState(TaskTitleView.OVERDUE);
            //TODO un-Check the checkbox
            holder.checkBox.setChecked(false);


        } else { //TODO If the Task is normal...

            //TODO Set the state of the nameView to "NORMAL"
            holder.nameView.setState(TaskTitleView.NORMAL);

            //TODO un-Check the checkbox
            holder.checkBox.setChecked(false);
;
        }

        //TODO If the Task is a priority...
        if (isPriority== 1) {
            //TODO Set the priorityView ImageView with the priority icon
            holder.priorityView.setImageResource(R.drawable.ic_priority);

        } else { //TODO If the Task is not a priority...

            //TODO Set the priorityView ImageView with the non-priority icon
            holder.priorityView.setImageResource(R.drawable.ic_not_priority);

        }

        //TODO If there is no due date...
        if (dueDateMillis == Long.MAX_VALUE) {

            //TODO Display the "Not Set" text in the dateView TextView
            holder.dateView.setText(R.string.NotSet);

        } else { //TODO If there is a due date...

            //TODO Format the dueDate
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = formatter.format(new Date(dueDateMillis));
            //TODO Make the dateView TextView visible
            holder.dateView.setVisibility(View.VISIBLE);

            //TODO Set the date of the Task on the dateView TextView
            holder.dateView.setText(dateString);
        }

        //TODO Set the description of the Task on the nameView TextView
        holder.nameView.setText(description);

    }

    @Override
    public int getItemCount() {
        return (mCursor != null) ? mCursor.getCount() : 0;
    }

    /**
     * Retrieve a {@link Task} for the data at the given position.
     *
     * @param position Adapter item position.
     *
     * @return A new {@link Task} filled with the position's attributes.
     */
    public Task getItem(int position) {
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Invalid item position requested");
        }

        return new Task(mCursor);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    public void swapCursor(Cursor cursor) {
        //TODO Get the new cursor object passed into the method
        // and persist it as a field of the class
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = cursor;

        //TODO Notify the adapter that the data has changed
        if ( cursor != null) {
            notifyDataSetChanged();
        }
    }
}

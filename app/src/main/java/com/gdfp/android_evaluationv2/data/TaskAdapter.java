package com.gdfp.android_evaluationv2.data;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.developer.taskmaker.R;
import com.google.developer.taskmaker.views.TaskTitleView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

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

    private Cursor mCursor;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    public TaskAdapter(Cursor cursor) {
        mCursor = cursor;
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


        //TODO Get the column index of the checkbox


        //TODO Get the column index of the priority


        //TODO Get the column index of the dueDate


        //TODO Get the column index of the taskDescription


        //TODO Move cursor to position


        //TODO Get the value of id from the cursor


        //TODO Get the value of isComplete from the cursor


        //TODO Get the value of priority from the cursor


        //TODO Get the value of dueDate from the cursor


        //TODO Get the value of taskDescription from the cursor


        //TODO Set a tag on the itemView with the id of the Task


        //TODO If the Task is done...
        if (isComplete == 1) {

            //TODO Check the checkbox

            //TODO Set the state of the nameView to "DONE"


            //TODO Strike through the text of th nameView

            //TODO If the Task is overdue...
        } else if (dueDate < System.currentTimeMillis()) {

            //TODO Set the state of the nameView to "OVERDUE"

            //TODO un-Check the checkbox


        } else { //TODO If the Task is normal...

            //TODO Set the state of the nameView to "NORMAL"


            //TODO un-Check the checkbox
;
        }

        //TODO If the Task is a priority...
        if (priority == 1) {

            //TODO Set the priorityView ImageView with the priority icon


        } else { //TODO If the Task is not a priority...

            //TODO Set the priorityView ImageView with the non-priority icon

        }

        //TODO If there is no due date...
        if (dueDate == Long.MAX_VALUE) {

            //TODO Display the "Not Set" text in the dateView TextView

        } else { //TODO If there is a due date...

            //TODO Format the dueDate

            //TODO Make the dateView TextView visible

            //TODO Set the date of the Task on the dateView TextView
        }

        //TODO Set the description of the Task on the nameView TextView

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

        //TODO Notify the adapter that the data has changed
    }
}

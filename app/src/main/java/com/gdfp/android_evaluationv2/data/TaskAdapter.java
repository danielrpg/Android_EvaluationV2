package com.gdfp.android_evaluationv2.data;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gdfp.android_evaluationv2.R;
import com.gdfp.android_evaluationv2.views.TaskTitleView;

import java.sql.Date;
import java.text.SimpleDateFormat;

import static android.content.ContentValues.TAG;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private Cursor mCursor;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    public TaskAdapter(Cursor cursor) {
        mCursor = cursor;
    }

    /* Callback for list item click events */
    public interface OnItemClickListener {
        void onItemClick(View v, int position);

        void onItemToggled(boolean active, int position);

    }

    /* ViewHolder for each task item */
    public class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TaskTitleView nameView;
        TextView dateView;
        ImageView priorityView;
        CheckBox checkBox;


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

        int indexId = mCursor.getColumnIndex(DatabaseContract.TaskColumns._ID);
        int indexCheckbox = mCursor.getColumnIndex(DatabaseContract.TaskColumns.IS_COMPLETE);
        int indexPriority = mCursor.getColumnIndex(DatabaseContract.TaskColumns.IS_PRIORITY);
        int indexDueDate = mCursor.getColumnIndex(DatabaseContract.TaskColumns.DUE_DATE);
        int indexDescription = mCursor.getColumnIndex(DatabaseContract.TaskColumns.DESCRIPTION);

        mCursor.moveToPosition(position);

        long id = mCursor.getLong(indexId);
        int isComplete = mCursor.getInt(indexCheckbox);
        int isPriority = mCursor.getInt(indexPriority);
        long dueDate = mCursor.getLong(indexDueDate);
        String description = mCursor.getString(indexDescription);

        holder.itemView.setTag(id);
        if (isComplete == 1) {
            holder.checkBox.setChecked(true);
            holder.nameView.setState(TaskTitleView.DONE);
            holder.nameView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else if (dueDate < System.currentTimeMillis()) {
            holder.nameView.setState(TaskTitleView.OVERDUE);
            holder.checkBox.setChecked(false);
        } else {
            holder.nameView.setState(TaskTitleView.NORMAL);
            holder.checkBox.setChecked(false);
        }
        if (isPriority == 1) {
            holder.priorityView.setImageResource(R.drawable.ic_priority);
        } else {
            holder.priorityView.setImageResource(R.drawable.ic_not_priority);
        }

        if (dueDate == Long.MAX_VALUE) {
            holder.dateView.setText(R.string.date_empty);

        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = formatter.format(new Date(dueDate));

            holder.dateView.setVisibility(View.VISIBLE);
            holder.dateView.setText(dateString);
        }
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
        if (mCursor != null) {
            mCursor.close();
        }
        if (mCursor != cursor) {
            notifyDataSetChanged();
        }
        mCursor = cursor;
    }
}


package com.example.worklist.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.worklist.AddNewTask;
import com.example.worklist.Model.WorkListModel;
import com.example.worklist.R;
import com.example.worklist.Utils.DatabaseConnection;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<WorkListModel> task_list;
    Context context;
    DatabaseConnection db;

    public CustomAdapter(Context context, List<WorkListModel> task_list,DatabaseConnection db){
        this.task_list = task_list;
        this.context=context;
        this.db=db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(view);
    }

    private boolean isChecked(int value) {
        return value == 1;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDatabase();

        WorkListModel workListModel = task_list.get(position);
        holder.checkBox.setText(workListModel.getTask());
        holder.checkBox.setChecked(isChecked(workListModel.getStatus()));

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(workListModel.getId(), 1);
                } else {
                    db.updateStatus(workListModel.getId(), 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return task_list != null ? task_list.size() : 0;
    }
    public Context getContext() {

        return context;
    }

    public void setTasks(List<WorkListModel> todoList) {
        this.task_list = todoList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        WorkListModel item = task_list.get(position);
        db.deleteTask(item.getId());
        task_list.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        WorkListModel item = task_list.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
        } else {
            throw new IllegalStateException("Context is not an AppCompatActivity");
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBoxId);
        }
    }
}

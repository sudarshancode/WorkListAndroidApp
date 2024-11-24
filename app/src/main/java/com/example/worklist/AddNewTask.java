package com.example.worklist;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.worklist.Model.WorkListModel;
import com.example.worklist.Utils.DatabaseConnection;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "hello";
    EditText newTask;
    Button saveButton;
    DatabaseConnection db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }
    @Override
    public void onCreate(Bundle savedInstanceStates){
        super.onCreate(savedInstanceStates);
        setStyle(STYLE_NORMAL,R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.new_task,container,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle saveInstanceStates){
        super.onViewCreated(view,saveInstanceStates);

        newTask=getView().findViewById(R.id.editTextId);
        saveButton=getView().findViewById(R.id.saveButtonId);

        db= new DatabaseConnection(getContext());

        db.openDatabase();

        boolean isUpdate=false;
        Bundle bundle=getArguments();

        if(bundle != null){
           isUpdate=true;
            String task=bundle.getString("task");
            newTask.setText(task);
            if(task.length()>0){
                saveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
            }
        }

        newTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    saveButton.setEnabled(false);
                    saveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.gray));
                }else{
                    saveButton.setEnabled(true);
                    saveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task=newTask.getText().toString();
                if(finalIsUpdate){
                    db.updateTask(bundle.getInt("id"),task);
                }else{
                    WorkListModel workListModel=new WorkListModel();
                    workListModel.setTask(task);
                    workListModel.setStatus(0);
                    db.insertTask(workListModel);
                }
                dismiss();
            }
        });
    }
    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity=getActivity();
        if(activity instanceof DialogCloseListner){
            ((DialogCloseListner)activity).HandleDialogClose(dialog);
        }
    }


}

package com.example.worklist.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.worklist.Model.WorkListModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection extends SQLiteOpenHelper {
    public static final int VERSION=2;
    public static final String DATABASE="workListStore.db";
    public static final String TODO_TABLE="TodoTable";
    public static final String ID="id";
    public static final String TASK="task";
    public static final String STATUS="status";
    public static final String CREATE_TABLE="CREATE TABLE "+TODO_TABLE+ " ( "+ID+ " INTEGER PRIMARY KEY AUTOINCREMENT , "+TASK+" TEXT, "+STATUS+" INTEGER )";
    SQLiteDatabase db;
    public DatabaseConnection(@Nullable Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TODO_TABLE);
        onCreate(db);
    }

    public void openDatabase(){
        db=this.getWritableDatabase();
    }

    public void insertTask(WorkListModel task){
        ContentValues contentValues=new ContentValues();

        contentValues.put(TASK,task.getTask());
        contentValues.put(STATUS,0);

        db.insert(TODO_TABLE,null,contentValues);
    }

    @SuppressLint({"Range", "Recycle"})
    public List<WorkListModel> getAllTasks(){
        List<WorkListModel> taskList=new ArrayList<>();

        Cursor cursor=null;

        db.beginTransaction();
        try{
            cursor=db.query(TODO_TABLE,null,null,null,null,null,null,null);
            if(cursor!=null){
                if(cursor.moveToFirst()){
                    do{
                        WorkListModel task=new WorkListModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(TASK)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));

                        taskList.add(task);
                    }while(cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            assert cursor != null;
            cursor.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues contentValues=new ContentValues();

        contentValues.put(STATUS,status);
        db.update(TODO_TABLE,contentValues,ID+ "=?", new String[]{ String.valueOf(id) });

    }

    public void updateTask(int id,String task){
        ContentValues contentValues=new ContentValues();

        contentValues.put(TASK,task);
        db.update(TODO_TABLE,contentValues,ID + "=?",new String[] {String.valueOf(id)});

    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE,ID + "=?",new String[] {String.valueOf(id)});
    }

}

package com.example.worklist.Model;

public class WorkListModel {
    int id,status;
    String task;
    public WorkListModel(){

    }

    public void setId(int id){
        this.id=id;
    }
    public  void setStatus(int status){
        this.status=status;
    }
    public void setTask(String task){
        this.task=task;
    }

    public int getId(){
        return id;
    }
    public int getStatus(){
        return status;
    }
    public String getTask(){
        return task;
    }
}

package com.talentjoko.leaveappjsc.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.talentjoko.leaveappjsc.R;
import com.talentjoko.leaveappjsc.model.Todo;
import com.talentjoko.leaveappjsc.model.TodoDao;
import com.talentjoko.leaveappjsc.model.TodoDataBase;

import java.util.List;

public  class HelperClass {
    //DbHelper

    TodoDataBase todoDataBase;
    TodoDao dao;
    public HelperClass(TodoDataBase todoDataBase, TodoDao dao) {
        this.todoDataBase = todoDataBase;
        this.dao = dao;
    }

    //insert

    public  void  insertTodo(Todo todo){

               dao.insert(todo);


    }

    //update
    public  void  updateTodo(Todo todo){
        new Runnable() {
            @Override
            public void run() {
                //init dao and perform operation
                dao.update(todo);
            }
        };

    }


    //delete
    public  void  deleteTodo(Todo todo){

        new Runnable() {
            @Override
            public void run() {
                dao.delete(todo);
            }
        };
    }

    public List<Todo> getAllTodo(){


                //get all todos
                List<Todo> todos= dao.getAllTodos();
                return  todos;

    }
    public  Todo getTodo(int todoId){
        //get single user by id
        Todo  todo = dao.getTodo(todoId);
        return todo;

    }



}

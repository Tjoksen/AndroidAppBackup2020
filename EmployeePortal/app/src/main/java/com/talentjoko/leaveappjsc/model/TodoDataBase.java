package com.talentjoko.leaveappjsc.model;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Todo.class}, version = 1, exportSchema = false)
public abstract class TodoDataBase extends RoomDatabase {
    public abstract TodoDao todoDao();

    private static TodoDataBase INSTANCE;

    public static TodoDataBase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TodoDataBase.class, "todo-database").build();
        }
        return INSTANCE;
    }






    public static void destroyInstance() {
        INSTANCE = null;
    }



}
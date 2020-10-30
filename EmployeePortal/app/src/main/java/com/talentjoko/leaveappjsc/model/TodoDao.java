package com.talentjoko.leaveappjsc.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDao {
        @Insert
        Long insert(Todo todo);

        @Query("SELECT * FROM `Todo` ORDER BY `id` DESC")
        List<Todo> getAllTodos();

        @Query("SELECT * FROM `Todo` WHERE `id` =:id")
        Todo getTodo(int id);

        @Update
        void update(Todo todo);

        @Delete
        void delete(Todo todo);

}

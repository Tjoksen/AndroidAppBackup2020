package com.talentjoko.todoapplication.domain;

import com.google.firebase.database.Query;
import com.talentjoko.todoapplication.domain.entities.ToDo;
import com.talentjoko.todoapplication.domain.entities.User;

import io.reactivex.Observable;

public interface Repository {

    Observable<User> getCurrUser();
    Observable<ToDo> getTodo(String todoKey);
    Observable<Object> editTodo(String todoKey, String title, String description, boolean completed);

    Query getQueryForUserTodos();
}

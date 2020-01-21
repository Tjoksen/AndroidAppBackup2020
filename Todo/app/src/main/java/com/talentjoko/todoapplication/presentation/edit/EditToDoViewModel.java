package com.talentjoko.todoapplication.presentation.edit;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.talentjoko.todoapplication.domain.usecases.EditToDoUseCase;
import com.talentjoko.todoapplication.domain.usecases.GetToDoUseCase;
import com.talentjoko.todoapplication.presentation.common.BaseViewModel;

import io.reactivex.annotations.Nullable;

import static com.talentjoko.todoapplication.presentation.edit.EditToDoViewState.COMPLETED;
import static com.talentjoko.todoapplication.presentation.edit.EditToDoViewState.ERROR;
import static com.talentjoko.todoapplication.presentation.edit.EditToDoViewState.LOADING;
import static com.talentjoko.todoapplication.presentation.edit.EditToDoViewState.SUCCESS;
import static com.talentjoko.todoapplication.presentation.edit.EditToDoViewState.SUCCESS_LOADING_TODO;

public class EditToDoViewModel extends BaseViewModel {

    private EditToDoUseCase editToDoUseCase;
    private GetToDoUseCase getToDoUseCase;
    public MutableLiveData<EditToDoViewState> editTodoLiveData = new MutableLiveData<>();
    public MutableLiveData<EditToDoViewState> getTodoLiveData = new MutableLiveData<>();

    public EditToDoViewModel(GetToDoUseCase getToDoUseCase, EditToDoUseCase editToDoUseCase) {
        this.editToDoUseCase = editToDoUseCase;
        this.getToDoUseCase = getToDoUseCase;
    }

    public void editToDo(@Nullable String todoKey, String title, String description, boolean completed) {
        editTodoLiveData.setValue(new EditToDoViewState(LOADING));
        addDisposable(editToDoUseCase.editTodo(todoKey, title, description, completed)
                .subscribe(
                        nextEvent -> {
                            /*
                             In offline mode OnSuccessListener will not fire until again in online mode.
                             Because of that we do not wait for this event, but trigger editToDoLiveData change immediately.
                              */
                        },
                        error -> {
                            EditToDoViewState editToDoViewState = new EditToDoViewState(ERROR);
                            editToDoViewState.throwable = error;
                            editTodoLiveData.setValue(editToDoViewState);
                        },
                        () -> editTodoLiveData.setValue(new EditToDoViewState(COMPLETED))));
        editTodoLiveData.setValue(new EditToDoViewState(SUCCESS));
    }


    public void getToDo(@NonNull String todoKey) {
        getTodoLiveData.setValue(new EditToDoViewState(LOADING));
        addDisposable(getToDoUseCase.getTodo(todoKey)
                .subscribe(
                        todo -> {
                            EditToDoViewState editToDoViewState = new EditToDoViewState(SUCCESS_LOADING_TODO);
                            editToDoViewState.todo = todo;
                            getTodoLiveData.setValue(editToDoViewState);
                        },
                        error -> {
                            EditToDoViewState editToDoViewState = new EditToDoViewState(ERROR);
                            editToDoViewState.throwable = error;
                            editTodoLiveData.setValue(editToDoViewState);
                        },
                        () -> editTodoLiveData.setValue(new EditToDoViewState(COMPLETED))
                )
        );
    }


}

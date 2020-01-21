package com.talentjoko.todoapplication.presentation.view;


import androidx.lifecycle.MutableLiveData;

import com.talentjoko.todoapplication.domain.usecases.GetToDoUseCase;
import com.talentjoko.todoapplication.presentation.common.BaseViewModel;

import static com.talentjoko.todoapplication.presentation.view.GetToDoViewState.COMPLETED;
import static com.talentjoko.todoapplication.presentation.view.GetToDoViewState.ERROR;
import static com.talentjoko.todoapplication.presentation.view.GetToDoViewState.LOADING;
import static com.talentjoko.todoapplication.presentation.view.GetToDoViewState.SUCCESS;

public class ToDoDetailViewModel extends BaseViewModel {

    private GetToDoUseCase getToDoUseCase;
    public MutableLiveData<GetToDoViewState> getTodoLiveData = new MutableLiveData<>();

    public ToDoDetailViewModel(GetToDoUseCase getToDoUseCase) {
        this.getToDoUseCase = getToDoUseCase;
    }

    public void getToDo(String todoKey) {
        getTodoLiveData.setValue(new GetToDoViewState(LOADING));
        addDisposable(getToDoUseCase.getTodo(todoKey)
                .subscribe(
                        todo -> {
                            GetToDoViewState getToDoViewState = new GetToDoViewState(SUCCESS);
                            getToDoViewState.todo = todo;
                            getTodoLiveData.setValue(getToDoViewState);
                        },
                        error -> {
                            GetToDoViewState getToDoViewState = new GetToDoViewState(ERROR);
                            getToDoViewState.throwable = error;
                            getTodoLiveData.setValue(getToDoViewState);
                        },
                        () -> getTodoLiveData.setValue(new GetToDoViewState(COMPLETED))
                )
        );
    }
}

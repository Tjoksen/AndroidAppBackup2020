package com.talentjoko.todoapplication.presentation.view;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.talentjoko.todoapplication.domain.usecases.GetToDoUseCase;

@SuppressWarnings("unchecked")
public class ToDoDetailVMFactory implements ViewModelProvider.Factory {

    private GetToDoUseCase getToDoUseCase;

    public ToDoDetailVMFactory(GetToDoUseCase getToDoUseCase) {
        this.getToDoUseCase = getToDoUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ToDoDetailViewModel(getToDoUseCase);
    }
}
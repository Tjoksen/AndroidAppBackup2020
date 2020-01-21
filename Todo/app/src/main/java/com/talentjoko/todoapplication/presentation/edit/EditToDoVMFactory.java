package com.talentjoko.todoapplication.presentation.edit;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.talentjoko.todoapplication.domain.usecases.EditToDoUseCase;
import com.talentjoko.todoapplication.domain.usecases.GetToDoUseCase;

@SuppressWarnings("unchecked")
public class EditToDoVMFactory implements ViewModelProvider.Factory {

    private EditToDoUseCase editToDoUseCase;
    private GetToDoUseCase getToDoUseCase;

    public EditToDoVMFactory(GetToDoUseCase getToDoUseCase, EditToDoUseCase editToDoUseCase) {
        this.editToDoUseCase = editToDoUseCase;
        this.getToDoUseCase = getToDoUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EditToDoViewModel(getToDoUseCase, editToDoUseCase);
    }
}

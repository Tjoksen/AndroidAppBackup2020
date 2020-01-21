package com.talentjoko.todoapplication.presentation.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.talentjoko.todoapplication.domain.usecases.GetCurrentUserUseCase;

@SuppressWarnings("unchecked")
public class ListVMFactory  implements ViewModelProvider.Factory {

    private GetCurrentUserUseCase getCurrentUserUseCase;

    public ListVMFactory(GetCurrentUserUseCase getCurrentUserUseCase) {
        this.getCurrentUserUseCase = getCurrentUserUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ListViewModel(getCurrentUserUseCase);
    }
}

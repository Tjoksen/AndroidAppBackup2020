package com.talentjoko.todoapplication.presentation.list;

import androidx.lifecycle.MutableLiveData;

import com.talentjoko.todoapplication.domain.usecases.GetCurrentUserUseCase;
import com.talentjoko.todoapplication.presentation.common.BaseViewModel;

import static com.talentjoko.todoapplication.presentation.list.ListViewState.COMPLETED;
import static com.talentjoko.todoapplication.presentation.list.ListViewState.ERROR;
import static com.talentjoko.todoapplication.presentation.list.ListViewState.LOADING;
import static com.talentjoko.todoapplication.presentation.list.ListViewState.SUCCESS;

public class ListViewModel extends BaseViewModel {

    private GetCurrentUserUseCase getCurrentUserUseCase;
    public MutableLiveData<ListViewState> listLiveData = new MutableLiveData<>();

    public ListViewModel(GetCurrentUserUseCase getCurrentUserUseCase) {
        this.getCurrentUserUseCase = getCurrentUserUseCase;
    }


    public void getCurrentUser() {
        listLiveData.setValue(new ListViewState(LOADING));
        addDisposable(getCurrentUserUseCase.getCurrentUser()
                .subscribe(
                        user -> {
                            ListViewState listViewState = new ListViewState(SUCCESS);
                            listLiveData.setValue(listViewState);
                        },
                        error -> {
                            ListViewState listViewState = new ListViewState(ERROR);
                            listViewState.throwable = error;
                            listLiveData.setValue(listViewState);
                        },
                        () -> listLiveData.setValue(new ListViewState(COMPLETED))
                )
        );
    }
}

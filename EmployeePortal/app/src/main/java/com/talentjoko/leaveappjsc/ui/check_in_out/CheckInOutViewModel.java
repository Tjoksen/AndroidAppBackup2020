package com.talentjoko.leaveappjsc.ui.check_in_out;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CheckInOutViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CheckInOutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is check-in-out fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
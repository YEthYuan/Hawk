package com.example.mobileobjectdetection.ui.face;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FaceViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public FaceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
package com.example.kuriersharp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
//        mText.setValue("This is home fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }
    public void setText(String text) {
        mText.setValue(text); // Teraz setValue jest wywoływane w ViewModel, gdzie ma dostęp
    }
}
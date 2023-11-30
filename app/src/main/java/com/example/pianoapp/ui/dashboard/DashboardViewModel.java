package com.example.pianoapp.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pianoapp.R;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<Integer> mImageResource;
    private final MutableLiveData<String> mData;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");

        // Example image resource (you can replace it with your actual image resource)
        mImageResource = new MutableLiveData<>();
        mImageResource.setValue(R.drawable.p11);

        // Example data (replace it with your actual data)
        mData = new MutableLiveData<>();
        mData.setValue("Some data for the dashboard");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Integer> getImageResource() {
        return mImageResource;
    }

    public LiveData<String> getData() {
        return mData;
    }
}

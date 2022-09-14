package com.lvovds.simplemessenger;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<FirebaseAuth> auth = new MutableLiveData<>();
    private FirebaseAuth mAuth;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
    }




}

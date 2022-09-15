package com.lvovds.simplemessenger;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private MutableLiveData<String> errorText = new MutableLiveData<>();
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    public RegisterViewModel() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() !=null) {
                    user.setValue(firebaseAuth.getCurrentUser());
                }
            }
        });
    }

    public void createAccount(String email,String pass,String name,String latName,int age) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        errorText.setValue(e.getMessage());
                    }
                });
    }

    public LiveData<String> getErrorText() {
        return errorText;
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }
}

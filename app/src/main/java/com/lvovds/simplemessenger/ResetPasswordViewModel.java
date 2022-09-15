package com.lvovds.simplemessenger;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private MutableLiveData<String> errorText = new MutableLiveData<>();
    private MutableLiveData<Boolean> success = new MutableLiveData<>();

    public ResetPasswordViewModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                success.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                errorText.setValue(e.getMessage());
            }
        });
    }

    public LiveData<String> getErrorText() {
        return errorText;
    }

    public LiveData<Boolean> getSuccess() {
        return success;
    }
}

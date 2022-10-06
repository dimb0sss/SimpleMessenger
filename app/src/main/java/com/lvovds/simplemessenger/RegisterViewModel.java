package com.lvovds.simplemessenger;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class RegisterViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersReference;
    private String tokenId;

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
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference("Users");
    }

    public void createAccount(String email,String pass,String name,String lastName,int age) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = authResult.getUser();
                        if (firebaseUser==null) {
                            return;
                        }
                        FirebaseMessaging.getInstance().getToken()
                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        if (!task.isSuccessful()) {
                                            return;
                                        }
                                        // Get new FCM registration token
                                        String token = task.getResult();

                                        // Log and toast
                                        Log.d("MainActivity", token);
                                        PushService pushService = new PushService();
                                        pushService.onNewToken(token);
                                        tokenId=token;


                                    }
                                });

                        User user = new User(firebaseUser.getUid(),name,lastName,age,false,false,tokenId,"");
                        usersReference.child(firebaseUser.getUid()).setValue(user);
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

    public LiveData<FirebaseUser> getUser() {
        return user;
    }
}

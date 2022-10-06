package com.lvovds.simplemessenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private MutableLiveData<User> userInfo = new MutableLiveData<>();
    private MutableLiveData<Boolean> editionSent = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersReference;

    public SettingsViewModel() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference("Users");
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser==null) {
                    return;
                }
                for (DataSnapshot dataSnapshot:snapshot.getChildren()
                ) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user==null) {
                        return;
                    }
                    if (currentUser.getUid().equals(user.getId())) {
                        userInfo.setValue(user);
                        break;
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void changeUserInfo(String name,String lastName, int age) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser==null) {
            return;
        }
        usersReference.child(firebaseUser.getUid()).child("name").setValue(name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        editionSent.setValue(true);
                        usersReference.child(firebaseUser.getUid()).child("lastName").setValue(lastName)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        editionSent.setValue(true);
                                        usersReference.child(firebaseUser.getUid()).child("age").setValue(age)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        editionSent.setValue(true);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        error.setValue(e.getMessage());
                                                        editionSent.setValue(false);
                                                    }
                                                });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        error.setValue(e.getMessage());
                                        editionSent.setValue(false);
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error.setValue(e.getMessage());
                        editionSent.setValue(false);
                    }
                });


    }

    public LiveData<User> getUserInfo() {
        return userInfo;
    }

    public LiveData<Boolean> getEditionSent() {
        return editionSent;
    }

    public LiveData<String> getError() {
        return error;
    }
}

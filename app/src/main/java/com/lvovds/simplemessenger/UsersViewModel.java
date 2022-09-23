package com.lvovds.simplemessenger;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class UsersViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private MutableLiveData<String> errorText = new MutableLiveData<>();
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private MutableLiveData<List<User>> userList = new MutableLiveData<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersReference;

    public UsersViewModel() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    user.setValue(firebaseAuth.getCurrentUser());
            }
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference("Users");

        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser==null) {
                    return;
                }
                List<User> users = new ArrayList<>();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()
                     ) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user==null) {
                        return;
                    }
                    if (!currentUser.getUid().equals(user.getId())) {
                        users.add(user);
                    }

                }
                userList.setValue(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setUserOnline(boolean isOnline) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser==null) {
            return;
        }
        usersReference.child(firebaseUser.getUid()).child("online").setValue(isOnline);
    }

    public void setUserTokenId() {

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
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser==null) {
                            return;
                        }
                        usersReference.child(firebaseUser.getUid()).child("token").setValue(token);

                    }
                });

    }

    public void logOut() {
        setUserOnline(false);
        mAuth.signOut();
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public LiveData<List<User>> getUserList() {
        return userList;
    }
}

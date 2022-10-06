package com.lvovds.simplemessenger;

import static com.lvovds.simplemessenger.Time.currentDate;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lvovds.simplemessenger.notification.MessageSend;
import com.lvovds.simplemessenger.notification.Notification;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChatViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    private MutableLiveData<User> otherUser = new MutableLiveData<>();
    private MutableLiveData<Boolean> messageSent = new MutableLiveData<>();
    private MutableLiveData<Boolean> messageDelete = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference usersReference = firebaseDatabase.getReference("Users");
    private DatabaseReference referenceMessages = firebaseDatabase.getReference("Messages");


    private String currentUserId;
    private String otherUserId;
    private User userInfo = new User();
    private User otherUserInfo = new User();


    public ChatViewModel(String currentUserId, String otherUserId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
        usersReference.child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                otherUser.setValue(user);
                otherUserInfo = user;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        usersReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userInfo = user;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        referenceMessages.child(currentUserId).child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Message> messageList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    messageList.add(message);
                }
                messages.setValue(messageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setUserOnline(boolean isOnline) {
        usersReference.child(currentUserId).child("online").setValue(isOnline);
        if (!isOnline) {
            String lastOnlineInfo = currentDate();
            usersReference.child(currentUserId).child("lastOnlineInfo").setValue(lastOnlineInfo);
        }
    }

    public void setUserTyping(boolean isTyping) {
        usersReference.child(currentUserId).child("typing").setValue(isTyping);
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public LiveData<User> getOtherUser() {
        return otherUser;
    }

    public LiveData<Boolean> getMessageSent() {
        return messageSent;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getMessageDelete() {
        return messageDelete;
    }

    public void sendMessage(Message message) {
//        String aesText = "";
//        try {
//            aesText = Aes256Class.encrypt(message.getText(),Aes256Class.getKeyFromPassword(currentUserId));
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
//        message.setText(aesText);
        referenceMessages
                .child(message.getSendId())
                .child(message.getReceiverId())
                .child(message.getMessageId())
                .setValue(message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        referenceMessages
                                .child(message.getReceiverId())
                                .child(message.getSendId())
                                .child(message.getMessageId())
                                .setValue(message)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        messageSent.setValue(true);
                                        Disposable disposable = ApiFactory.apiService
                                                .messageResponse(new MessageSend(otherUserInfo.getToken()
                                                        , new Notification(message.getText(), userInfo.getName() + " " + userInfo.getLastName())))
                                                .subscribeOn(Schedulers.io())
                                                .subscribe(new Consumer<String>() {
                                                    @Override
                                                    public void accept(String s) throws Throwable {

                                                    }
                                                }, new Consumer<Throwable>() {
                                                    @Override
                                                    public void accept(Throwable throwable) throws Throwable {
                                                        Log.d("MainActivity", throwable.getMessage());
                                                    }
                                                });
                                        compositeDisposable.add(disposable);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        error.setValue(e.getMessage());
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error.setValue(e.getMessage());
                    }
                });

    }

    public void removeMessageForAllUsers(Message message) {

        referenceMessages.child(message.getSendId())
                .child(message.getReceiverId())
                .child(message.getMessageId())
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        referenceMessages
                                .child(message.getReceiverId())
                                .child(message.getSendId())
                                .child(message.getMessageId())
                                .removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        messageDelete.setValue(true);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        error.setValue(e.getMessage());
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error.setValue(e.getMessage());
                    }
                });
    }

    public void removeMessageForMe(Message message) {
        referenceMessages
                .child(currentUserId)
                .child(otherUserId)
                .child(message.getMessageId())
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        messageDelete.setValue(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error.setValue(e.getMessage());
                    }
                });
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}

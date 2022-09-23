package com.lvovds.simplemessenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.lvovds.simplemessenger.notification.MessageSend;
import com.lvovds.simplemessenger.notification.Notification;

import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChatActivity extends AppCompatActivity {
    private static final String EXTRA_CURRENT_USER_ID = "current_id";
    private static final String EXTRA_OTHER_USER_ID = "other_id";

    private TextView textViewTitle;
    private View onlineStatus;
    private RecyclerView recyclerViewMessages;
    private EditText editText;
    private ImageView imageViewSendMessage;

    private MessagesAdapter adapter;

    private String currentUserid;
    private String otherUserid;

    private ChatViewModel chatViewModel;
    private ChatViewModelFactory chatViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        currentUserid = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        otherUserid = getIntent().getStringExtra(EXTRA_OTHER_USER_ID);
        chatViewModelFactory = new ChatViewModelFactory(currentUserid,otherUserid);
        chatViewModel = new ViewModelProvider(this,chatViewModelFactory).get(ChatViewModel.class);
        adapter = new MessagesAdapter(currentUserid);
        recyclerViewMessages.setAdapter(adapter);
        observeViewModel();
        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Пустое сообщение"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    Message message = new Message(editText.getText()
                            .toString().trim(),currentUserid,otherUserid);
                    chatViewModel.sendMessage(message);
                    recyclerViewMessages.smoothScrollToPosition(adapter.getItemCount());
                    Log.d("MainActivity", String.valueOf(adapter.getItemCount()));

                }

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        chatViewModel.setUserOnline(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatViewModel.setUserOnline(true);
    }

    private void observeViewModel() {
        chatViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(ChatActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        chatViewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
               adapter.setMessages(messages);
            }
        });
        chatViewModel.getMessageSent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!editText.getText().toString().isEmpty()) {
                    editText.getText().clear();
                }
            }
        });
        chatViewModel.getOtherUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String userInfo = String.format("%s %s",user.getName(),user.getLastName());
                textViewTitle.setText(userInfo);
                int bgResid;
                if (user.isOnline()) {
                    bgResid = R.drawable.light_online;
                } else {
                    bgResid = R.drawable.light_offline;
                }
                Drawable background = ContextCompat.getDrawable(ChatActivity.this,bgResid);
                onlineStatus.setBackground(background);
            }
        });
    }

    private void initViews() {
        textViewTitle = findViewById(R.id.textViewTitle);
        onlineStatus = findViewById(R.id.onlineStatus);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editText = findViewById(R.id.editTextMessage);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);
    }

    public static Intent newIntent(Context context,String currentUserid,String otherUserid){
        Intent intent = new Intent(context,ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID,currentUserid);
        intent.putExtra(EXTRA_OTHER_USER_ID,otherUserid);
        return intent;
    }
}
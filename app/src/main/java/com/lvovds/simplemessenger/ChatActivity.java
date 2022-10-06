package com.lvovds.simplemessenger;

import static com.lvovds.simplemessenger.Time.currentDate;
import static com.lvovds.simplemessenger.Time.lastOnlineStatus;
import static com.lvovds.simplemessenger.Time.lastOnlineTime;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity {
    private static final String EXTRA_CURRENT_USER_ID = "current_id";
    private static final String EXTRA_OTHER_USER_ID = "other_id";

    private TextView textViewTitle;
    private View onlineStatus;
    private RecyclerView recyclerViewMessages;
    private EditText editText;
    private ImageView imageViewSendMessage;
    private TextView textViewLastOnlineInfo;
    private List<Message> messagesList = new ArrayList<>();
    private Message deletingMessage;


    private Timer timer = new Timer();
    private final long DELAY = 1000; // Milliseconds


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
        chatViewModelFactory = new ChatViewModelFactory(currentUserid, otherUserid);
        chatViewModel = new ViewModelProvider(this, chatViewModelFactory).get(ChatViewModel.class);
        adapter = new MessagesAdapter(currentUserid);
        recyclerViewMessages.setAdapter(adapter);
//        adapter.setOnMessageClickListener(new MessagesAdapter.OnMessageClickListener() {
//            @Override
//            public void onNoteClick(Message message) {
//
//            }
//        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                chatViewModel.setUserTyping(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                chatViewModel.setUserTyping(false);
                            }
                        },
                        DELAY);
            }
        });
        observeViewModel();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper
                .SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView
                    , @NonNull RecyclerView.ViewHolder viewHolder
                    , @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Message message = messagesList.get(position);
                deletingMessage = message;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentManager manager = getSupportFragmentManager();
                        DialogFragmentDeleteForMe dfDeleteForMe = new DialogFragmentDeleteForMe();
                        DialogFragmentDeleteForAll dfDeleteForAll = new DialogFragmentDeleteForAll();
                        System.out.println(message.getSendingTime().substring(0, 5));
                        System.out.println(Time.currentDate().substring(0, 5));
                        if (message.getSendId().equals(currentUserid) &&
                                (message.getSendingTime().substring(0, 5)
                                        .equals(currentDate().substring(0, 5)))) {
                            dfDeleteForAll.show(manager, "DeleteForAll");
                        } else {
                            dfDeleteForMe.show(manager, "DeleteForMe");
                        }

                    }
                });
                thread.start();


            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewMessages);
        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Пустое сообщение"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    Message message = new Message(editText.getText()
                            .toString().trim(), currentUserid, otherUserid, currentDate(), String.valueOf(System.currentTimeMillis()));
                    chatViewModel.sendMessage(message);
                    recyclerViewMessages.smoothScrollToPosition(adapter.getItemCount());


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
                messagesList = messages;
                adapter.notifyDataSetChanged();
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
        chatViewModel.getMessageDelete().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDelete) {
                if (isDelete) {
                    Toast.makeText(ChatActivity.this, "Сообщение удалено", Toast.LENGTH_SHORT).show();
                }
            }
        });
        chatViewModel.getOtherUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String userInfo = String.format("%s %s", user.getName(), user.getLastName());
                textViewTitle.setText(userInfo);
                int bgResid;
                String lastOnlineInfo = "";
                if (user.isTyping()) {
                    bgResid = R.drawable.light_online;
                    lastOnlineInfo = "набирает текст ...";
                } else if (!user.isTyping() && user.isOnline()) {
                    bgResid = R.drawable.light_online;
                    lastOnlineInfo = "Online";
                } else {
                    bgResid = R.drawable.light_offline;

                    Time time = lastOnlineTime(user.getLastOnlineInfo());
                    lastOnlineInfo = lastOnlineStatus(time);
                }

                Drawable background = ContextCompat.getDrawable(ChatActivity.this, bgResid);
                onlineStatus.setBackground(background);
                textViewLastOnlineInfo.setText(lastOnlineInfo);
            }
        });
    }

    private void initViews() {
        textViewTitle = findViewById(R.id.textViewTitle);
        onlineStatus = findViewById(R.id.onlineStatus);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editText = findViewById(R.id.editTextMessage);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);
        textViewLastOnlineInfo = findViewById(R.id.textViewLastOnlineInfo);
    }

    public static Intent newIntent(Context context, String currentUserid, String otherUserid) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserid);
        intent.putExtra(EXTRA_OTHER_USER_ID, otherUserid);
        return intent;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void okClicked() {
        chatViewModel.removeMessageForMe(deletingMessage);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void okClickedAll() {
        chatViewModel.removeMessageForAllUsers(deletingMessage);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void cancelClicked() {
        adapter.notifyDataSetChanged();
    }
}
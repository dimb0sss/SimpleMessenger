package com.lvovds.simplemessenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        currentUserid = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        otherUserid = getIntent().getStringExtra(EXTRA_OTHER_USER_ID);
        adapter = new MessagesAdapter(currentUserid);
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
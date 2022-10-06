package com.lvovds.simplemessenger;

import static com.lvovds.simplemessenger.Time.currentDate;
import static com.lvovds.simplemessenger.Time.timeOfSending;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private static final int VIEW_TYPE_MY_MESSAGE = 100;
    private static final int VIEW_TYPE_OTHER_MESSAGE = 101;
    private static final int VIEW_TYPE_MY_MESSAGE_WITH_HEADER = 102;
    private static final int VIEW_TYPE_OTHER_MESSAGE_WITH_HEADER = 103;

    private List<Message> messages = new ArrayList<>();
    private OnMessageClickListener onMessageClickListener;

    private String currentUserId;
    private String lastDateOfMessage = "1";
    private Boolean lastDateOfMessageChange = true;

    public MessagesAdapter(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void setOnMessageClickListener(OnMessageClickListener onMessageClickListener) {
        this.onMessageClickListener = onMessageClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResId;
        if (viewType == VIEW_TYPE_MY_MESSAGE) {
            layoutResId = R.layout.my_message_item;
        } else {
            layoutResId = R.layout.other_message_item;
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutResId, parent, false);


        return new MessageViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
      if (message.getSendId().equals(currentUserId)) {
            return VIEW_TYPE_MY_MESSAGE;
        }
        else return VIEW_TYPE_OTHER_MESSAGE;

//        if (message.getSendId().equals(currentUserId) && lastDateOfMessage == null &&lastDateOfMessageChange) {
//            lastDateOfMessage = message.getSendingTime().substring(0, 5);
//            lastDateOfMessageChange=true;
//            return VIEW_TYPE_MY_MESSAGE_WITH_HEADER;
//
//        } else if (!message.getSendId().equals(currentUserId) && lastDateOfMessage == null&&lastDateOfMessageChange) {
//            lastDateOfMessage = message.getSendingTime().substring(0, 5);
//            lastDateOfMessageChange=true;
//            return VIEW_TYPE_OTHER_MESSAGE_WITH_HEADER;
//
//        } else if (message.getSendId().equals(currentUserId)&&!lastDateOfMessageChange) {
//            return VIEW_TYPE_MY_MESSAGE;
//        } else return VIEW_TYPE_OTHER_MESSAGE;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
//        SecretKey key = null;
//        try {
//            key = Aes256Class.getKeyFromPassword(currentUserId);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
//        if (message.getSendId()==currentUserId) {
//            try {
//                key = Aes256Class.getKeyFromPassword(message.getSendId());
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (InvalidKeySpecException e) {
//                e.printStackTrace();
//            }
//        } else if (message.getSendId()!=currentUserId) {
//            try {
//                key = Aes256Class.getKeyFromPassword(message.getReceiverId());
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (InvalidKeySpecException e) {
//                e.printStackTrace();
//            }
//        }
//        String descryptText = null;
//        try {
//            descryptText = Aes256Class.decrypt(message.getText(),key);
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
//        }
        System.out.println(holder.getItemViewType());

//        if (position!=0) {
//            if (messages.get(position).getSendingTime().substring(0, 5).equals(messages.get(position - 1).getSendingTime().substring(0, 5)))
//            {  holder.textViewHeaderDate.setVisibility(View.GONE); }
//        }
//
//
//        if (currentDate().substring(0, 5).equals(message.getSendingTime().substring(0,5))) {
//            holder.textViewHeaderDate.setText("Сегодня");
////
//        } else  holder.textViewHeaderDate.setText(message.getSendingTime().substring(0,5));

        holder.textViewHeaderDate.setVisibility(View.GONE);
        holder.textViewMessage.setText(message.getText());
        if (message.getSendingTime() == null) {
            holder.sendingTime.setText("     ");
        } else {
            holder.sendingTime.setText(timeOfSending(message.getSendingTime()));
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMessage;
        private TextView sendingTime;
        private TextView textViewHeaderDate;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            sendingTime = itemView.findViewById(R.id.textViewTimeSending);
            textViewHeaderDate = itemView.findViewById(R.id.textViewHeaderDate);
        }
    }

    interface OnMessageClickListener {
        void onNoteClick(Message message);
    }


}

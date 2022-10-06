package com.lvovds.simplemessenger;

import static com.lvovds.simplemessenger.Time.lastOnlineStatus;
import static com.lvovds.simplemessenger.Time.lastOnlineTime;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private List<User> users = new ArrayList<>();
    private OnUserClickListener onUserClickListener;


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.textViewUserInfo
                .setText(String.format("%s %s, %s", user.getName(), user.getLastName(), user.getAge()));

        int bgResid;
        String lastOnlineInfo = "";
        if (user.isOnline()) {
            bgResid = R.drawable.light_online;
            lastOnlineInfo = "Online";
        } else {
            bgResid = R.drawable.light_offline;

            Time time = lastOnlineTime(user.getLastOnlineInfo());
            lastOnlineInfo = lastOnlineStatus(time);
        }

        Drawable background = ContextCompat.getDrawable(holder.itemView.getContext(), bgResid);
        holder.onlineStatus.setBackground(background);
        holder.textViewLastOnlineInfo.setText(lastOnlineInfo);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onUserClickListener != null) {
                    onUserClickListener.onUserClick(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    interface OnUserClickListener {
        void onUserClick(User user);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUserInfo;
        private View onlineStatus;
        private TextView textViewLastOnlineInfo;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserInfo = itemView.findViewById(R.id.textViewUserInfo);
            onlineStatus = itemView.findViewById(R.id.onlineStatus);
            textViewLastOnlineInfo = itemView.findViewById(R.id.textViewLastOnlineInfo);
        }
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void setOnUserClickListener(OnUserClickListener onUserClickListener) {
        this.onUserClickListener = onUserClickListener;
    }


}

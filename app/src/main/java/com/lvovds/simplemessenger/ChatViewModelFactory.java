package com.lvovds.simplemessenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChatViewModelFactory implements ViewModelProvider.Factory {
    private String currentUserId;
    private String otherUserId;

    public ChatViewModelFactory(String currentUserId, String otherCurrentId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherCurrentId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChatViewModel(currentUserId,otherUserId);
    }
}

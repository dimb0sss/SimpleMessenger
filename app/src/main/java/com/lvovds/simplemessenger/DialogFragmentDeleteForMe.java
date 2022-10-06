package com.lvovds.simplemessenger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogFragmentDeleteForMe extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = "Удалить сообщение?";
        String buttonAbort = "Отмена";
        String buttonDelete = "Удалить у меня";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setNegativeButton(buttonAbort, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((ChatActivity) getActivity()).cancelClicked();
            }
        });
        builder.setPositiveButton(buttonDelete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((ChatActivity) getActivity()).okClicked();
            }
        });
        builder.setCancelable(true);


        return builder.create();
    }
}


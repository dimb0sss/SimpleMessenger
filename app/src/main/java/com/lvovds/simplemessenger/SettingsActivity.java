package com.lvovds.simplemessenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {
    private SettingsViewModel viewModel;
    private EditText editTextName;
    private EditText editTextLastName;
    private EditText editTextOld;
    private Button buttonRegister;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initItem();
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        viewModel.getUserInfo().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                editTextName.setText(user.getName());
                editTextLastName.setText(user.getLastName());
                editTextOld.setText(String.valueOf(user.getAge()));
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name  = editTextName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                int age = Integer.parseInt(editTextOld.getText().toString().trim());
                viewModel.changeUserInfo(name,lastName,age);
                viewModel.getEditionSent().observe(SettingsActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean) {
                            finish();
                        }
                    }
                });
                viewModel.getError().observe(SettingsActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Toast.makeText(SettingsActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void initItem() {
        editTextName=findViewById(R.id.editTextName);
        editTextLastName=findViewById(R.id.editTextLastName);
        editTextOld=findViewById(R.id.editTextOld);
        buttonRegister=findViewById(R.id.buttonRegister);
    }
}
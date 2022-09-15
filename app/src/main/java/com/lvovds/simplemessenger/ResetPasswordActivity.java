package com.lvovds.simplemessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private Button buttonReset;
    private FirebaseAuth mAuth;
    private static final String LOG_TAG = "MainActivity";
    private static final String EXTRA_EMAIL = "email";
    private ResetPasswordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        viewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        initViews();
        observeViewModel();
        getIntentMethod();
        setupClickListeners();
    }

    private void getIntentMethod() {
        Intent intent = getIntent();
        initViews();
        if (intent.hasExtra(EXTRA_EMAIL)) {
            String email = intent.getStringExtra(EXTRA_EMAIL);
            Log.d(LOG_TAG,email);
            editTextEmail.setText(email);
//            Toast.makeText(LoginActivity.this, "Ссылка для сброса пароля выслана на почту", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupClickListeners() {
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextEmail.getText().toString().isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this, "Заполните email", Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.resetPassword(editTextEmail.getText().toString().trim());
                }

            }
        });
    }

    private void observeViewModel() {
        viewModel.getErrorText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error!=null) {
                    Toast.makeText(ResetPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                }

            }
        });

        viewModel.getSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success) {
                    Toast.makeText(ResetPasswordActivity.this, "Link has been send on email" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public static Intent newIntent(Context context,String email)
    {
        Intent intent = new Intent(context,ResetPasswordActivity.class);
        intent.putExtra(EXTRA_EMAIL,email);
        return intent;
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonReset = findViewById(R.id.buttonReset);
    }
}
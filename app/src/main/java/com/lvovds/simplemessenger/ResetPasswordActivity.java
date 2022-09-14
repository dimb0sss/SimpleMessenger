package com.lvovds.simplemessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mAuth = FirebaseAuth.getInstance();
        initViews();
        Intent intent = getIntent();
        initViews();
        if (intent.hasExtra("email")) {
            String email = intent.getStringExtra("email");
            Log.d(LOG_TAG,email);
            editTextEmail.setText(email);
//            Toast.makeText(LoginActivity.this, "Ссылка для сброса пароля выслана на почту", Toast.LENGTH_SHORT).show();
        }
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextEmail.getText().toString().isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this, "Заполните email", Toast.LENGTH_SHORT).show();
                } else {
                    resetPassword(editTextEmail.getText().toString());
                }

            }
        });
    }

    private void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                startActivity(LoginActivity.newIntent(ResetPasswordActivity.this,email));

            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ResetPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Intent newIntent(Context context,String email)
    {
        Intent intent = new Intent(context,ResetPasswordActivity.class);
        intent.putExtra("email",email);
        return intent;
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonReset = findViewById(R.id.buttonReset);
    }
}
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextLastName;
    private EditText editTextOld;
    private Button buttonRegister;
    private FirebaseAuth mAuth;

    private static final String LOG_TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        Intent intent = getIntent();
        initViews();
        if (intent.hasExtra("email")) {
            String email = intent.getStringExtra("email");
            Log.d(LOG_TAG,email);
            editTextEmail.setText(email);
//            Toast.makeText(LoginActivity.this, "Ссылка для сброса пароля выслана на почту", Toast.LENGTH_SHORT).show();
        }
        mAuth = FirebaseAuth.getInstance();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter email and password!", Toast.LENGTH_SHORT).show();
                } else {
                    createAccount(email,password);
                    startActivity(LoginActivity.newIntent(RegisterActivity.this,email));
                }
            }
        });



    }

    private void createAccount(String email,String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(LOG_TAG, "createUserWithEmail:success");
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser == null) {
                            Log.d(LOG_TAG, "Не авторизован");
                        } else {
                            Log.d(LOG_TAG, "Авторизован");
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextOld = findViewById(R.id.editTextOld);
        buttonRegister = findViewById(R.id.buttonRegister);
    }



    public static Intent newIntent(Context context, String email)
    {
        Intent intent = new Intent(context,RegisterActivity.class);
        intent.putExtra("email",email);
        return intent;
    }
}
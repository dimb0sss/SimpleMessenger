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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewForgot;
    private TextView textViewRegister;


    private static final String LOG_TAG = "MainActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        initViews();
        if (intent.hasExtra("email")) {
            String email = intent.getStringExtra("email");
            Log.d(LOG_TAG,email);
            editTextEmail.setText(email);
//            Toast.makeText(LoginActivity.this, "Ссылка для сброса пароля выслана на почту", Toast.LENGTH_SHORT).show();
        }
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextEmail.getText().toString().isEmpty() && !editTextPassword.getText().toString().isEmpty()) {
                    signIn(editTextEmail.getText().toString(),editTextPassword.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, "Enter email and password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent =  ResetPasswordActivity.newIntent(LoginActivity.this,editTextEmail.getText().toString());
               startActivity(intent);
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  RegisterActivity.newIntent(LoginActivity.this,editTextEmail.getText().toString());
                startActivity(intent);
            }
        });



        mAuth = FirebaseAuth.getInstance();
//        signOut();
//        resetPassword();

//        resetPassword();
//        createAccount();
//        getCurrentUser();
//        signOut();

//        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                Log.d(LOG_TAG,"Изменение статуса");
//            }
//        });
//        signIn();
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewForgot = findViewById(R.id.textViewForgot);
        textViewRegister = findViewById(R.id.textViewRegister);
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
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signIn(String email,String pass) {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser == null) {
                            Toast.makeText(LoginActivity.this, "Пользователь не авторизован", Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(LoginActivity.this,UsersActivity.class));
                            Log.d(LOG_TAG, "Авторизован");
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
    }



    private void getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
//            String name = user.getDisplayName();
            String email = user.getEmail();


            // Check if user's email is verified
//            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            Log.d(LOG_TAG, email);
            Log.d(LOG_TAG, uid);
        } else Log.d(LOG_TAG, "ccoco");
    }

    public static Intent newIntent(Context context, String email)
    {
        Intent intent = new Intent(context,LoginActivity.class);
        intent.putExtra("email",email);
        return intent;
    }

}
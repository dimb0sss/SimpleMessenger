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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextLastName;
    private EditText editTextAge;
    private Button buttonRegister;
    private FirebaseAuth mAuth;
    private RegisterViewModel viewModel;

    private static final String LOG_TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        initViews();
        observeViewModel();
        getIntentMethod();
        setupClickListeners();


    }

    private void setupClickListeners() {
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = getTrimmedValue(editTextEmail);
                String password = getTrimmedValue(editTextPassword);
                String name = getTrimmedValue(editTextName);
                String lastName = getTrimmedValue(editTextLastName);
                int age = 0;
                if (!getTrimmedValue(editTextAge).isEmpty()) {
                     age = Integer.parseInt(getTrimmedValue(editTextAge));
                }
                if (email.isEmpty() || password.isEmpty() || name.isEmpty() || lastName.isEmpty()
                        || getTrimmedValue(editTextAge).isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.createAccount(email, password, name, lastName, age);
                }
            }
        });
    }

    private void getIntentMethod() {
        Intent intent = getIntent();
        initViews();
        if (intent.hasExtra("email")) {
            String email = intent.getStringExtra("email");
            Log.d(LOG_TAG, email);
            editTextEmail.setText(email);
//            Toast.makeText(LoginActivity.this, "Ссылка для сброса пароля выслана на почту", Toast.LENGTH_SHORT).show();
        }
    }

    private String getTrimmedValue(EditText editText) {
        return editText.getText().toString().trim();
    }


    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextAge = findViewById(R.id.editTextOld);
        buttonRegister = findViewById(R.id.buttonRegister);
    }

    private void observeViewModel() {
        viewModel.getErrorText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }

        });
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    startActivity(UsersActivity.newIntent(RegisterActivity.this,firebaseUser.getUid()));
                    finish();
                }
            }
        });
    }


    public static Intent newIntent(Context context, String email) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra("email", email);
        return intent;
    }
}
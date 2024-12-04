package com.susu.bankapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText inputLogin;
    private EditText inputPassword;
    private Button btnLogin;
    private TextView signUp;
    private TextView forgotPassword;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        login();
    }

    private void login() {
        inputLogin = findViewById(R.id.login_input);
        inputPassword = findViewById(R.id.password_input);
        btnLogin = findViewById(R.id.login_button);
        signUp = findViewById(R.id.sign_up);
        forgotPassword = findViewById(R.id.forgot_password);

        btnLogin.setOnClickListener(view -> {
            String login = inputLogin.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (TextUtils.isEmpty(login)) {
                inputLogin.setError("Login is required");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                inputPassword.setError("Password is required");
                return;
            }

            auth.signInWithEmailAndPassword(login, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });

        });

        signUp.setOnClickListener(view -> {
            startActivity(new Intent(this, RegistrationActivity.class));
        });
    }
}

package com.susu.bankapp;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText inputLogin;
    private EditText inputPassword;
    private Button btnReg;
    private TextView signIn;

    private FirebaseAuth auth;
//    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registration();
    }

    private void registration() {
        inputLogin = findViewById(R.id.login_input);
        inputPassword = findViewById(R.id.password_input);
        btnReg = findViewById(R.id.register_button);
        signIn = findViewById(R.id.sign_in);

        auth = FirebaseAuth.getInstance();
//        progressDialog = new Dialog(this);

        btnReg.setOnClickListener(view -> {
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
            auth.createUserWithEmailAndPassword(login, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Successfully registered!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    Toast.makeText(this, "Registration is failed!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        signIn.setOnClickListener(view -> startActivity(new Intent(this, LoginActivity.class)));
    }
}

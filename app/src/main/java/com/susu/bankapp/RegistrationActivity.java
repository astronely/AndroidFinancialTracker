package com.susu.bankapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    private EditText inputLogin;
    private EditText inputPassword;
    private Button btnReg;
    private TextView signIn;


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

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
    }
}

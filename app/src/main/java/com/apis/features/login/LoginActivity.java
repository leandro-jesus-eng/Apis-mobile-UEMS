package com.apis.features.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apis.R;
import com.apis.data.repositories.AuthenticationRepository;
import com.apis.features.lote_list.MainActivity;
import com.apis.features.sign_up.SignUpActivity;

public class LoginActivity extends AppCompatActivity {

    AuthenticationRepository authenticationRepository;
    EditText emailInput;
    EditText passwordInput;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        authenticationRepository = new AuthenticationRepository();
        emailInput = findViewById(R.id.input_email_editText);
        passwordInput = findViewById(R.id.input_password_editText);
        saveButton = findViewById(R.id.save_button);
        onConfirm();
    }

    private void onConfirm() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                loginUser(email, password);
            }
        });
    }

    private void loginUser(String email, String password) {
        try {
            authenticationRepository.loginUser(email, password);
            Toast.makeText(this, "Login confirmado!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao fazer login!", Toast.LENGTH_SHORT).show();
        }
    }
}
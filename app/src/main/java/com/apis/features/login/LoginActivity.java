package com.apis.features.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.apis.R;
import com.apis.data.repositories.AuthenticationRepository;
import com.apis.features.lote_list.MainActivity;
import com.apis.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    AuthenticationRepository authenticationRepository;
    EditText emailInput;
    EditText passwordInput;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        authenticationRepository = new AuthenticationRepository();
        emailInput = findViewById(R.id.login_input_email_editText);
        passwordInput = findViewById(R.id.login_input_password_editText);
        saveButton = findViewById(R.id.login_save_button);
        onConfirm();
    }

    private void onConfirm() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                if(verifyForm(email, password)) loginUser(email, password);
            }
        });
    }

    private Boolean verifyForm(String email, String password) {
        if(email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return false;
        } else if(email.isEmpty()) {
            Toast.makeText(this, "Insira um email!", Toast.LENGTH_SHORT).show();
            return false;
        } else if(password.isEmpty()) {
            Toast.makeText(this, "Insira uma senha!", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Insira um email válido!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void loginUser(String email, String password) {
        try {
            authenticationRepository.loginUser(email, password);
            User user = new User(email);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user_from_login", user);

            Toast.makeText(getApplicationContext(), "Login confirmado!", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao fazer login!", Toast.LENGTH_SHORT).show();
        }
    }
}
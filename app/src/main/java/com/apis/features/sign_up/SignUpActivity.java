package com.apis.features.sign_up;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.apis.R;
import com.apis.data.repositories.AuthenticationRepository;
import com.apis.features.animal_list.ListaAnimais;
import com.apis.features.login.LoginActivity;
import com.apis.features.lote_list.MainActivity;
import com.apis.model.User;

public class SignUpActivity extends AppCompatActivity {

    AuthenticationRepository authenticationRepository;
    EditText emailInput;
    EditText passwordInput;
    Button saveButton;
    TextView goToLoginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        authenticationRepository = new AuthenticationRepository();
        emailInput = findViewById(R.id.sign_up_input_email_editText);
        passwordInput = findViewById(R.id.sign_up_input_password_editText);
        saveButton = findViewById(R.id.sign_up_save_button);
        goToLoginTextView = findViewById(R.id.go_to_login_textView);
        setupUi();
    }

    private void setupUi() {
        onConfirm();
        onGoToLogin();
    }

    private void onConfirm() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                if(verifyForm(email, password)) signUpUser(email, password);

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

    private void onGoToLogin() {
        goToLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signUpUser(String email, String password) {
        try {
            authenticationRepository.signUpUser(email, password);
            User user = new User(email);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user_from_sign_up", user);

            Toast.makeText(this, "Cadastro confirmado!", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();

        } catch (Exception e) {
            Toast.makeText(this, "Erro ao fazer cadastro!", Toast.LENGTH_SHORT).show();
        }
    }
}
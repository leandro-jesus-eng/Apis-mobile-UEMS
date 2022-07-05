package com.apis.features.login;

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
import com.apis.data.repositories.DbRepository;
import com.apis.data.repositories.EntitiesHandlerRepository;
import com.apis.features.lote_list.MainActivity;
import com.apis.model.User;

public class LoginActivity extends AppCompatActivity {

    AuthenticationRepository authenticationRepository;
    EntitiesHandlerRepository entitiesHandlerRepository;
    DbRepository dbRepository;
    EditText emailInput;
    EditText passwordInput;
    Button saveButton;
    TextView erroMsgTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        authenticationRepository = new AuthenticationRepository();
        entitiesHandlerRepository = new EntitiesHandlerRepository(this);
        dbRepository = new DbRepository(this);
        emailInput = findViewById(R.id.login_input_email_editText);
        passwordInput = findViewById(R.id.login_input_password_editText);
        saveButton = findViewById(R.id.login_save_button);
        erroMsgTextView = findViewById(R.id.error_msg_textView);
        onConfirm();
    }

    private void onConfirm() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                if(verifyForm(email, password)) loginUser(email, password.trim());
            }
        });
    }

    private Boolean verifyForm(String email, String password) {
        if(email.isEmpty() && password.isEmpty()) {
            errorMsgVisible(true, "Preencha todos os campos!");
            return false;
        } else if(email.isEmpty()) {
            errorMsgVisible(true, "Insira um email!");
            return false;
        } else if(password.isEmpty()) {
            errorMsgVisible(true, "Insira uma senha!");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMsgVisible(true, "Insira um email válido!");
            return false;
        } else if(!entitiesHandlerRepository.userExists(email)) {
            errorMsgVisible(true, "Não há usuários com esse email!");
            return false;
        }
        errorMsgVisible(false, "");
        return true;
    }

    private void loginUser(String email, String password) {
        try {
            User user;
            Intent intent = new Intent(this, MainActivity.class);
            authenticationRepository.loginUser(email, password);
            if (!entitiesHandlerRepository.userExists(email)) {
                dbRepository.insertUser(new User(email));
            }
            user = dbRepository.getUser(email);
            intent.putExtra("user_from_login", user);
            Toast.makeText(getApplicationContext(), "Login confirmado!", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao fazer login!", Toast.LENGTH_SHORT).show();
        }
    }

    private void errorMsgVisible(boolean show, String message) {
        if(show) {
            erroMsgTextView.setVisibility(View.VISIBLE);
            erroMsgTextView.setText(message);
        }
        else erroMsgTextView.setVisibility(View.INVISIBLE);
    }
}
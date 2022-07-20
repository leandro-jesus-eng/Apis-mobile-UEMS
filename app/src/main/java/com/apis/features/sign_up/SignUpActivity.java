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
import com.apis.data.repositories.DbRepository;
import com.apis.data.repositories.EntitiesHandlerRepository;
import com.apis.data.repositories.FirestoreRepository;
import com.apis.features.login.LoginActivity;
import com.apis.features.lote_list.MainActivity;
import com.apis.model.User;

public class SignUpActivity extends AppCompatActivity {

    AuthenticationRepository authenticationRepository;
    DbRepository dbRepository;
    FirestoreRepository firestoreRepository;
    EntitiesHandlerRepository entitiesHandlerRepository;
    EditText emailInput;
    EditText passwordInput;
    Button saveButton;
    TextView goToLoginTextView;
    TextView erroMsgTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firestoreRepository = new FirestoreRepository(this);
        authenticationRepository = new AuthenticationRepository();
        entitiesHandlerRepository = new EntitiesHandlerRepository(this);
        dbRepository = new DbRepository(this);
        emailInput = findViewById(R.id.sign_up_input_email_editText);
        passwordInput = findViewById(R.id.sign_up_input_password_editText);
        saveButton = findViewById(R.id.sign_up_save_button);
        goToLoginTextView = findViewById(R.id.go_to_login_textView);
        erroMsgTextView = findViewById(R.id.error_msg_textView);
        firestoreRepository.setupRemoteChangeListenerUserOnly();
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
                if(verifyForm(email, password)) signUpUser(email, password.trim());

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
        } else if(entitiesHandlerRepository.userExists(email)) {
            errorMsgVisible(true, "Esse email já está sendo usado!");
            return false;
        }
        errorMsgVisible(false, "");
        return true;
    }

    private void onGoToLogin() {
        goToLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    private void signUpUser(String email, String password) {
        try {
            User user = new User(email);
            authenticationRepository.signUpUser(email, password);
            dbRepository.insertUser(user);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user_from_sign_up", dbRepository.getUser(user.getEmail()));

            Toast.makeText(this, "Cadastro confirmado!", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao fazer cadastro!", Toast.LENGTH_SHORT).show();
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
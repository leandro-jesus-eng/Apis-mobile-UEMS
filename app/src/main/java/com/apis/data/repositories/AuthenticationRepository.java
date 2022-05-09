package com.apis.data.repositories;

import android.util.Log;
import androidx.annotation.NonNull;

import com.apis.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationRepository {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void signUpUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
            }
        });
    }

    public void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
            }
        });
    }

    public void logoutUser() {
        try {
            firebaseAuth.signOut();
        } catch (Exception e) {
            Log.i("USERZAO", "erro ao fazer logout");
        }
    }


    public User getCurrentUser() {
        try {
            return new User(firebaseAuth.getCurrentUser().getEmail());
        } catch (Exception e) {
            Log.i("USERZAO", "erro ao pegar user");
            return null;
        }
    }
}

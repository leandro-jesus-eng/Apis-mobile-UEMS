package com.apis.data.repositories;

import android.util.Log;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationRepository {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void signUpUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) { }
        });
    }

    public void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) { }
        });
    }

    public void logoutUser() {
        try {
            firebaseAuth.signOut();
        } catch (Exception e) {
            Log.i("Error", "Erro ao fazer logout");
        }
    }
}
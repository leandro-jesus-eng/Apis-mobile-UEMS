package com.apis.data.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class AuthenticationRepository {

    final private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;

    public void signUpUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = firebaseAuth.getCurrentUser();
                        }
                    }
                });
    }

    public void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = firebaseAuth.getCurrentUser();
                        }
                    }
                });
    }

    public void logoutUser() {
        try {
            firebaseAuth.signOut();
            user = null;
        } catch (Exception e) {
            Log.i("Error", "erro ao fazer logout");
        }
    }

    public FirebaseUser getCurrentUser() {
        try {
            if(user == null) {
                user = firebaseAuth.getCurrentUser();
            }
            return user;
        } catch (Exception e) {
            return null;
        }
    }
}

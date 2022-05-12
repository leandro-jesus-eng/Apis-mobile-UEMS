package com.apis.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

@Entity(tableName = "tb_user")
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int userId;

    @ColumnInfo(name = "email")
    private String email;

    public User(String email){
        this.email = email;
    }

    public User() {}

    public int getUserId() { return userId; }
    public void setUserId(int id) { this.userId = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
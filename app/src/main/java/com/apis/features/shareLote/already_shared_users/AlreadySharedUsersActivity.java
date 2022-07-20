package com.apis.features.shareLote.already_shared_users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.apis.R;
import com.apis.features.shareLote.ShareLoteAdapter;
import com.apis.features.usecases.ManageUser;
import com.apis.model.ItemUser;
import com.apis.model.User;

import java.util.ArrayList;
import java.util.List;

public class AlreadySharedUsersActivity extends AppCompatActivity {

    List<User> users = new ArrayList<>();
    LinearLayout noUsersAlert;
    static private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_shared_users);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Compartilhados com");
        noUsersAlert = findViewById(R.id.alert_no_shared_users_linearLayout);
        currentUser = new ManageUser().getUser();
        getDataFromMain();
        showNoUsersAlert();
    }

    private void showNoUsersAlert() {
        if(users.size() == 0) {
            noUsersAlert.setVisibility(View.VISIBLE);
        } else {
            noUsersAlert.setVisibility(View.GONE);
        }
    }

    private void getDataFromMain() {
        if (getIntent().hasExtra("users")) {
            users = (List<User>) getIntent().getSerializableExtra("users");

            removeCurrentUser();
            setupList();
        }
    }

    private void removeCurrentUser() {
        for(User user: users) {
            if(user.getEmail().equals(currentUser.getEmail())) {
                users.remove(user);
                break;
            }
        }
    }

    private void setupList() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.shared_users_recycleView);
        AlreadySharedUsersAdapter adapter = new AlreadySharedUsersAdapter(users, this);
        recyclerView.setAdapter(adapter);
    }
}
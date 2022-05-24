package com.apis.features.shareLote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.apis.R;
import com.apis.data.database.relations.UserLoteCrossRef;
import com.apis.data.repositories.DbRepository;
import com.apis.features.lote_list.LoteAdapter;
import com.apis.model.ItemUser;
import com.apis.model.Lote;
import com.apis.model.User;

import java.util.ArrayList;
import java.util.List;

public class ShareLoteActivity extends AppCompatActivity {

    EditText searchEditText;
    ImageButton searchButton;
    Button saveButton;
    DbRepository dbRepository;
    String loteName;
    Integer loteId;
    ArrayList<User> users = new ArrayList<>();
    User currentUser;
    ShareLoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_lote);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        searchEditText = findViewById(R.id.share_lote_search_editText);
        searchButton = findViewById(R.id.share_lote_search_button);
        saveButton = findViewById(R.id.share_lote_save_button);
        dbRepository = new DbRepository(this);
        users.addAll(dbRepository.getAllUsers());

        getDataFromMain();
        removeCurrentUser();
        setListeners();
        setupList(users);
        getSupportActionBar().setTitle("Compartilhar "+loteName+" com outros usuários");
    }

    private void removeCurrentUser() {
        for(User user : users) {
            if (user.getEmail().equals(currentUser.getEmail())) {
                users.remove(user);
                break;
            }
        }
    }

    private void setListeners() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearch();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSelectedUsers();
            }
        });
    }

    private void onSearch() {
        String searchedText = searchEditText.getText().toString();
        List<User> visibleUsers = new ArrayList<>();

        for(User user : users) {
            if(user.getEmail().toLowerCase().contains(searchedText)) {
                visibleUsers.add(user);
            }
        }
        setupList(visibleUsers);
    }

    private void getDataFromMain() {
        if (getIntent().hasExtra("lote_nome") && getIntent().hasExtra("lote_id") && getIntent().hasExtra("user")) {
            loteName = getIntent().getStringExtra("lote_nome");
            loteId = getIntent().getIntExtra("lote_id", 9999);
            currentUser = (User) getIntent().getSerializableExtra("user");
        }
    }

    private void setupList(List<User> users) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.share_lote_user_recycleView);
        List<ItemUser> itemUsers = new ArrayList<>();
        for (User user : users) {
            Log.i("UZER", user.getEmail());
            itemUsers.add(new ItemUser(false, user));
        }

        adapter = new ShareLoteAdapter(itemUsers, this);
        recyclerView.setAdapter(adapter);
    }

    private void getSelectedUsers() {
        List<Integer> selectedUsers = adapter.getSelectedUserIds();
        /*
        for(Integer userId : selectedUsers) {
            dbRepository.insertUserLoteCrossRef(new UserLoteCrossRef(userId, loteId));
        }
         */
    }
}
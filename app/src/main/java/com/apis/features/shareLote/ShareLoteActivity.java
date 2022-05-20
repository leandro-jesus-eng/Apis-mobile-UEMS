package com.apis.features.shareLote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.apis.R;
import com.apis.data.repositories.DbRepository;
import com.apis.features.lote_list.LoteAdapter;
import com.apis.model.Lote;
import com.apis.model.User;

import java.util.ArrayList;
import java.util.List;

public class ShareLoteActivity extends AppCompatActivity {

    EditText searchEditText;
    ImageButton searchButton;
    DbRepository dbRepository;
    String loteName;
    Integer loteId;
    List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_lote);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        searchEditText = findViewById(R.id.share_lote_search_editText);
        searchButton = findViewById(R.id.share_lote_search_button);
        dbRepository = new DbRepository(this);
        users = dbRepository.getAllUsers();

        getDataFromMain();
        setListeners();
        setupList(users);
        getSupportActionBar().setTitle("Compartilhar "+loteName+" com outros usuários");
    }

    private void setListeners() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearch();
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
        if (getIntent().hasExtra("lote_nome") && getIntent().hasExtra("lote_id")){
            loteName = getIntent().getStringExtra("lote_nome");
            loteId = getIntent().getIntExtra("lote_id", 9999);
        }
    }

    private void setupList(List<User> users) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.share_lote_user_recycleView);
        recyclerView.setAdapter(new ShareLoteAdapter(users, this));
    }
}
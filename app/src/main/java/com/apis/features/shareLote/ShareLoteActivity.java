package com.apis.features.shareLote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apis.R;
import com.apis.data.database.relations.UserLoteCrossRef;
import com.apis.data.repositories.DbRepository;
import com.apis.data.repositories.EntitiesHandlerRepository;
import com.apis.features.comportamentos_list.AdicionarComportamento;
import com.apis.features.lote_list.LoteAdapter;
import com.apis.features.lote_list.MainActivity;
import com.apis.model.ItemUser;
import com.apis.model.Lote;
import com.apis.model.User;

import java.util.ArrayList;
import java.util.List;

public class ShareLoteActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private Button saveButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DbRepository dbRepository;
    private EntitiesHandlerRepository entitiesHandlerRepository;
    private String loteName;
    private Integer loteId;
    private ArrayList<User> users = new ArrayList<>();
    private User currentUser;
    private ShareLoteAdapter adapter;
    private LinearLayout alertUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_lote);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        swipeRefreshLayout = findViewById(R.id.share_lote_swipeRefresh);
        searchEditText = findViewById(R.id.share_lote_search_editText);
        searchButton = findViewById(R.id.share_lote_search_button);
        saveButton = findViewById(R.id.share_lote_save_button);
        alertUser = findViewById(R.id.alert_no_users_linearLayout);
        dbRepository = new DbRepository(this);
        entitiesHandlerRepository = new EntitiesHandlerRepository(this);
        users.addAll(dbRepository.getAllUsers());

        getDataFromMain();
        removeUnnecessaryUsers();
        setupSwipeRefresh();
        setListeners();
        setupList(users);
        getSupportActionBar().setTitle("Compartilhar "+loteName+" com outros usuários");
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setupList(users);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getDataFromMain() {
        if (getIntent().hasExtra("lote_nome") && getIntent().hasExtra("lote_id") && getIntent().hasExtra("user")) {
            loteName = getIntent().getStringExtra("lote_nome");
            loteId = getIntent().getIntExtra("lote_id", 9999);
            currentUser = (User) getIntent().getSerializableExtra("user");
        }
    }

    private void removeUnnecessaryUsers() {
        List<User> nonNecessaryUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getEmail().equals(currentUser.getEmail()) || entitiesHandlerRepository
                    .userLoteCrosRefExists(user.getUserId(), loteId)
            ) {
                nonNecessaryUsers.add(user);
            }
        }

        for (User nonNecessaryUser : nonNecessaryUsers) {
            users.contains(nonNecessaryUser);
            users.remove(nonNecessaryUser);
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
                onConfirm();
            }
        });
    }

    private void setupList(List<User> users) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.share_lote_user_recycleView);
        List<ItemUser> itemUsers = new ArrayList<>();
        for (User user : users) { itemUsers.add(new ItemUser(false, user)); }
        showNoUsersAlert(itemUsers);
        adapter = new ShareLoteAdapter(itemUsers, this);
        recyclerView.setAdapter(adapter);
    }

    private void showNoUsersAlert(List<ItemUser> users) {
        if(users.size() > 0)  {
            alertUser.setVisibility(View.INVISIBLE);
            saveButton.setVisibility(View.VISIBLE);
        } else {
            alertUser.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.INVISIBLE);
        }
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

    private void onConfirm() {
        List<User> selectedUsers = adapter.getSelectedUsers();
        List<Integer> selectedUsersId = new ArrayList<>();
        String title = "Deseja compartilhar o lote "+loteName+" com o(s) seguinte(s) usuário(s)?";
        StringBuilder message = new StringBuilder();

        for(User user : selectedUsers) {
            selectedUsersId.add(user.getUserId());
            message.append(" ").append(user.getEmail()).append(";");
        }

        if(selectedUsersId.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ShareLoteActivity.this);
            builder.setTitle(title)
                    .setMessage(message.toString())
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    for (Integer userId : selectedUsersId) {
                                        Log.i("UZER", userId.toString());
                                        dbRepository.insertUserLoteCrossRef(
                                                new UserLoteCrossRef(userId, loteId)
                                        );
                                    }
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Salvo!",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    finish();
                                }
                            }
                    ).setNegativeButton("Cancelar", null)
                    .create()
                    .show();
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Você não selecionou nenhum usuário para compartilhar",
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}
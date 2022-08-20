package com.apis.features.shareLote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apis.R;
import com.apis.data.database.relations.UserLoteCrossRef;
import com.apis.data.repositories.DbRepository;
import com.apis.data.repositories.EntitiesHandlerRepository;
import com.apis.features.shareLote.already_shared_users.AlreadySharedUsersActivity;
import com.apis.model.ItemUser;
import com.apis.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShareLoteActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private Button saveButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DbRepository dbRepository;
    private EntitiesHandlerRepository entitiesHandlerRepository;
    static private String loteName;
    static private Integer loteId;
    private ArrayList<User> users = new ArrayList<>();
    static private User currentUser;
    private ShareLoteAdapter adapter;
    private LinearLayout alertUser;
    List<User> nonNecessaryUsers = new ArrayList<>();

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
        getSupportActionBar().setTitle("Compartilhar "+loteName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share_lote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.already_shared_users) {
            Intent intent = new Intent(this, AlreadySharedUsersActivity.class);
            intent.putExtra("users", (Serializable) nonNecessaryUsers);
            this.startActivity(intent);
            return true;
        }
        finish();
        return true;
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
            alertUser.setVisibility(View.GONE);
            saveButton.setVisibility(View.VISIBLE);
        } else {
            alertUser.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.GONE);
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
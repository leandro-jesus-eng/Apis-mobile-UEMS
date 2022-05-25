package com.apis.features.shareLote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;
import android.widget.Toast;

import com.apis.R;
import com.apis.data.database.relations.UserLoteCrossRef;
import com.apis.data.repositories.DbRepository;
import com.apis.features.comportamentos_list.AdicionarComportamento;
import com.apis.features.lote_list.LoteAdapter;
import com.apis.features.lote_list.MainActivity;
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

    private void getDataFromMain() {
        if (getIntent().hasExtra("lote_nome") && getIntent().hasExtra("lote_id") && getIntent().hasExtra("user")) {
            loteName = getIntent().getStringExtra("lote_nome");
            loteId = getIntent().getIntExtra("lote_id", 9999);
            currentUser = (User) getIntent().getSerializableExtra("user");
        }
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
                onConfirm();
            }
        });
    }

    private void setupList(List<User> users) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.share_lote_user_recycleView);
        List<ItemUser> itemUsers = new ArrayList<>();
        for (User user : users) { itemUsers.add(new ItemUser(false, user)); }

        adapter = new ShareLoteAdapter(itemUsers, this);
        recyclerView.setAdapter(adapter);
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
                                    for(Integer userId : selectedUsersId) {
                                        Log.i("UZER", userId.toString());
                                    /*
                                    dbRepository.insertUserLoteCrossRef(
                                            new UserLoteCrossRef(userId, loteId)
                                    );

                                     */
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
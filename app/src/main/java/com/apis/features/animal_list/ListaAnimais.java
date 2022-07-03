package com.apis.features.animal_list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.apis.R;
import com.apis.data.repositories.DbRepository;
import com.apis.data.repositories.EntitiesHandlerRepository;
import com.apis.features.edicaoComportamento.EditComportamento;
import com.apis.model.Animal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaAnimais extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String nomeLote;
    private String nomeAnimal;
    private int idLote;

    static boolean idChecked= false;
    static boolean ordAscChecked= false;
    static boolean ordDscChecked= false;

    List<Animal> animais= new ArrayList<>();

    DrawerLayout drawer;
    DbRepository dbRepository;
    EntitiesHandlerRepository entitiesHandlerRepository;
    private SwipeRefreshLayout swipeRefreshLayout;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_animais);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Botão flutuante
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarAnimal();
            }
        });

        swipeRefreshLayout = findViewById(R.id.animal_list_swipe_refresh);
        dbRepository = new DbRepository(this);
        entitiesHandlerRepository = new EntitiesHandlerRepository(this);
        drawer = findViewById(R.id.drawerListaAnimais);
        NavigationView navigationView = findViewById(R.id.navViewListaAnimais);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        pegarDadosActivityPassada();
        setupSwipeRefresh();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        configurarLista();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                configurarLista();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void pegarDadosActivityPassada(){

        if (getIntent().hasExtra("lote_nome") && getIntent().hasExtra("lote_id")){
            nomeLote = getIntent().getStringExtra("lote_nome");
            idLote = getIntent().getIntExtra("lote_id", 9999);

            TextView txtNomeLote = (TextView)findViewById(R.id.txtNomeLote);
            txtNomeLote.setText(nomeLote);

            TextView txtIdLote = (TextView)findViewById(R.id.txtIdLote);
            txtIdLote.setText("ID: "+idLote);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void configurarLista(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerAnimais);

        if(ordAscChecked){
            animais = dbRepository.getAnimais(idLote);
            animais = dbRepository.getAnimaisPorOrdemDeAnotacao(animais);
        }else if(idChecked){
            animais = dbRepository.getAnimaisPorId(idLote);
        }else {
            //Ordenação por anotação mais antiga padrão
            animais = dbRepository.getAnimais(idLote);
            animais = dbRepository.getAnimaisPorOrdemDeAnotacao(animais);
            Collections.reverse(animais);
        }

        TextView nenhumAnimal = (TextView) findViewById(R.id.textNenhumAnimal);
        ImageView alertImg = (ImageView) findViewById(R.id.alertImgAnimais);

        if(animais.size() > 0){
            nenhumAnimal.setVisibility(View.INVISIBLE);
            alertImg.setVisibility(View.INVISIBLE);
        }else {
            nenhumAnimal.setVisibility(View.VISIBLE);
            alertImg.setVisibility(View.VISIBLE);
        }

        recyclerView.setAdapter(new AnimalAdapter(animais, this));
        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
    }

    public void salvarAnimal(){
        LayoutInflater layoutInflater = LayoutInflater.from(ListaAnimais.this);
        View promptView = layoutInflater.inflate(R.layout.prompt_animal, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListaAnimais.this);
        alertDialogBuilder.setView(promptView);

        final EditText txtNomeAnimal = (EditText) promptView.findViewById(R.id.lote_name_editText);

        txtNomeAnimal.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        nomeAnimal = txtNomeAnimal.getText().toString();

                        if(!nomeAnimal.equals("")) {

                            if(entitiesHandlerRepository.animalExiste(nomeAnimal)) {
                                Toast.makeText(getApplicationContext(), "O animal com esse nome já existe!", Toast.LENGTH_LONG).show();
                            } else {
                                ////Salva no BD
                                Animal animal = new Animal(0, nomeAnimal, idLote, "Sem anotação!");
                                try {
                                    dbRepository.insertAnimal(animal);
                                    finish();
                                    startActivity(getIntent());
                                }catch (Throwable throwable){
                                    Toast.makeText(getApplicationContext(), "Erro ao salvar!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "O animal não pode ter um nome em branco!", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerAnimais);
        int id = menuItem.getItemId();
        if(id == R.id.ordenacaoAnotacaoASC){
            ordAscChecked = true;
            ordDscChecked = false;
            idChecked = false;

            animais = dbRepository.getAnimais(idLote);
            animais = dbRepository.getAnimaisPorOrdemDeAnotacao(animais);

            drawer.closeDrawer(GravityCompat.START);
            Toast.makeText(getApplicationContext(), "Lista ordenada por anotação mais recente!", Toast.LENGTH_SHORT).show();

        }else if(id == R.id.ordenacaoAnotacaoDSC){
            ordAscChecked = false;
            ordDscChecked = true;
            idChecked = false;

            animais = dbRepository.getAnimais(idLote);
            animais = dbRepository.getAnimaisPorOrdemDeAnotacao(animais);
            Collections.reverse(animais);

            drawer.closeDrawer(GravityCompat.START);
            Toast.makeText(getApplicationContext(), "Lista ordenada por anotação mais antiga!", Toast.LENGTH_SHORT).show();

        }else if(id == R.id.ordenacaoID){
            ordAscChecked = false;
            ordDscChecked = false;
            idChecked = true;

            animais = dbRepository.getAnimaisPorId(idLote);

            drawer.closeDrawer(GravityCompat.START);
            Toast.makeText(getApplicationContext(), "Lista ordenada por identificação!", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.voltar){
            finish();
        }else{
            Intent intent = new Intent(getApplicationContext(), EditComportamento.class);
            intent.putExtra("edit_comp_lote", true);
            intent.putExtra("lote_id", idLote);
            intent.putExtra("lote_nome", nomeLote);
            startActivity(intent);
            finish();
        }
        recyclerView.setAdapter(new AnimalAdapter(animais, this));
        return true;
    }

}

package com.apis.features.comportamento_reprodutivo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apis.R;
import com.apis.database.DbRepository;
import com.apis.features.comportamentos_list.AdicionarComportamento;
import com.apis.features.edicaoComportamento.EditComportamentoAdapter;
import com.apis.models.Animal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AdicionarCompReprodutivo extends AppCompatActivity {

    private FloatingActionButton topButton;
    private String nomeAnimal;
    private String nomeLote;
    private int idAnimal;
    private int idLote;
    private RecyclerView recyclerView;
    private AdicionarCompReprodutivoAdapter adapter;
    private DbRepository database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_comp_reprodutivo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        topButton = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycler_view_rep);
        database = new DbRepository(this);

        setRecycler();
        pegarDadosActivityPassada();

        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdicionarComportamento.class);
                intent.putExtra("animal_nome", nomeAnimal);
                intent.putExtra("animal_id", idAnimal);
                intent.putExtra("lote_id", idLote);
                startActivity(intent);
                finish();
            }
        });

        List<Animal> animais = database.getAnimais(idLote);
        adapter.submitList(animais);
    }

    private void setRecycler(){
        adapter = new AdicionarCompReprodutivoAdapter(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
    }

    private void pegarDadosActivityPassada(){
        if (getIntent().hasExtra("animal_nome") && getIntent().hasExtra("animal_id") && getIntent().hasExtra("lote_id")){
            nomeAnimal = getIntent().getStringExtra("animal_nome");
            idAnimal = getIntent().getIntExtra("animal_id", 9999);
            idLote = getIntent().getIntExtra("lote_id", 9999);
        }
    }

}


package com.apis.features.comportamento_reprodutivo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apis.R;
import com.apis.database.DbRepository;
import com.apis.features.animal_list.AnimalAdapter;
import com.apis.features.comportamentos_list.AdicionarComportamento;
import com.apis.model.Animal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AdicionarCompReprodutivo extends AppCompatActivity {

    private String nomeAnimal;
    private String nomeLote;
    private int idAnimal;
    private int idLote;
    private List<Animal> animalList = new ArrayList<>();

    private RecyclerView recyclerView;
    private AdicionarCompReprodutivoAdapter adapter;
    private Button montandoButton;
    private Button aceitandoMontaButton;

    private DbRepository database;
    static private boolean isMontando = true;

    private TextView textMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_comp_reprodutivo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView = findViewById(R.id.recycler_view_rep);
        montandoButton = findViewById(R.id.estaMontando_btn);
        aceitandoMontaButton = findViewById(R.id.aceitandoMonta_btn);
        textMsg = findViewById(R.id.textMsg);

        database = new DbRepository(this);
        isMontando = true;

        pegarDadosActivityPassada();
        setRecycler();
        setNoAnimaisMsg();

        getSupportActionBar().setTitle(nomeAnimal);

        montandoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                montandoButton.setBackgroundResource(R.drawable.round_button_on);
                aceitandoMontaButton.setBackgroundResource(R.drawable.round_button_off);
                Toast.makeText(getApplicationContext(), "Montando", Toast.LENGTH_SHORT).show();
                isMontando = true;
                adapter.isMontando(true);
            }
        });

        aceitandoMontaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aceitandoMontaButton.setBackgroundResource(R.drawable.round_button_on);
                montandoButton.setBackgroundResource(R.drawable.round_button_off);
                Toast.makeText(getApplicationContext(), "Aceitando monta", Toast.LENGTH_SHORT).show();
                isMontando = false;
                adapter.isMontando(false);
            }
        });
        List<Animal> animalList = database.getAnimais(idLote);
        animalList.remove(findVacaEmAnotacao());
        adapter.submitList(animalList);
    }

    private void setNoAnimaisMsg(){
        List<Animal> animalList = database.getAnimais(idLote);

        if(animalList.size() == 1){
            textMsg.setVisibility(View.VISIBLE);
        }else{
           textMsg.setVisibility(View.INVISIBLE);
        }
    }

    private void setRecycler(){
        adapter = new AdicionarCompReprodutivoAdapter(this, isMontando, findVacaEmAnotacao());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
    }

    private Animal findVacaEmAnotacao(){
        for(Animal animal : database.getAnimais(idLote)){
            if(animal.getNome().equals(nomeAnimal)){
                return animal;
            }
        }
        return null;
    }

    private void pegarDadosActivityPassada(){
        if (getIntent().hasExtra("animal_nome") && getIntent().hasExtra("animal_id") && getIntent().hasExtra("lote_id")){
            nomeAnimal = getIntent().getStringExtra("animal_nome");
            idAnimal = getIntent().getIntExtra("animal_id", 9999);
            idLote = getIntent().getIntExtra("lote_id", 9999);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), AdicionarComportamento.class);
                intent.putExtra("animal_nome", nomeAnimal);
                intent.putExtra("animal_id", idAnimal);
                intent.putExtra("lote_id", idLote);
                startActivity(intent);
                finish();
                break;
            default:break;
        }
        return true;
    }

}


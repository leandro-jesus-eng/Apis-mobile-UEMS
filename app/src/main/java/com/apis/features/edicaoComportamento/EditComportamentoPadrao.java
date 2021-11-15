package com.apis.features.edicaoComportamento;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apis.R;
import com.apis.database.DbRepository;
import com.apis.features.animal_list.AnimalAdapter;
import com.apis.models.Comportamento;
import com.apis.models.DateTime;
import com.apis.models.FormularioComportamento;
import com.apis.models.TipoComportamento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditComportamentoPadrao extends AppCompatActivity {

    EditComportamentoAdapter _adapter;
    EditComportamentoAdapter adapter;
    DbRepository database;

    List<TipoComportamento> listaTipos = new ArrayList<>();
    List<Comportamento> listaComportamentos = new ArrayList<>();
    FormularioComportamento formularioComportamento;

    TextView textAdicionarTipo;
    ImageButton imgAdicionarTipo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicao_comportamento_padrao);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        database = new DbRepository(getApplicationContext());

        if(database.getFormularioPadrao(true) == null){
            DateTime dateTime = new DateTime();
            String dataCriacao = dateTime.pegarData() + " " + dateTime.pegarHora();

            database.insertFormularioComportamento(new FormularioComportamento(0, dataCriacao, true, -1));
            createPatternData();
        }

        formularioComportamento = database.getFormularioPadrao(true);

        textAdicionarTipo = findViewById(R.id.lbl_tipo_adicionar_comportamento);
        imgAdicionarTipo = findViewById(R.id.imgAddTipo);

        setRecycler();
        setAddTipoClickListener();

        setData();
    }
    private void setRecycler(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_edit_comportamento);
        _adapter = new EditComportamentoAdapter( this);
        adapter = _adapter;

        recyclerView.setAdapter(adapter);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
    }

    private void setAddTipoClickListener(){
        textAdicionarTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.insertTipoComportamento(new TipoComportamento(0, "", formularioComportamento.getId()));

                adapter.submitItem(database.getAllTipos().get(database.getAllTipos().size() - 1));
            }
        });

        imgAdicionarTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.insertTipoComportamento(new TipoComportamento(0, "", formularioComportamento.getId()));

                adapter.submitItem(database.getAllTipos().get(database.getAllTipos().size() - 1));
            }
        });

    }

    private void createPatternData(){
        formularioComportamento = database.getFormularioPadrao(true);
        insertNewType("Fisiológico");
        insertNewType("Reprodutivo");
        insertNewType("Uso da Sombra");
        insertNewType("Leitura");
    }

    private void insertNewType(String descricao){
        for(TipoComportamento tipos :  database.getAllTipos()){
            if(descricao.equals(tipos.getDescricao())){
                break;
            }
        }
        database.insertTipoComportamento(new TipoComportamento(0, descricao, formularioComportamento.getId()));
    }

    private void setData(){
        listaTipos = database.getFormularioWithTipoComportamento(formularioComportamento.getId()).get(0).tiposComportamento;

        adapter.submitList(listaTipos);
    }

     public void saveData(View view){
         listaTipos = adapter.getTipos();
         listaComportamentos = adapter.getComportamentos();

         for (TipoComportamento tipo : listaTipos) {
             insertNewType(tipo.getDescricao());
         }

         for (Comportamento comportamento : listaComportamentos) {
             database.insertComportamento(comportamento);
         }

         finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:break;
        }
        return true;
    }
}

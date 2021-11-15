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
    List<TipoComportamento> listaTiposBackup = new ArrayList<>();
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
        insertNewType("Fisiológico", 0);
        insertNewType("Reprodutivo", 0);
        insertNewType("Uso da Sombra", 0);

        for(TipoComportamento tipoComportamento : database.getAllTipos()){
            switch (tipoComportamento.getDescricao()){
                case "Fisiológico":
                    insertNewComportamento("Pastejando", 0, tipoComportamento.getId());
                    insertNewComportamento("Ociosa em pé", 0, tipoComportamento.getId());
                    insertNewComportamento("Ociosa deitada", 0, tipoComportamento.getId());
                    insertNewComportamento("Ruminando em pé", 0, tipoComportamento.getId());
                    insertNewComportamento("Ruminando em deitada", 0, tipoComportamento.getId());
                    break;
                case "Reprodutivo":
                    insertNewComportamento("Aceita de monta", 0, tipoComportamento.getId());
                    insertNewComportamento("Monta outra", 0, tipoComportamento.getId());
                    insertNewComportamento("Inquieta", 0, tipoComportamento.getId());
                    break;
                case "Uso da Sombra":
                    insertNewComportamento("Sol", 0, tipoComportamento.getId());
                    insertNewComportamento("Sombra", 0, tipoComportamento.getId());

                    break;
            }
        }
    }

    private void insertNewType(String descricao, int id){
        boolean exist = false;

        for(TipoComportamento tipos :  database.getAllTipos()){
            if(descricao.equals(tipos.getDescricao()) || tipos.getId() == id){
                exist = true;
            }
        }

        if(exist){
            for(TipoComportamento tipoComportamento : database.getAllTipos()){
                if(tipoComportamento.getId() == id && tipoComportamento.getDescricao() != descricao){
                    tipoComportamento.setDescricao(descricao);
                    database.updateTipo(tipoComportamento);
                }
            }

        }else{
            database.insertTipoComportamento(new TipoComportamento(0, descricao, formularioComportamento.getId()));
        }

    }

    private void insertNewComportamento(String nome, int id, int tipoId){
        boolean exist = false;

        for(Comportamento comportamento :  database.getAllComportamentos()){
            if(nome.equals(comportamento.getNome()) || comportamento.getId() == id){
                exist = true;
            }
        }

        if(exist){
            for(Comportamento comportamento : database.getAllComportamentos()){
                if(comportamento.getId() == id && comportamento.getNome() != nome){
                    comportamento.setNome(nome);
                    database.updateComportamento(comportamento);
                }
            }
        }else{
            database.insertComportamento(new Comportamento(id, nome, tipoId));
        }

    }

    private void setData(){
        listaTipos = database.getFormularioWithTipoComportamento(formularioComportamento.getId()).get(0).tiposComportamento;
        listaComportamentos = database.getAllComportamentos();

        adapter.submitTipoList(listaTipos);
        adapter.submitComportamentoList(listaComportamentos);
    }

     public void saveData(View view){
         listaTipos = adapter.getTipos();
         listaComportamentos = adapter.getComportamentos();
         List<Comportamento> listaComportamentosExcluidos = adapter.getComportamentosExcluidos();
         List<TipoComportamento> listaTiposComportamentoExcluidos = adapter.getTiposExcluidos();

         for(Comportamento comportamento : listaComportamentosExcluidos){
             database.excluirComportamento(comportamento);
         }

         for(TipoComportamento tipoComportamento : listaTiposComportamentoExcluidos){
             database.excluirTipoComportamento(tipoComportamento);
         }

         for (TipoComportamento tipo : listaTipos) {
             insertNewType(tipo.getDescricao(), tipo.getId());
         }

         for (Comportamento comportamento : listaComportamentos) {
             database.insertComportamento(comportamento);
         }
         Toast.makeText(getApplicationContext(), "Formulário salvo!", Toast.LENGTH_SHORT).show();
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

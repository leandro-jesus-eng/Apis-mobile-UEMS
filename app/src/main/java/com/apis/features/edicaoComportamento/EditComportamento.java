package com.apis.features.edicaoComportamento;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apis.R;
import com.apis.database.DbRepository;
import com.apis.features.animal_list.ListaAnimais;
import com.apis.models.Comportamento;
import com.apis.models.DateTime;
import com.apis.models.FormularioComportamento;
import com.apis.models.TipoComportamento;

import java.util.ArrayList;
import java.util.List;

public class EditComportamento extends AppCompatActivity {

    EditComportamentoAdapter _adapter;
    EditComportamentoAdapter adapter;
    DbRepository database;
    int idLote;
    boolean edit_comp_lote;
    String nomeLote;

    List<TipoComportamento> listaTipos = new ArrayList<>();
    List<Comportamento> listaComportamentos = new ArrayList<>();
    FormularioComportamento formularioComportamento;

    TextView textAdicionarTipo;
    ImageButton imgAdicionarTipo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicao_comportamento);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        textAdicionarTipo = findViewById(R.id.lbl_tipo_adicionar_comportamento);
        imgAdicionarTipo = findViewById(R.id.imgAddTipo);
        database = new DbRepository(getApplicationContext());

        if(database.getFormularioPadrao(true) == null &&
                getIntent().hasExtra("lote_id")
                && getIntent().hasExtra("edit_comp_lote")
                && getIntent().hasExtra("lote_nome")){

            DateTime dateTime = new DateTime();
            String dataCriacao = dateTime.pegarData() + " " + dateTime.pegarHora();
            idLote = getIntent().getIntExtra("lote_id", 9999);
            edit_comp_lote = getIntent().getBooleanExtra("edit_comp_lote", true);
            nomeLote = getIntent().getStringExtra("lote_nome");

            database.insertFormularioComportamento(new FormularioComportamento(0, dataCriacao, true, -1));
            formularioComportamento = database.getFormularioPadrao(true);
            createPatternData();

            database.insertFormularioComportamento(new FormularioComportamento(0, dataCriacao, false, idLote));
            formularioComportamento = database.getFormulario(idLote);
            copyPatternIntoNewFormulario();
        }
        else if(database.getFormularioPadrao(true) == null){
            DateTime dateTime = new DateTime();
            String dataCriacao = dateTime.pegarData() + " " + dateTime.pegarHora();

            database.insertFormularioComportamento(new FormularioComportamento(0, dataCriacao, true, -1));
            formularioComportamento = database.getFormularioPadrao(true);
            createPatternData();
        }else if (
                getIntent().hasExtra("lote_id")
                        && getIntent().hasExtra("edit_comp_lote")
                        && getIntent().hasExtra("lote_nome")
        ){
            idLote = getIntent().getIntExtra("lote_id", 9999);
            edit_comp_lote = getIntent().getBooleanExtra("edit_comp_lote", true);
            nomeLote = getIntent().getStringExtra("lote_nome");

            if(database.getLoteAndFormulario(idLote).get(0).formularioComportamento != null){
                formularioComportamento = database.getLoteAndFormulario(idLote).get(0).formularioComportamento;
            }else{
                DateTime dateTime = new DateTime();
                String dataCriacao = dateTime.pegarData() + " " + dateTime.pegarHora();
                database.insertFormularioComportamento(new FormularioComportamento(0, dataCriacao, false, idLote));
                formularioComportamento = database.getFormulario(idLote);
                copyPatternIntoNewFormulario();
            }

        }else{
            formularioComportamento = database.getFormularioPadrao(true);
        }

        listaComportamentos = database.getAllComportamentos();
        setRecycler();
        setAddTipoClickListener();
        setData();
    }

    private List<TipoComportamento> getFormularioWithTipo(FormularioComportamento formularioComportamento){
        return  database.getFormularioWithTipoComportamento(
                formularioComportamento.getId())
                .get(0)
                .tiposComportamento;
    }

    private void copyPatternIntoNewFormulario(){

        for(TipoComportamento tipo : getFormularioWithTipo(database.getFormularioPadrao(true))){
            database.insertTipoComportamento(new TipoComportamento(0, tipo.getDescricao(), formularioComportamento.getId()));
        }
        List<TipoComportamento> tiposFormularioLote = getFormularioWithTipo(formularioComportamento);
        List<Comportamento> comportamentosPadrao = new ArrayList<>();

        for(Comportamento comportamento : database.getAllComportamentos()){
            TipoComportamento tipo = database.getTipo(comportamento.getIdTipo());
            if(tipo != null) {
                if (tipo.getIdFormularioComportamento() == database.getFormularioPadrao(true).getId()) {
                    comportamentosPadrao.add(comportamento);
                }
            }
        }

        for(Comportamento comportamento : comportamentosPadrao){
            for(TipoComportamento tipoComportamento : tiposFormularioLote){
                if(database.getTipo(comportamento.getIdTipo()).getDescricao().equals(tipoComportamento.getDescricao())){
                    database.insertComportamento(new Comportamento(0, comportamento.getNome(), tipoComportamento.getId()));
                    listaComportamentos.add(database.getComportamento(tipoComportamento.getId()));
                }
            }

        }
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

                adapter.submitItem(getFormularioWithTipo(formularioComportamento)
                        .get(getFormularioWithTipo(formularioComportamento).size() -1));

            }
        });

        imgAdicionarTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.insertTipoComportamento(new TipoComportamento(0, "", formularioComportamento.getId()));

                adapter.submitItem(getFormularioWithTipo(formularioComportamento)
                        .get(getFormularioWithTipo(formularioComportamento).size() -1));
            }
        });

    }

    private void setData(){
        listaTipos = database.getFormularioWithTipoComportamento(formularioComportamento.getId()).get(0).tiposComportamento;

        adapter.submitTipoList(listaTipos);
        adapter.submitComportamentoList(listaComportamentos);
    }

    private void createPatternData(){
        insertNewType("Comportamento Fisiológico", 0);
        insertNewType("Comportamento Reprodutivo", 0);
        insertNewType("Uso da Sombra", 0);

        for(TipoComportamento tipoComportamento : database.getAllTipos()){
            switch (tipoComportamento.getDescricao()){
                case "Comportamento Fisiológico":
                    insertNewComportamento("Pastejando", 0, tipoComportamento.getId());
                    insertNewComportamento("Ociosa em pé", 0, tipoComportamento.getId());
                    insertNewComportamento("Ociosa deitada", 0, tipoComportamento.getId());
                    insertNewComportamento("Ruminando em pé", 0, tipoComportamento.getId());
                    insertNewComportamento("Ruminando em deitada", 0, tipoComportamento.getId());
                    break;
                case "Comportamento Reprodutivo":
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
             insertNewComportamento(comportamento.getNome(), comportamento.getId(), comportamento.getIdTipo());
         }

         Toast.makeText(getApplicationContext(), "Formulário salvo!", Toast.LENGTH_SHORT).show();

         if(edit_comp_lote){
             Intent intent = new Intent(getApplicationContext(), ListaAnimais.class);
             intent.putExtra("lote_nome", nomeLote);
             intent.putExtra("lote_id",idLote);
             startActivity(intent);
         }
         finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(edit_comp_lote){
                    Intent intent = new Intent(getApplicationContext(), ListaAnimais.class);
                    intent.putExtra("lote_nome", nomeLote);
                    intent.putExtra("lote_id",idLote);
                    startActivity(intent);
                }
                finish();
                break;
            default:break;
        }
        return true;
    }
}

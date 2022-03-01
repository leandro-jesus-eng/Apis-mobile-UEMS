package com.apis.features.edicaoComportamento;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.apis.R;
import com.apis.data.repositories.DbRepository;
import com.apis.data.repositories.EntitiesHandlerRepository;
import com.apis.features.animal_list.ListaAnimais;
import com.apis.model.Comportamento;
import com.apis.model.DateTime;
import com.apis.model.FormularioComportamento;
import com.apis.model.TipoComportamento;
import java.util.ArrayList;
import java.util.List;

public class EditComportamento extends AppCompatActivity {

    EditComportamentoAdapter _adapter;
    EditComportamentoAdapter adapter;
    DbRepository dbRepository;
    EntitiesHandlerRepository entitiesHandlerRepository;

    int idLote;
    boolean edit_comp_lote;
    String nomeLote;
    List<TipoComportamento> listaTipos = new ArrayList<>();
    List<Comportamento> listaComportamentos = new ArrayList<>();
    List<TipoComportamento> newTypes = new ArrayList<>();
    FormularioComportamento formularioComportamento;

    TextView textAdicionarTipo;
    ImageButton imgAdicionarTipo;
    private SwipeRefreshLayout swipeRefreshLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicao_comportamento);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        textAdicionarTipo = findViewById(R.id.lbl_tipo_adicionar_comportamento);
        imgAdicionarTipo = findViewById(R.id.imgAddTipo);
        dbRepository = new DbRepository(getApplicationContext());
        entitiesHandlerRepository = new EntitiesHandlerRepository(getApplicationContext());
        swipeRefreshLayout = findViewById(R.id.edit_comportamento_swipeRefresh);

        if(dbRepository.getFormularioPadrao(true) == null &&
                getIntent().hasExtra("lote_id")
                && getIntent().hasExtra("edit_comp_lote")
                && getIntent().hasExtra("lote_nome")){

            DateTime dateTime = new DateTime();
            String dataCriacao = dateTime.pegarData() + " " + dateTime.pegarHora();

            dbRepository.insertFormularioComportamento(new FormularioComportamento(0, dataCriacao, true, -1));
            formularioComportamento = dbRepository.getFormularioPadrao(true);
            createPatternData();

            idLote = getIntent().getIntExtra("lote_id", 9999);
            edit_comp_lote = getIntent().getBooleanExtra("edit_comp_lote", true);
            nomeLote = getIntent().getStringExtra("lote_nome");
            dbRepository.insertFormularioComportamento(new FormularioComportamento(0, dataCriacao, false, idLote));
            formularioComportamento = dbRepository.getFormulario(idLote);
            copyPatternIntoNewFormulario();
        }
        else if(dbRepository.getFormularioPadrao(true) == null){
            DateTime dateTime = new DateTime();
            String dataCriacao = dateTime.pegarData() + " " + dateTime.pegarHora();

            dbRepository.insertFormularioComportamento(new FormularioComportamento(0, dataCriacao, true, -1));
            formularioComportamento = dbRepository.getFormularioPadrao(true);
            createPatternData();
        }else if (
                getIntent().hasExtra("lote_id")
                        && getIntent().hasExtra("edit_comp_lote")
                        && getIntent().hasExtra("lote_nome")
        ){
            idLote = getIntent().getIntExtra("lote_id", 9999);
            edit_comp_lote = getIntent().getBooleanExtra("edit_comp_lote", true);
            nomeLote = getIntent().getStringExtra("lote_nome");

            if(dbRepository.getLoteAndFormulario(idLote).get(0).formularioComportamento != null){
                formularioComportamento = dbRepository.getLoteAndFormulario(idLote).get(0).formularioComportamento;
            }else{
                DateTime dateTime = new DateTime();
                String dataCriacao = dateTime.pegarData() + " " + dateTime.pegarHora();
                dbRepository.insertFormularioComportamento(new FormularioComportamento(0, dataCriacao, false, idLote));
                formularioComportamento = dbRepository.getFormulario(idLote);
                copyPatternIntoNewFormulario();
            }

        }else{
            formularioComportamento = dbRepository.getFormularioPadrao(true);
        }

        setRecycler();
        setAddTipoClickListener();
        setData();
        setupSwipeRefresh();
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
                submitTypeToAdapter();
            }
        });

        imgAdicionarTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitTypeToAdapter();
            }
        });

    }

    private void submitTypeToAdapter(){
        dbRepository.insertTipoComportamento(new TipoComportamento(0, "", formularioComportamento.getId()));

        List<TipoComportamento> tipos = dbRepository.getFormularioWithTipoComportamento(
                formularioComportamento.getId()).get(0).tiposComportamento;
        TipoComportamento tipo = tipos.get(tipos.size() - 1);

        adapter.submitItem(tipo);
        newTypes.add(tipo);
    }

    private void setData(){
        listaTipos = dbRepository.getFormularioWithTipoComportamento(formularioComportamento.getId()).get(0).tiposComportamento;
        List<Comportamento> tempListComp = new ArrayList<>();

        for (Comportamento comportamento : dbRepository.getAllComportamentos()) {
            for (TipoComportamento tipoComportamento : listaTipos) {
                if (comportamento.getIdTipo() == tipoComportamento.getId()) {
                    tempListComp.add(comportamento);
                }
            }
        }

        for(Comportamento comportamento : tempListComp){
            Log.i("COMPORTAMENTO", "TEMP:"+comportamento.getNome());
        }

        listaComportamentos.clear();
        for(Comportamento comportamento : tempListComp){
            if(!listaComportamentos.contains(comportamento)){
                listaComportamentos.add(comportamento);
            }
        }

        for(Comportamento comportamento : tempListComp){
            Log.i("COMPORTAMENTO", "FINAL:"+comportamento.getNome());
        }

        adapter.submitTipoList(listaTipos);
        adapter.submitComportamentoList(listaComportamentos);
    }

    private void createPatternData(){
        insertNewType("Comportamento Fisiológico", 0);
        insertNewType("Comportamento Reprodutivo", 0);
        insertNewType("Uso da Sombra", 0);

        for(TipoComportamento tipoComportamento : dbRepository.getAllTipos()){
            switch (tipoComportamento.getDescricao()){
                case "Comportamento Fisiológico":
                    insertNewComportamento("Pastejando", 0, tipoComportamento.getId());
                    insertNewComportamento("Ociosa em pé", 0, tipoComportamento.getId());
                    insertNewComportamento("Ociosa deitada", 0, tipoComportamento.getId());
                    insertNewComportamento("Ruminando em pé", 0, tipoComportamento.getId());
                    insertNewComportamento("Ruminando deitada", 0, tipoComportamento.getId());
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

    private void copyPatternIntoNewFormulario(){
        for(TipoComportamento tipo : getFormularioWithTipo(dbRepository.getFormularioPadrao(true))){
            dbRepository.insertTipoComportamento(new TipoComportamento(0, tipo.getDescricao(), formularioComportamento.getId()));
        }
        List<TipoComportamento> tiposFormularioLote = getFormularioWithTipo(formularioComportamento);
        List<Comportamento> comportamentosPadrao = new ArrayList<>();

        for(Comportamento comportamento : dbRepository.getAllComportamentos()){
            TipoComportamento tipo = dbRepository.getTipo(comportamento.getIdTipo());
            if(tipo != null) {
                if (tipo.getIdFormularioComportamento() == dbRepository.getFormularioPadrao(true).getId()) {
                    comportamentosPadrao.add(comportamento);
                }
            }
        }

        for(Comportamento comportamento : comportamentosPadrao){
            for(TipoComportamento tipoComportamento : tiposFormularioLote){
                if(dbRepository.getTipo(comportamento.getIdTipo()).getDescricao().equals(tipoComportamento.getDescricao())){
                    dbRepository.insertComportamento(new Comportamento(0, comportamento.getNome(), tipoComportamento.getId()));
                    listaComportamentos.add(dbRepository.getComportamento(tipoComportamento.getId()));
                }
            }
        }
    }

    public void saveData(View view){
        listaTipos = adapter.getTipos();
        listaComportamentos = adapter.getComportamentos();
        List<Comportamento> listaComportamentosExcluidos = adapter.getComportamentosExcluidos();
        List<TipoComportamento> listaTiposComportamentoExcluidos = adapter.getTiposExcluidos();

        for(Comportamento comportamento : listaComportamentosExcluidos){
            dbRepository.excluirComportamento(comportamento);
        }

        for(TipoComportamento tipoComportamento : listaTiposComportamentoExcluidos){
            dbRepository.excluirTipoComportamento(tipoComportamento);
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

    private List<TipoComportamento> getFormularioWithTipo(FormularioComportamento formularioComportamento){
        return  dbRepository.getFormularioWithTipoComportamento(
                formularioComportamento.getId())
                .get(0)
                .tiposComportamento;
    }

    private void insertNewType(String descricao, int id){
        boolean exist = false;

        for(TipoComportamento tipos :  dbRepository.getAllTipos()){
            if(descricao.equals(tipos.getDescricao()) || tipos.getId() == id){
                exist = true;
            }
        }

        if(exist){
            for(TipoComportamento tipoComportamento : dbRepository.getAllTipos()){
                if(tipoComportamento.getId() == id && tipoComportamento.getDescricao() != descricao){
                    tipoComportamento.setDescricao(descricao);
                    dbRepository.updateTipo(tipoComportamento);
                }
            }
        }else{
            dbRepository.insertTipoComportamento(new TipoComportamento(0, descricao, formularioComportamento.getId()));
        }
    }

    private void insertNewComportamento(String nome, int id, int tipoId){
        boolean exist = false;

        for(Comportamento comportamento :  dbRepository.getAllComportamentos()){
            if(nome.equals(comportamento.getNome()) || comportamento.getId() == id){
                exist = true;
            }
        }

        if(exist){
            for(Comportamento comportamento : dbRepository.getAllComportamentos()){
                if(comportamento.getId() == id && comportamento.getNome() != nome){
                    comportamento.setNome(nome);
                    dbRepository.updateComportamento(comportamento);
                }
            }
        }else{
            dbRepository.insertComportamento(new Comportamento(id, nome, tipoId));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
                if(!newTypes.isEmpty()){
                    for(TipoComportamento tipo : newTypes){
                        dbRepository.excluirTipoComportamento(tipo);
                    }
                }
                newTypes.clear();
                finish();
                break;
            default:break;
        }
        return true;
    }
}

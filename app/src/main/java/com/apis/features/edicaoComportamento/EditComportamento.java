package com.apis.features.edicaoComportamento;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.apis.R;
import com.apis.data.repositories.DbRepository;
import com.apis.data.repositories.EntitiesHandlerRepository;
import com.apis.features.animal_list.ListaAnimais;
import com.apis.features.usecases.ManageUser;
import com.apis.model.Comportamento;
import com.apis.model.DateTime;
import com.apis.model.FormularioLote;
import com.apis.model.FormularioPadrao;
import com.apis.model.TipoComportamento;
import com.apis.model.User;
import java.util.ArrayList;
import java.util.List;

public class EditComportamento extends AppCompatActivity {

    EditComportamentoAdapter _adapter;
    private EditComportamentoAdapter adapter;
    private DbRepository dbRepository;
    EntitiesHandlerRepository entitiesHandlerRepository;

    private List<TipoComportamento> listaTipos = new ArrayList<>();
    private List<Comportamento> listaComportamentos = new ArrayList<>();
    private final List<TipoComportamento> newTypes = new ArrayList<>();

    private String nomeLote;
    private final User user = new ManageUser().getUser();
    private final DateTime dateTime = new DateTime();
    private int formularioId;
    private int idLote;
    private boolean edit_comp_lote;

    private TextView textAdicionarTipo;
    private ImageButton imgAdicionarTipo;
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

        checkFormularioType();
        setRecycler();
        setAddTipoClickListener();
        setData();
        setupSwipeRefresh();
    }

    private void checkFormularioType() {
        if (
                getIntent().hasExtra("lote_id")
                        && getIntent().hasExtra("edit_comp_lote")
                        && getIntent().hasExtra("lote_nome")
        ) {
            idLote = getIntent().getIntExtra("lote_id", 9999);
            edit_comp_lote = getIntent().getBooleanExtra("edit_comp_lote", true);
            nomeLote = getIntent().getStringExtra("lote_nome");

            if (dbRepository.getLoteAndFormulario(idLote).get(0).formularioLote != null) {
                formularioId = dbRepository.getLoteAndFormulario(idLote).get(0).formularioLote.getId();
            } else {
                String dataCriacao = dateTime.pegarData() + " " + dateTime.pegarHora();
                dbRepository.insertFormularioLote(new FormularioLote(0, dataCriacao, idLote));
                formularioId = dbRepository.getFormularioLote(idLote).getId();
                copyPatternIntoNewFormulario();
            }
        } else {
            formularioId = dbRepository.getFormularioPadrao(user.getUserId()).getId();
        }
    }

    private void setRecycler() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_edit_comportamento);
        _adapter = new EditComportamentoAdapter(this);
        adapter = _adapter;
        recyclerView.setAdapter(adapter);
    }

    private void setAddTipoClickListener() {
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

    private void setData() {
        if (edit_comp_lote) listaTipos = getFormularioWithTipo(false);
        else listaTipos = getFormularioWithTipo(true);

        List<Comportamento> tempListComp = new ArrayList<>();

        for (Comportamento comportamento : dbRepository.getAllComportamentos()) {
            for (TipoComportamento tipoComportamento : listaTipos) {
                if (comportamento.getIdTipo() == tipoComportamento.getId()) {
                    tempListComp.add(comportamento);
                }
            }
        }

        listaComportamentos.clear();
        for (Comportamento comportamento : tempListComp) {
            if (!listaComportamentos.contains(comportamento)) {
                listaComportamentos.add(comportamento);
            }
        }

        adapter.submitTipoList(listaTipos);
        adapter.submitComportamentoList(listaComportamentos);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!newTypes.isEmpty()) {
                    for (TipoComportamento tipo : newTypes) {
                        dbRepository.excluirTipoComportamento(tipo);
                    }
                }
                newTypes.clear();
                finish();
                Intent intent = new Intent(getApplicationContext(), EditComportamento.class);
                if (edit_comp_lote) {
                    intent.putExtra("edit_comp_lote", true);
                    intent.putExtra("lote_id", idLote);
                    intent.putExtra("lote_nome", nomeLote);
                }
                startActivity(intent);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void submitTypeToAdapter() {
        List<TipoComportamento> tipos;
        TipoComportamento tipo;
        dbRepository.insertTipoComportamento(new TipoComportamento(0, "", formularioId));

        if (edit_comp_lote) tipos = getFormularioWithTipo(false);
        else tipos = getFormularioWithTipo(true);

        tipo = tipos.get(tipos.size() - 1);
        adapter.submitItem(tipo);
        newTypes.add(tipo);
    }

    private void copyPatternIntoNewFormulario() {
        for (TipoComportamento tipo : getFormularioWithTipo(true)) {
            dbRepository.insertTipoComportamento(new TipoComportamento(0, tipo.getDescricao(), formularioId));
        }
        List<TipoComportamento> tiposFormularioLote = getFormularioWithTipo(false);
        List<Comportamento> comportamentosPadrao = new ArrayList<>();

        for (Comportamento comportamento : dbRepository.getAllComportamentos()) {
            TipoComportamento tipo = dbRepository.getTipo(comportamento.getIdTipo());
            if (tipo != null) {
                if (tipo.getIdFormularioComportamento() == dbRepository.getFormularioPadrao(user.getUserId()).getId()) {
                    comportamentosPadrao.add(comportamento);
                }
            }
        }

        for (Comportamento comportamento : comportamentosPadrao) {
            for (TipoComportamento tipoComportamento : tiposFormularioLote) {
                if (dbRepository.getTipo(comportamento.getIdTipo()).getDescricao().equals(tipoComportamento.getDescricao())) {
                    dbRepository.insertComportamento(new Comportamento(0, comportamento.getNome(), tipoComportamento.getId()));
                    listaComportamentos.add(dbRepository.getComportamento(tipoComportamento.getId()));
                }
            }
        }
    }

    public void saveData(View view) {
        listaTipos = adapter.getTipos();
        listaComportamentos = adapter.getComportamentos();
        List<Comportamento> listaComportamentosExcluidos = adapter.getComportamentosExcluidos();
        List<TipoComportamento> listaTiposComportamentoExcluidos = adapter.getTiposExcluidos();

        for (Comportamento comportamento : listaComportamentosExcluidos) {
            dbRepository.excluirComportamento(comportamento);
        }

        for (TipoComportamento tipoComportamento : listaTiposComportamentoExcluidos) {
            dbRepository.excluirTipoComportamento(tipoComportamento);
        }

        for (TipoComportamento tipo : listaTipos) {
            insertNewType(tipo.getDescricao(), tipo.getId());
        }

        for (Comportamento comportamento : listaComportamentos) {
            insertNewComportamento(comportamento.getNome(), comportamento.getId(), comportamento.getIdTipo());
        }

        Toast.makeText(getApplicationContext(), "Formulário salvo!", Toast.LENGTH_SHORT).show();

        if (edit_comp_lote) {
            Intent intent = new Intent(getApplicationContext(), ListaAnimais.class);
            intent.putExtra("lote_nome", nomeLote);
            intent.putExtra("lote_id", idLote);
            startActivity(intent);
        }
        finish();
    }

    private List<TipoComportamento> getFormularioWithTipo(boolean padrao) {
        if (padrao) {
            int formularioPadrao = dbRepository.getFormularioPadrao(user.getUserId()).getId();
            return dbRepository.getFormularioPadraoWithTipoComportamento(formularioPadrao)
                    .get(0)
                    .tiposComportamento;
        }
        return dbRepository.getFormularioLoteWithTipoComportamento(formularioId)
                .get(0)
                .tiposComportamento;
    }

    private void insertNewType(String descricao, int id) {
        boolean exist = false;

        for (TipoComportamento tipos : dbRepository.getAllTipos()) {
            if ((descricao.equals(tipos.getDescricao()) || tipos.getId() == id) && formularioId == tipos.getIdFormularioComportamento()) {
                exist = true;
                break;
            }
        }

        if (exist) {
            for (TipoComportamento tipoComportamento : dbRepository.getAllTipos()) {
                if (tipoComportamento.getId() == id && !tipoComportamento.getDescricao().equals(descricao)) {
                    tipoComportamento.setDescricao(descricao);
                    dbRepository.updateTipo(tipoComportamento);
                }
            }
        } else {
            dbRepository.insertTipoComportamento(new TipoComportamento(0, descricao, formularioId));
        }
    }

    private void insertNewComportamento(String nome, int id, int tipoId) {
        boolean exist = false;

        for (Comportamento comportamento : dbRepository.getAllComportamentos()) {
            if ((nome.equals(comportamento.getNome()) || comportamento.getId() == id) && comportamento.getIdTipo() == tipoId) {
                exist = true;
                break;
            }
        }

        if (exist) {
            for (Comportamento comportamento : dbRepository.getAllComportamentos()) {
                if (comportamento.getId() == id && !comportamento.getNome().equals(nome)) {
                    comportamento.setNome(nome);
                    dbRepository.updateComportamento(comportamento);
                }
            }
        } else {
            dbRepository.insertComportamento(new Comportamento(id, nome, tipoId));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (edit_comp_lote) {
                Intent intent = new Intent(getApplicationContext(), ListaAnimais.class);
                intent.putExtra("lote_nome", nomeLote);
                intent.putExtra("lote_id", idLote);
                startActivity(intent);
            }
            if (!newTypes.isEmpty()) {
                for (TipoComportamento tipo : newTypes) {
                    dbRepository.excluirTipoComportamento(tipo);
                }
            }
            newTypes.clear();
            finish();
        }
        return true;
    }
}

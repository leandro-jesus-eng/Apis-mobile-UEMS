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
    static Boolean padrao = true;
    List<TipoComportamento> lista = new ArrayList<>();

    TextView textAdicionarTipo;
    ImageButton imgAdicionarTipo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicao_comportamento_padrao);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        textAdicionarTipo = findViewById(R.id.lbl_tipo_adicionar_comportamento);
        imgAdicionarTipo = findViewById(R.id.imgAddTipo);

        setRecycler();
        setAddTipoClickListener();

        if(padrao){
            setPatternData();
        }else{
            setData();
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
                adapter.submitItem(new TipoComportamento(0, "", 0));
            }
        });

        imgAdicionarTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.submitItem(new TipoComportamento(0, "", 0));
            }
        });

    }

    private void setPatternData(){
        DateTime dateTime = new DateTime();
        String dataCriacao = dateTime.pegarData() +" "+ dateTime.pegarHora();

        //FormularioComportamento formularioComportamento= new FormularioComportamento(0, dataCriacao, true, 0);
        lista.add(new TipoComportamento(0, "Fisiológico", 0));
        lista.add(new TipoComportamento(0, "Reprodutivo", 0));
        lista.add(new TipoComportamento(0, "Uso da Sombra", 0));

        adapter.submitList(lista);
    }

    private void setData(){
        adapter.submitList(lista);
    }

     public void saveData(View view){
         padrao = false;
         lista = adapter.getTipos();
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

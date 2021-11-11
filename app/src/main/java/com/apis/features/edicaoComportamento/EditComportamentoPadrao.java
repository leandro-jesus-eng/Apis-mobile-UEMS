package com.apis.features.edicaoComportamento;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apis.R;
import com.apis.features.animal_list.AnimalAdapter;

import java.util.ArrayList;
import java.util.List;

public class EditComportamentoPadrao extends AppCompatActivity {

    TextView text;
    EditComportamentoAdapter _adapter;
    EditComportamentoAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicao_comportamento_padrao);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_edit_comportamento);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        List<String> lista = new ArrayList<>();
        lista.add("Ola");
        lista.add("Tudo bom?");
        _adapter = new EditComportamentoAdapter(lista, this);
        adapter = _adapter;

        recyclerView.setAdapter(adapter);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);

        text = findViewById(R.id.lbl_adicionar_comportamento);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> lista = new ArrayList<>();
                lista.add("Ola");
                lista.add("Tudo bom?");
                adapter.submitList(lista);
            }
        });

    }

    private void saveData(){

    }

    private void excluirComportamento(){

    }
    private void updateComportamento(){

    }
    private void adicionarComportamento(){

    }

    private void excluirTipo(){

    }
    private void adicionarTipo(){

    }
    private void updateTipo(){

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

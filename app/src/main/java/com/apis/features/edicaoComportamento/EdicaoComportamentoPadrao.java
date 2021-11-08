package com.apis.features.edicaoComportamento;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.apis.R;

public class EdicaoComportamentoPadrao extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicao_comportamento_padrao);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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

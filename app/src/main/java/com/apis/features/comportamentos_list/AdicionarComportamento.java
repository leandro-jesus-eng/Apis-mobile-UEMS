package com.apis.features.comportamentos_list;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.apis.R;
import com.apis.data.repositories.DbRepository;
import com.apis.features.comportamento_reprodutivo.AdicionarCompReprodutivo;
import com.apis.features.settings.AlarmReceiver;
import com.apis.features.usecases.ManageUser;
import com.apis.model.Animal;
import com.apis.model.AnotacaoComportamento;
import com.apis.model.Comportamento;
import com.apis.model.DateTime;
import com.apis.model.FileControl;
import com.apis.model.Lote;
import com.apis.model.TipoComportamento;
import com.apis.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdicionarComportamento extends AppCompatActivity {

    private String nomeAnimal;
    private String nomeLote;
    private String nomeOutraVaca;
    private int idAnimal;
    private int idLote;
    private String comportamento = "";
    private String comportamentoReprodutivo = "";
    private String comportamentoOutraVaca = "";
    private String obS = "";
    private Integer formularioId;
    private Boolean temReprodutivo = false;

    private final DateTime dateTime = new DateTime();
    final private List<CheckBox> listComportamentoView = new ArrayList<>();
    List<TipoComportamento> tiposComportamentoReprodutivo = new ArrayList<>();
    List<Comportamento> comportamentosReprodutivos = new ArrayList<>();
    private FloatingActionButton goToReprodutivo;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final User user = new ManageUser().getUser();

    DbRepository database;
    LinearLayout layout;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_comportamento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        database = new DbRepository(this);
        layout = findViewById(R.id.linearLayoutAddComp);
        swipeRefreshLayout = findViewById(R.id.adicionar_comportamento_swipeRefresh);
        goToReprodutivo = findViewById(R.id.fab);

        pegarDadosActivityPassada();
        pegarUltimaAtualizacao();
        configurarListaComportamentos();
        setList();

        getSupportActionBar().setTitle(nomeAnimal);

        //Arquivo de comportamentos temporarios
        FileControl fc = new FileControl(getApplicationContext());
        fc.deleteTmpFile();

        //Click do botão 'Salvar'
        Button btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                salvarDados();
            }
        });

        //Click do botão 'Exportar dados'
        Button btnExportar = (Button) findViewById(R.id.btnExportarDados);
        btnExportar.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                exportarDados();
            }
        });

        goToReprodutivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdicionarCompReprodutivo.class);
                intent.putExtra("animal_nome", nomeAnimal);
                intent.putExtra("animal_id", idAnimal);
                intent.putExtra("lote_id", idLote);
                startActivity(intent);
                finish();
            }
        });
        setupSwipeRefresh();
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setList();
                configurarListaComportamentos();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setList() {
        List<TipoComportamento> tiposComportamento;
        if (database.getLoteAndFormulario(idLote).get(0).formularioLote == null) {
            formularioId = database.getFormularioPadrao(user.getUserId()).getId();
            tiposComportamento = getFormularioWithTipo(true);
        } else {
            formularioId = database.getLoteAndFormulario(idLote).get(0).formularioLote.getId();
            tiposComportamento = getFormularioWithTipo(false);
        }

        List<Comportamento> comportamentos = database.getAllComportamentos();

        for (TipoComportamento tipoComportamento : tiposComportamento) {
            if (
                    tipoComportamento.getDescricao().equals("Comportamento Reprodutivo") ||
                            tipoComportamento.getDescricao().equals("Reprodutivo") ||
                            tipoComportamento.getDescricao().equals("reprodutivo") ||
                            tipoComportamento.getDescricao().equals("comportamento reprodutivo")
            ) {
                temReprodutivo = true;
                break;
            }
        }

        layout.removeAllViews();
        for (TipoComportamento tipoComportamento : tiposComportamento) {
            createTextView(tipoComportamento.getDescricao(), false);

            for (Comportamento comportamento : comportamentos) {
                if (comportamento.getIdTipo() == tipoComportamento.getId()) {

                    if (comportamentoReprodutivo.equals("")) {
                        createCheckListView(comportamento.getNome());
                    }

                    if (!comportamentoReprodutivo.equals("") &&
                            (!tipoComportamento.getDescricao().equals("Comportamento Reprodutivo") &&
                                    !tipoComportamento.getDescricao().equals("Reprodutivo") &&
                                    !tipoComportamento.getDescricao().equals("reprodutivo") &&
                                    !tipoComportamento.getDescricao().equals("comportamento reprodutivo"))
                    ) {
                        createCheckListView(comportamento.getNome());
                    }
                }
            }
            if (!comportamentoReprodutivo.equals("") &&
                    (tipoComportamento.getDescricao().equals("Comportamento Reprodutivo") ||
                            tipoComportamento.getDescricao().equals("Reprodutivo") ||
                            tipoComportamento.getDescricao().equals("reprodutivo") ||
                            tipoComportamento.getDescricao().equals("comportamento reprodutivo"))) {
                createTextView("*" + comportamentoReprodutivo, true);
            }
        }
        if (!comportamentoReprodutivo.equals("") && !temReprodutivo) {
            createTextView("Comportamento Reprodutivo", false);
            createTextView("*" + comportamentoReprodutivo, true);
        }
    }

    private Animal findAnimalByName(String nomeAnimal) {
        List<Animal> animais = database.getAnimais(idLote);
        for (Animal animal : animais) {
            if (animal.getNome().equals(nomeAnimal)) {
                return animal;
            }
        }
        return null;
    }

    private void createTextView(String texto, Boolean avisoDeAnotacaoReprodutiva) {
        TextView textTipoComportamento = new TextView(this);
        textTipoComportamento.setText(texto);

        if (avisoDeAnotacaoReprodutiva) {
            textTipoComportamento.setTextColor(getResources().getColor(R.color.colorGreen));
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(32, 5, 5, 5);
        textTipoComportamento.setLayoutParams(params);
        layout.addView(textTipoComportamento);
    }

    private void createCheckListView(String texto) {
        CheckBox checkComportamento = new CheckBox(this);
        checkComportamento.setText(texto);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(32, 5, 5, 1);

        checkComportamento.setLayoutParams(params);
        layout.addView(checkComportamento);
        listComportamentoView.add(checkComportamento);
    }

    public void exportarDados() {

        DbRepository database = new DbRepository(this);
        database.exportarDados(idLote, idAnimal);
        Lote lote = database.getLote(idLote);
        Animal animal = database.getAnimal(idLote, idAnimal);

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);

        File files[] = getBaseContext().getExternalFilesDirs(null);
        File fileWithinMyDir = null;
        if (files.length > 0) {
            fileWithinMyDir = new File(files[0], FileControl.getNameOfAnimalCSV(lote, animal));
        } else {
            Toast.makeText(getApplicationContext(), "APP não possui permissão para salvar no dispositivo!", Toast.LENGTH_SHORT).show();
        }

        if (fileWithinMyDir.exists()) {
            intentShareFile.setType("application/pdf");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(files[0].getAbsolutePath() + "/" + FileControl.getNameOfAnimalCSV(lote, animal)));

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Dados" + nomeLote + " - " + nomeAnimal);

            startActivity(Intent.createChooser(intentShareFile, "Enviar para"));
        }
    }


    public void configurarListaComportamentos() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerComportamento);

        List<AnotacaoComportamento> comportamentos = database.getAnimalWithAnotacao(idAnimal).get(0).anotacaoComportamentos;

        Collections.reverse(comportamentos);

        recyclerView.setAdapter(new ComportamentoAdapter(comportamentos, this));
        LinearLayoutManager layout = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layout);
    }

    private void pegarDadosActivityPassada() {

        if (getIntent().hasExtra("animal_nome") && getIntent().hasExtra("animal_id") && getIntent().hasExtra("lote_id")) {
            nomeAnimal = getIntent().getStringExtra("animal_nome");
            idAnimal = getIntent().getIntExtra("animal_id", 9999);
            idLote = getIntent().getIntExtra("lote_id", 9999);
            nomeLote = database.getNomeLote(idLote);
        }

        if (database.getLoteAndFormulario(idLote).get(0).formularioLote == null) {
            formularioId = database.getFormularioPadrao(user.getUserId()).getId();
        } else {
            formularioId = database.getLoteAndFormulario(idLote).get(0).formularioLote.getId();
        }

        if (getIntent().hasExtra("anotacao_vaca")) {
            comportamentoReprodutivo = getIntent().getStringExtra("anotacao_vaca");
            nomeOutraVaca = getIntent().getStringExtra("outra_vaca_nome");
            comportamentoOutraVaca = getIntent().getStringExtra("anotacao_outra_vaca");

            List<TipoComportamento> tiposComportamento = new ArrayList<>();

            if (database.getLoteAndFormulario(idLote).get(0).formularioLote == null) {
                tiposComportamento = getFormularioWithTipo(true);
            } else {
                tiposComportamento = getFormularioWithTipo(false);
            }

            for (TipoComportamento tipoComportamento : tiposComportamento) {
                if (tipoComportamento.getDescricao().equals("Comportamento Reprodutivo")) {
                    tiposComportamentoReprodutivo.add(tipoComportamento);
                }
            }

            for (TipoComportamento tipoComportamento : tiposComportamentoReprodutivo) {
                for (Comportamento comportamento : database.getAllComportamentos()) {
                    if (comportamento.getIdTipo() == tipoComportamento.getId()) {
                        comportamentosReprodutivos.add(comportamento);
                    }
                }
            }
        }
    }

    private void pegarUltimaAtualizacao() {
        TextView atualizadoEm = (TextView) findViewById(R.id.atualizadoEm);
        List<Animal> animais = database.getAnimais(idLote);
        String lastUpdate = "";
        for (Animal animal : animais) {
            if (animal.getId() == idAnimal) {
                lastUpdate = database.getLastUpdateAnimal(animal);
            }
        }

        //Verifica se irá mandar notificações
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean receberNotificacao = sharedPreferences.getBoolean("notifications", false);
        String intervaloAtualizacao = sharedPreferences.getString("intervalo_atualizacao", "0");

        if (lastUpdate != "") {
            atualizadoEm.setText(receberNotificacao ? "Notificações ativadas (" + intervaloAtualizacao + "min)" : "Notificações desativadas");
        } else {
            atualizadoEm.setText("Não existem registros para este animal.");
        }

    }

    public void salvarDados() {

        for (CheckBox viewComportamento : listComportamentoView) {
            if (viewComportamento.isChecked()) {
                comportamento += viewComportamento.getText() + "; ";
            }
        }
        String c = comportamento;
        if (!comportamentoReprodutivo.isEmpty()) {
            comportamento += comportamentoReprodutivo;
        }

        EditText txtObs = (EditText) findViewById(R.id.textObs);
        obS = txtObs.getText().toString();

        ///Alerta para confirmação dos dados
        LayoutInflater layoutInflater = LayoutInflater.from(AdicionarComportamento.this);
        View promptView = layoutInflater.inflate(R.layout.alert_comportamento, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdicionarComportamento.this);
        alertDialogBuilder.setView(promptView);

        final TextView lblNome = (TextView) promptView.findViewById(R.id.lbl_nome_alert);
        final TextView lblComportamento = (TextView) promptView.findViewById(R.id.lbl_comportamento);
        final TextView lblObs = (TextView) promptView.findViewById(R.id.lbl_obs_alert);

        lblNome.setText(nomeAnimal);
        lblComportamento.setText(comportamento.replace(";", " | "));
        lblObs.setText(obS);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int id) {

                        ////Salva no BD
                        AnotacaoComportamento anotacaoVaca = new AnotacaoComportamento(
                                0, nomeAnimal, idAnimal, dateTime.pegarData(), dateTime.pegarHora(), comportamento, obS);

                        try {
                            if (!comportamentoReprodutivo.isEmpty()) {
                                Animal outra_vaca = findAnimalByName(nomeOutraVaca);
                                AnotacaoComportamento anotacaoOutraVaca = new AnotacaoComportamento(
                                        0,
                                        nomeOutraVaca,
                                        outra_vaca.getId(),
                                        dateTime.pegarData(),
                                        dateTime.pegarHora(),
                                        comportamentoOutraVaca,
                                        "");
                                database.insertAnotacaoComportamento(anotacaoOutraVaca);

                                String horaDefinida = dateTime.pegarHora();
                                String dataDefinida = dateTime.pegarData();

                                database.setLastUpdateAnimal(outra_vaca.getId(), dataDefinida + " " + horaDefinida);
                                salvarTxt(outra_vaca.getId(), nomeOutraVaca, dataDefinida, horaDefinida, comportamentoOutraVaca, "");
                            }

                            database.insertAnotacaoComportamento(anotacaoVaca);
                            //Trata o horário
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            boolean usarNtp = sharedPreferences.getBoolean("datetime_web", false);

                            String horaDefinida = dateTime.pegarHora();
                            String dataDefinida = dateTime.pegarData();

                            database.setLastUpdateAnimal(idAnimal, dataDefinida + " " + horaDefinida);
                            salvarTxt(idAnimal, nomeAnimal, dataDefinida, horaDefinida, comportamento, obS);

                            //Verifica se o usuário quer receber a notificação
                            boolean receberNotificacao = sharedPreferences.getBoolean("notifications", false);

                            if (receberNotificacao) {

                                //Pega o intervalo de atualizações das preferencias do  app
                                String intervaloAtualizacaoString = sharedPreferences.getString("intervalo_atualizacao", "0");
                                int intervaloAtualizacao = Integer.parseInt(intervaloAtualizacaoString);

                                //Define o lembrete de adicionar mais dados
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    definirAlarme(intervaloAtualizacao);
                                }
                            }

                            Toast.makeText(getApplicationContext(), "Salvo!", Toast.LENGTH_LONG).show();

                            //Arquivo de comportamentos temporarios
                            FileControl fc = new FileControl(getApplicationContext());
                            fc.deleteTmpFile();

                            finish();
                        } catch (Throwable throwable) {
                            Toast.makeText(getApplicationContext(), "Erro ao salvar!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                comportamento = "";
                                obS = "";
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void salvarTxt(int idAnimal, String nomeAnimal, String data, String hora, String comportamento, String obS) {

        String conteudo = idAnimal + ";" + nomeAnimal + ";" + data + ";" + hora + ";" + comportamento + ";" + obS;

        Lote lote = database.getLote(idLote);

        try {
            try {

                File files[] = getBaseContext().getExternalFilesDirs(null);

                File f = null;
                if (files.length > 0) {
                    f = new File(files[0], FileControl.getNameOfLoteCSV(lote));
                    if (!f.exists()) {
                        f.createNewFile();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "APP não possui permissão para salvar no dispositivo!", Toast.LENGTH_SHORT).show();
                    return;
                }

                FileOutputStream out = new FileOutputStream(f, true);
                out.write(conteudo.getBytes());
                out.write('\n');
                out.flush();
                out.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void definirAlarme(int tempo) {
        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("animal_nome", nomeAnimal);
        intent.putExtra("animal_id", idAnimal);
        intent.putExtra("lote_id", idLote);
        intent.putExtra("lote_nome", nomeLote);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + tempo * 60000, alarmIntent);
        alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + tempo * 60000, alarmIntent);
    }

    private List<TipoComportamento> getFormularioWithTipo(boolean padrao) {
        if (padrao) {
            int formularioPadrao = database.getFormularioPadrao(user.getUserId()).getId();
            return database.getFormularioPadraoWithTipoComportamento(formularioPadrao)
                    .get(0)
                    .tiposComportamento;
        }
        return database.getFormularioLoteWithTipoComportamento(formularioId)
                .get(0)
                .tiposComportamento;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}
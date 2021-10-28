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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.apis.R;
import com.apis.database.DbRepository;
import com.apis.database.relations.AnimalWithAnotacao;
import com.apis.features.others.AlarmReceiver;
import com.apis.models.Animal;
import com.apis.models.AnotacaoComportamento;
import com.apis.models.DateTime;
import com.apis.models.FileControl;
import com.apis.models.Lote;
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
    private int idAnimal;
    private int idLote;

    private String comportamento = "";
    private String obS = "";

    private DateTime dateTime = new DateTime();
    DbRepository database = new DbRepository(this);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_comportamento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        pegarDadosActivityPassada();
        pegarUltimaAtualizacao();
        configurarListaComportamentos();

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


    }

    public void exportarDados() {

        DbRepository database = new DbRepository(this);
        database.exportarDados(idLote, idAnimal);
        Lote lote = database.getLote(idLote);
        Animal animal = database.getAnimal(idLote, idAnimal);

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);

        File files[] = getBaseContext().getExternalFilesDirs(null);
        File fileWithinMyDir = null;
        if(files.length > 0) {
            fileWithinMyDir = new File( files[0] , FileControl.getNameOfAnimalCSV(lote, animal));
        } else {
            Toast.makeText(getApplicationContext(), "APP não possui permissão para salvar no dispositivo!", Toast.LENGTH_SHORT).show();
        }

        if(fileWithinMyDir.exists()) {
            intentShareFile.setType("application/pdf");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(files[0].getAbsolutePath() + "/"+FileControl.getNameOfAnimalCSV(lote, animal) ));

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

    private void pegarDadosActivityPassada(){

        if (getIntent().hasExtra("animal_nome") && getIntent().hasExtra("animal_id") && getIntent().hasExtra("lote_id")){
            nomeAnimal = getIntent().getStringExtra("animal_nome");
            idAnimal = getIntent().getIntExtra("animal_id", 9999);
            idLote = getIntent().getIntExtra("lote_id", 9999);

            nomeLote = database.getNomeLote(idLote);

        }

    }

    private void pegarUltimaAtualizacao() {
        TextView atualizadoEm = (TextView) findViewById(R.id.atualizadoEm);
        List<Animal> animais = database.getAnimais(idLote);
        String lastUpdate = "";
        for (Animal animal : animais){
            if(animal.getId() == idAnimal){
                lastUpdate = database.getLastUpdateAnimal(animal);
            }
        }

        //Verifica se irá mandar notificações
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean receberNotificacao = sharedPreferences.getBoolean("notifications", false);
        String intervaloAtualizacao = sharedPreferences.getString("intervalo_atualizacao", "0");

        if(lastUpdate != ""){
            atualizadoEm.setText(receberNotificacao ? "Notificações ativadas ("+intervaloAtualizacao+"min)" : "Notificações desativadas");
        }else {
            atualizadoEm.setText("Não existem registros para este animal.");
        }

    }

    public void salvarDados(){

        ///Pega os dados
        RadioGroup btnGroupFisio = (RadioGroup) findViewById(R.id.btnGroupFisio);
        switch (btnGroupFisio.getCheckedRadioButtonId()) {
            case R.id.radioPastej:
                comportamento += "Pastejando;";
                break;
            case R.id.radioOP:
                comportamento +=  "Ociosa em pé;";
                break;
            case R.id.radioOD:
                comportamento +=  "Ociosa deitada;";
                break;
            case R.id.radioRumPe:
                comportamento +=  "Ruminando em pé;";
                break;
            case R.id.radioRumDeit:
                comportamento +=  "Ruminando deitada;";
                break;
            default:
                comportamento +=  ";";
                break;
        }

        RadioGroup btnGroupRepro = (RadioGroup) findViewById(R.id.btnGroupRepro);
        switch (btnGroupRepro.getCheckedRadioButtonId()) {
            case R.id.radioAcMonta:
                comportamento += "Aceita monta;";
                break;
            case R.id.radioMontaOutra:
                comportamento += "Monta outra;";
                break;
            case R.id.radioInqueta:
                comportamento += "Inquieta;";
                break;
            default:
                comportamento +=  ";";
                break;
        }

        RadioGroup btnGroupSombra = (RadioGroup) findViewById(R.id.btnGroupSombra);
        switch (btnGroupSombra.getCheckedRadioButtonId()) {
            case R.id.radioSol:
                comportamento += "Sol;";
                break;
            case R.id.radioSombra:
                comportamento += "Sombra;";
                break;
            default:
                comportamento +=  ";";
                break;
        }

        EditText txtObs = (EditText) findViewById(R.id.textObs);
        obS = txtObs.getText().toString();
        ///Fim Pega os dados


        ///Alerta para confirmação dos dados
        LayoutInflater layoutInflater = LayoutInflater.from(AdicionarComportamento.this);
        View promptView = layoutInflater.inflate(R.layout.alert_comportamento, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdicionarComportamento.this);
        alertDialogBuilder.setView(promptView);

        final TextView lblNome = (TextView) promptView.findViewById(R.id.lbl_nome_alert);
        final TextView lblComportamento = (TextView) promptView.findViewById(R.id.lbl_comportamento);
        final TextView lblObs = (TextView) promptView.findViewById(R.id.lbl_obs_alert);

        lblNome.setText("Nome: "+nomeAnimal);
        lblComportamento.setText("Comportamento: "+comportamento.replace(";", " | "));
        lblObs.setText("Observação: "+obS);


        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int id) {

                        ////Salva no BD
                        AnotacaoComportamento newAnotacao = new AnotacaoComportamento(
                                0, nomeAnimal , idAnimal , dateTime.pegarData(), dateTime.pegarHora(), comportamento, obS);
                        try {

                            database.insertAnotacaoComportamento(newAnotacao);
                            //Trata o horário
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            boolean usarNtp = sharedPreferences.getBoolean("datetime_web", false);

                            String horaDefinida = dateTime.pegarHora();
                            String dataDefinida = dateTime.pegarData();

                            database.setLastUpdateAnimal(idAnimal, dataDefinida+" "+horaDefinida);
                            salvarTxt(idAnimal, nomeAnimal, dataDefinida, horaDefinida, comportamento, obS);

                            //Verifica se o usuário quer receber a notificação
                            boolean receberNotificacao = sharedPreferences.getBoolean("notifications", false);

                            if(receberNotificacao){

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
                        }catch (Throwable throwable){
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

    public void salvarTxt(int idAnimal, String nomeAnimal, String data, String hora, String comportamento, String obS){

        String conteudo = idAnimal+";"+nomeAnimal+";"+data+";"+hora+";"+comportamento+";"+obS;

        Lote lote = database.getLote(idLote);

        try {
                try {

                    File files[] = getBaseContext().getExternalFilesDirs(null);
                    File f = null;
                    if(files.length > 0) {
                        f = new File( files[0] , FileControl.getNameOfLoteCSV(lote));
                        if (!f.exists()){
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
    public void definirAlarme(int tempo){
        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("animal_nome", nomeAnimal);
        intent.putExtra("animal_id", idAnimal);
        intent.putExtra("lote_id", idLote);
        intent.putExtra("lote_nome", nomeLote);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,  SystemClock.elapsedRealtime() + tempo * 60000, alarmIntent);
        alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,  SystemClock.elapsedRealtime() + tempo * 60000, alarmIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                finish();
                break;
            default:break;
        }
        return true;
    }

}

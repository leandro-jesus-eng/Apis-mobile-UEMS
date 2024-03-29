package com.apis.features.lote_list;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.apis.R;
import com.apis.data.repositories.AuthenticationRepository;
import com.apis.data.repositories.DbRepository;
import com.apis.data.repositories.EntitiesHandlerRepository;
import com.apis.data.repositories.FirestoreRepository;
import com.apis.features.usecases.ManageUser;
import com.apis.features.edicaoComportamento.EditComportamento;
import com.apis.features.intro_app.IntroActivity;
import com.apis.features.settings.SettingsActivity;
import com.apis.features.sign_up.SignUpActivity;
import com.apis.model.Comportamento;
import com.apis.model.FormularioPadrao;
import com.apis.model.Lote;
import com.apis.model.TipoComportamento;
import com.apis.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String nomeLote;
    private String nomeExperimento;
    private User user;
    private int formularioId;
    private SwipeRefreshLayout swipeRefreshLayout;
    DbRepository dbRepository;
    EntitiesHandlerRepository entitiesHandlerRepository;
    FirestoreRepository firestoreRepository;
    AuthenticationRepository authenticationRepository;

    String CHANNEL_ID = "main.notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeRefreshLayout = findViewById(R.id.lote_list_swipe_refresh);

        //Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        dbRepository = new DbRepository(this);
        entitiesHandlerRepository = new EntitiesHandlerRepository(this);
        firestoreRepository = new FirestoreRepository(this);
        authenticationRepository = new AuthenticationRepository();

        firestoreRepository.setupRemoteChangeListener();
        pedirPermissoes();
        createNotificationChannel();
        setupSwipeRefresh();
        setupFloatingButton();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null) {
            User newUser = new User(firebaseAuth.getCurrentUser().getEmail());
            if (!entitiesHandlerRepository.userExists(newUser.getEmail())) {
                dbRepository.insertUser(newUser);
            }
            user = dbRepository.getUser(newUser.getEmail());
        }
        if (getIntent().hasExtra("user_from_login")){
            user = (User) getIntent().getSerializableExtra("user_from_login");
        }
        if (getIntent().hasExtra("user_from_sign_up")){
            user = (User) getIntent().getSerializableExtra("user_from_sign_up");
        }
        hasUserLogged(user);
        new ManageUser().setUser(user);
        configurarLista();
        formularioPadraoExist();
    }

    private void hasUserLogged(User user) {
        if (user == null) {
            Log.i("Usuario", "usuario: nulo");
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        } else {
            Log.i("Usuario", "usuario email: " + user.getEmail());
            Log.i("Usuario", "usuario id: " + user.getUserId());
        }
    }

    private void logoutUser() {
        authenticationRepository.logoutUser();
        user = null;
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                configurarLista();
                formularioPadraoExist();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void configurarLista() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerAnimais);
        TextView nenhumLote = (TextView) findViewById(R.id.textNenhumLote);
        ImageView alertImg  = (ImageView) findViewById(R.id.alertImgLotes);

        List<Lote> visibleLotes = dbRepository.selectVisibleLotes(user);

        if(visibleLotes.size() > 0) {
            nenhumLote.setVisibility(View.INVISIBLE);
            alertImg.setVisibility(View.INVISIBLE);
        }else {
            nenhumLote.setVisibility(View.VISIBLE);
            alertImg.setVisibility(View.VISIBLE);
        }

        recyclerView.setAdapter(new LoteAdapter(visibleLotes, this, user));
        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
    }

    private void setupFloatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarLote();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
    }

    public void salvarLote() {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.prompt_lote, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText txtNomeLote =  promptView.findViewById(R.id.lote_name_editText);
        final EditText txtExperimento = promptView.findViewById(R.id.lote_experiment_editText);

        txtNomeLote.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        nomeLote = txtNomeLote.getText().toString();
                        nomeExperimento = txtExperimento.getText().toString();

                        boolean camposValidos = true;
                        if(nomeLote.equals("")){
                            camposValidos = false;
                        }

                        if(camposValidos){

                            boolean loteJaExiste = false;
                            if(entitiesHandlerRepository.loteExiste(nomeLote.trim(), nomeExperimento.trim())){
                                loteJaExiste = true;
                            }

                            if(loteJaExiste) {
                                Toast.makeText(getApplicationContext(), "O lote com esse nome e experimento já existe!", Toast.LENGTH_LONG).show();

                            } else {
                                ////Salva no BD
                                Lote novoLote = new Lote(0 , nomeLote, nomeExperimento);

                                try {
                                    dbRepository.insertLote(novoLote, user.getUserId());
                                    finish();
                                    startActivity(getIntent());
                                }catch (Throwable throwable){
                                    Toast.makeText(getApplicationContext(), "Erro ao salvar!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "O lote não pode ter um nome em branco!", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void formularioPadraoExist() {
        if (dbRepository.getFormularioPadrao(user.getUserId()) == null) {
            dbRepository.insertFormularioPadrao(new FormularioPadrao(0, user.getUserId()));
            formularioId = dbRepository.getFormularioPadrao(user.getUserId()).getId();
            createPatternData();
        } else {
            formularioId = dbRepository.getFormularioPadrao(user.getUserId()).getId();
        }
    }

    private void createPatternData(){
        insertNewType("Comportamento Fisiológico");
        insertNewType("Comportamento Reprodutivo");
        insertNewType("Uso da Sombra");

        for(TipoComportamento tipoComportamento : getFormularioPadraoWithTipo()){
            switch (tipoComportamento.getDescricao()){
                case "Comportamento Fisiológico":
                    insertNewComportamento("Pastejando", tipoComportamento.getId());
                    insertNewComportamento("Ociosa em pé", tipoComportamento.getId());
                    insertNewComportamento("Ociosa deitada", tipoComportamento.getId());
                    insertNewComportamento("Ruminando em pé", tipoComportamento.getId());
                    insertNewComportamento("Ruminando deitada", tipoComportamento.getId());
                    break;
                case "Comportamento Reprodutivo":
                    insertNewComportamento("Aceita de monta", tipoComportamento.getId());
                    insertNewComportamento("Monta outra", tipoComportamento.getId());
                    insertNewComportamento("Inquieta", tipoComportamento.getId());
                    break;
                case "Uso da Sombra":
                    insertNewComportamento("Sol", tipoComportamento.getId());
                    insertNewComportamento("Sombra", tipoComportamento.getId());
                    break;
            }
        }
    }

    private void insertNewType(String descricao) {
        dbRepository.insertTipoComportamento(new TipoComportamento(0, descricao, formularioId));
    }

    private void insertNewComportamento(String nome, int tipoId){
        dbRepository.insertComportamento(new Comportamento(0, nome, tipoId));
    }

    private List<TipoComportamento> getFormularioPadraoWithTipo() {
        int formularioPadrao = dbRepository.getFormularioPadrao(user.getUserId()).getId();
        return dbRepository.getFormularioPadraoWithTipoComportamento(formularioPadrao)
                .get(0)
                .tiposComportamento;
    }

    final static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.VIBRATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    final static String[] PERMISSIONS_INTERNET = {
            Manifest.permission.INTERNET
    };
    final static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    final static String[] PERMISSIONS_WRITE_EXTERNAL_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.INTERNET
    };

    public void pedirPermissoes() {

        int writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    1
            );
        }

        int internetPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (internetPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_INTERNET,
                    1
            );
        }

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_LOCATION,
                    1
            );
        }

        permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_WRITE_EXTERNAL_STORAGE,
                    1
            );
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notificações";
            String description = "Lembrete de atualização de dados";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            this.startActivity(intent);

        }else if (id == R.id.nav_edicao_comportamentos) {
            Intent intent = new Intent(this, EditComportamento.class);
            this.startActivity(intent);

        }else if (id == R.id.nav_intro) {
            Intent intent = new Intent(this, IntroActivity.class);
            this.startActivity(intent);

        } else if (id == R.id.nav_github) {
            String url = "https://github.com/leandro-jesus-eng/Apis-mobile-UEMS";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else  {
            logoutUser();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() { }
}

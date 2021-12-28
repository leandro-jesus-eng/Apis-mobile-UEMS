package com.apis.database;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.apis.database.DAOs.AnimalDao;
import com.apis.database.DAOs.AnotacaoComportamentoDao;
import com.apis.database.DAOs.ComportamentoDao;
import com.apis.database.DAOs.FormularioComportamentoDao;
import com.apis.database.DAOs.LoteDao;
import com.apis.database.DAOs.RelationsDao;
import com.apis.database.DAOs.TipoComportamentoDao;
import com.apis.model.Animal;
import com.apis.model.AnotacaoComportamento;
import com.apis.model.Comportamento;
import com.apis.model.FormularioComportamento;
import com.apis.model.Lote;
import com.apis.model.TipoComportamento;

@androidx.room.Database(entities = {
        Animal.class,
        Lote.class,
        FormularioComportamento.class,
        TipoComportamento.class,
        Comportamento.class,
        AnotacaoComportamento.class,
}, version = 2, exportSchema = false)
abstract public class Database extends RoomDatabase {

    public abstract AnimalDao animalDao();
    public abstract LoteDao loteDao();
    public abstract AnotacaoComportamentoDao anotacaoComportamentoDao();
    public abstract ComportamentoDao comportamentoDao();
    public abstract FormularioComportamentoDao formularioComportamentoDao();
    public abstract TipoComportamentoDao tipoComportamentoDao();
    public abstract RelationsDao relationsDao();

    private static Database database;
    final private static String DATABASE_NAME = "Apis.db";

    public synchronized static Database getInstance(Context context){

        if(database == null){

            database = Room.databaseBuilder(
                    context.getApplicationContext(),
                    Database.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
}

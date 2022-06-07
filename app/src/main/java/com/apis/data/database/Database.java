package com.apis.data.database;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.apis.data.database.DAOs.AnimalDao;
import com.apis.data.database.DAOs.AnotacaoComportamentoDao;
import com.apis.data.database.DAOs.ComportamentoDao;
import com.apis.data.database.DAOs.FormularioComportamentoDao;
import com.apis.data.database.DAOs.LoteDao;
import com.apis.data.database.DAOs.RelationsDao;
import com.apis.data.database.DAOs.TipoComportamentoDao;
import com.apis.data.database.DAOs.UserDao;
import com.apis.data.database.relations.UserLoteCrossRef;
import com.apis.model.Animal;
import com.apis.model.AnotacaoComportamento;
import com.apis.model.Comportamento;
import com.apis.model.FormularioLote;
import com.apis.model.Lote;
import com.apis.model.TipoComportamento;
import com.apis.model.User;

@androidx.room.Database(entities = {
        Animal.class,
        Lote.class,
        FormularioLote.class,
        TipoComportamento.class,
        Comportamento.class,
        AnotacaoComportamento.class,
        User.class,
        UserLoteCrossRef.class
}, version = 4, exportSchema = false)
abstract public class Database extends RoomDatabase {

    public abstract AnimalDao animalDao();
    public abstract LoteDao loteDao();
    public abstract AnotacaoComportamentoDao anotacaoComportamentoDao();
    public abstract ComportamentoDao comportamentoDao();
    public abstract FormularioComportamentoDao formularioComportamentoDao();
    public abstract TipoComportamentoDao tipoComportamentoDao();
    public abstract UserDao userDao();
    public abstract RelationsDao relationsDao();

    private static Database database;
    final private static String DATABASE_NAME = "Apis.db";

    public synchronized static Database getInstance(Context context) {

        if (database == null) {

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
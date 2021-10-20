package com.apis.database;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.apis.database.DAOs.AnimalDao;
import com.apis.database.DAOs.AnotacaoComportamentoDao;
import com.apis.database.DAOs.ComportamentoDao;
import com.apis.database.DAOs.FormularioComportamentoDao;
import com.apis.database.DAOs.LoteDao;
import com.apis.models.Animal;
import com.apis.models.Lote;
import com.apis.models.TipoComportamento;

@androidx.room.Database(entities = {Animal.class, Lote.class}, version = 1, exportSchema = false)
abstract public class Database extends RoomDatabase {

    public abstract AnimalDao animalDao();
    public abstract LoteDao loteDao();
    public abstract AnotacaoComportamentoDao anotacaoComportamentoDao();
    public abstract ComportamentoDao comportamentoDao();
    public abstract FormularioComportamentoDao formularioComportamentoDao();
    public abstract TipoComportamento tipoComportamento();

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

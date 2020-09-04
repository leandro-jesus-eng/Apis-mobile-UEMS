package com.apis.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Apis.db";
    private static final int DATABASE_VERSION = 1;
    private final String CREATE_TABLE_LOTE = "CREATE TABLE Lote (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nome TEXT NOT NULL, experimento TEXT);";
    private final String CREATE_TABLE_ANIMAL = "CREATE TABLE Animal (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nome TEXT NOT NULL, Lote_id INTEGER NOT NULL, FOREIGN KEY(Lote_id) REFERENCES Lote(id));";
    private final String CREATE_TABLE_COMPORTAMENTO = "CREATE TABLE Comportamento (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Animal_nome TEXT NOT NULL, Animal_id INTEGER NOT NULL, data DATE NOT NULL, hora TIME NOT NULL, comportamento TEXT NOT NULL, observacao TEXT, FOREIGN KEY(Animal_id) REFERENCES Animal(id));";
    private final String CREATE_TABLE_PREFERENCIA = "CREATE TABLE Preferencia (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nome TEXT NOT NULL, valor TEXT);";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_LOTE);
        sqLiteDatabase.execSQL(CREATE_TABLE_ANIMAL);
        sqLiteDatabase.execSQL(CREATE_TABLE_COMPORTAMENTO);
        sqLiteDatabase.execSQL(CREATE_TABLE_PREFERENCIA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}


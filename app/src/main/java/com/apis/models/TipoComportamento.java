package com.apis.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "tb_tipoComportamento")
public class TipoComportamento implements Serializable {

    @PrimaryKey(autoGenerate = true)
    final private int id;

    @ColumnInfo(name = "descricao")
    final private String descricao;

    @ColumnInfo(name = "comportamentos")
    final private ArrayList<Comportamento> comportamentos;

    public TipoComportamento(int id, String descricao, ArrayList<Comportamento> comportamentos){
        this.id = id;
        this.descricao = descricao;
        this.comportamentos = comportamentos;
    }

    public int getId() { return id;}
    public String getDescricao() { return descricao; }
    public ArrayList<Comportamento> getComportamentos() { return comportamentos; }


}

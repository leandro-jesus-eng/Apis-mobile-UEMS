package com.apis.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "tb_formularioComportamento")
public class FormularioComportamento implements Serializable {

    @PrimaryKey(autoGenerate = true)
    final private int id;

    @ColumnInfo(name = "dataCriacao")
    final private Date dataCriacao;

    @ColumnInfo(name = "formularioPadrao")
    final private boolean formularioPadrao;

    @ColumnInfo(name = "loteId")
    final private int loteId;


    public FormularioComportamento(int id, Date dataCriacao, boolean formularioPadrao, int loteId){
        this.id = id;
        this.dataCriacao = dataCriacao;
        this.formularioPadrao = formularioPadrao;
        this.loteId = loteId;
    }
    public int getId() { return id; }
    public Date getDataCriacao() { return dataCriacao; }
    public boolean getPadrao() { return formularioPadrao; }
    public int getLoteId() { return loteId; }
}

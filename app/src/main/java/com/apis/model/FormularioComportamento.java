package com.apis.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "tb_formularioComportamento")
public class FormularioComportamento implements Serializable {

    @PrimaryKey(autoGenerate = true)
    final private int id;

    @ColumnInfo(name = "dataCriacao")
    final private String dataCriacao;

    @ColumnInfo(name = "formularioPadrao")
    final private boolean formularioPadrao;

    @ColumnInfo(name = "loteId")
    final private int loteId;

    public FormularioComportamento(int id, String dataCriacao, boolean formularioPadrao, int loteId){
        this.id = id;
        this.dataCriacao = dataCriacao;
        this.formularioPadrao = formularioPadrao;
        this.loteId = loteId;
    }

    public int getId() { return id; }
    public String getDataCriacao() { return dataCriacao; }
    public boolean isFormularioPadrao() { return formularioPadrao; }
    public int getLoteId() { return loteId; }
}

package com.apis.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "tb_formularioComportamento")
public class FormularioComportamento implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "dataCriacao")
    private String dataCriacao;

    @ColumnInfo(name = "formularioPadrao")
    private boolean formularioPadrao;

    @ColumnInfo(name = "loteId")
    private int loteId;

    public FormularioComportamento(int id, String dataCriacao, boolean formularioPadrao, int loteId){
        this.id = id;
        this.dataCriacao = dataCriacao;
        this.formularioPadrao = formularioPadrao;
        this.loteId = loteId;
    }

    public FormularioComportamento(){}

    public int getId() { return id; }
    public String getDataCriacao() { return dataCriacao; }
    public boolean isFormularioPadrao() { return formularioPadrao; }
    public int getLoteId() { return loteId; }

    public void setId(int id) { this.id = id; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }
    public void setFormularioPadrao(boolean formularioPadrao) { this.formularioPadrao = formularioPadrao; }
    public void setLoteId(int loteId) { this.loteId = loteId; }
}

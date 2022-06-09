package com.apis.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "tb_formulario_lote")
public class FormularioLote implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "dataCriacao")
    private String dataCriacao;

    @ColumnInfo(name = "loteId")
    private int loteId;

    public FormularioLote(int id, String dataCriacao, int loteId){
        this.id = id;
        this.dataCriacao = dataCriacao;
        this.loteId = loteId;
    }

    public FormularioLote(){}

    public int getId() { return id; }
    public String getDataCriacao() { return dataCriacao; }
    public int getLoteId() { return loteId; }

    public void setId(int id) { this.id = id; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }
    public void setLoteId(int loteId) { this.loteId = loteId; }
}
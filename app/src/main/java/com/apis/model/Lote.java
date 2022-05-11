package com.apis.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "tb_lote")
public class Lote implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int loteId;

    @ColumnInfo(name = "nome")
    private String nome;

    @ColumnInfo(name = "experimento")
    private String experimento;

    public Lote(int id, String nome, String experimento){
        this.loteId = id;
        this.nome = nome;
        this.experimento = experimento;
    }

    public Lote(){}

    public int getLoteId(){ return this.loteId; }
    public String getNome(){ return this.nome; }
    public String getExperimento(){ return this.experimento; }

    public void setLoteId(int id) { this.loteId = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setExperimento(String experimento) { this.experimento = experimento; }

    @Override
    public boolean equals(Object o){
        return this.loteId == ((Lote)o).loteId;
    }

    @Override
    public int hashCode(){
        return this.loteId;
    }
}
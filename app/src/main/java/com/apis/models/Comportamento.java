package com.apis.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "tb_comportamento")
public class Comportamento implements Serializable {

    @PrimaryKey(autoGenerate = true)
    final private int id;

    @ColumnInfo(name = "nome")
    final private String nome;

    @ColumnInfo(name = "descricaoTipo")
    final private String descricaoTipo;

    public Comportamento(int id, String nome, String descricaoTipo){
       this.id = id;
       this.nome = nome;
       this.descricaoTipo = descricaoTipo;
    }

    public int getId() { return id; }
    public String getNome(){return nome;}
    public String getDescricaoTipo() { return descricaoTipo; }

    @Override
    public boolean equals(Object o){
        return this.id == ((Comportamento)o).id;
    }

    @Override
    public int hashCode(){
        return this.id;
    }

}

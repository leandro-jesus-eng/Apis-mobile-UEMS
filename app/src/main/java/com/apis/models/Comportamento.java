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

    @ColumnInfo(name = "tipo")
    final private TipoComportamento tipo;

    public Comportamento(int id, String nome, TipoComportamento tipo){
       this.id = id;
       this.nome = nome;
       this.tipo = tipo;
    }

    public int getId() { return id; }
    public String getNome(){return nome;}

    public TipoComportamento getTipo(){ return tipo;}

    @Override
    public boolean equals(Object o){
        return this.id == ((Comportamento)o).id;
    }

    @Override
    public int hashCode(){
        return this.id;
    }

}

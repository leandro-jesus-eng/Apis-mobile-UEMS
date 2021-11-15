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
    private String nome;

    @ColumnInfo(name = "idTipo")
    final private int idTipo;

    public Comportamento(int id, String nome, int idTipo){
       this.id = id;
       this.nome = nome;
       this.idTipo = idTipo;
    }

    public int getId() { return id; }
    public String getNome(){return nome;}
    public int getIdTipo() { return idTipo; }
    public void setNome(String nome){this.nome = nome;}

    @Override
    public boolean equals(Object o){
        return this.id == ((Comportamento)o).id;
    }

    @Override
    public int hashCode(){
        return this.id;
    }

}

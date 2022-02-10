package com.apis.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "tb_comportamento")
public class Comportamento implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "nome")
    private String nome;

    @ColumnInfo(name = "idTipo")
    private int idTipo;

    public Comportamento(int id, String nome, int idTipo){
       this.id = id;
       this.nome = nome;
       this.idTipo = idTipo;
    }

    public Comportamento(){}

    public int getId() { return id; }
    public String getNome(){return nome;}
    public int getIdTipo() { return idTipo; }


    public void setId(int id) { this.id = id; }
    public void setNome(String nome){this.nome = nome;}
    public void setIdTipo(int idTipo) { this.idTipo = idTipo; }

    @Override
    public boolean equals(Object o){
        return this.id == ((Comportamento)o).id;
    }

    @Override
    public int hashCode(){
        return this.id;
    }
}

package com.apis.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "tb_anotacaoComportamento")
public class AnotacaoComportamento implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "animalNome")
    private String nomeAnimal;

    @ColumnInfo(name = "animalId")
    private int idAnimal;

    @ColumnInfo(name = "data")
    private String data;

    @ColumnInfo(name = "hora")
    private String hora;

    @ColumnInfo(name = "nomeComportamento")
    private String nomeComportamento;

    @ColumnInfo(name = "obs")
    private String obs;

    public AnotacaoComportamento(int id, String nomeAnimal, int idAnimal, String data, String hora, String nomeComportamento, String obs){
        this.id = id;
        this.nomeAnimal = nomeAnimal;
        this.idAnimal = idAnimal;
        this.data = data;
        this.hora = hora;
        this.nomeComportamento = nomeComportamento;
        this.obs = obs;
    }

    public AnotacaoComportamento(){}

    public int getId() { return id; }
    public String getNomeAnimal() { return nomeAnimal; }
    public int getIdAnimal() { return idAnimal; }
    public String getData() { return data; }
    public String getHora() { return hora; }
    public String getNomeComportamento() { return nomeComportamento; }
    public String getObs() { return obs; }

    public void setId(int id) { this.id = id; }
    public void setNomeAnimal(String nomeAnimal) { this.nomeAnimal = nomeAnimal; }
    public void setIdAnimal(int idAnimal) { this.idAnimal = idAnimal; }
    public void setData(String data) { this.data = data; }
    public void setHora(String hora) { this.hora = hora; }
    public void setNomeComportamento(String nomeComportamento) { this.nomeComportamento = nomeComportamento; }
    public void setObs(String obs) { this.obs = obs; }
}

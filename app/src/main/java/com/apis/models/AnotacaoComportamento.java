package com.apis.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "tb_anotacaoComportamento")
public class AnotacaoComportamento implements Serializable {

    @PrimaryKey(autoGenerate = true)
    final private int id;

    @ColumnInfo(name = "animalNome")
    final private String nomeAnimal;

    @ColumnInfo(name = "animalId")
    final private int idAnimal;

    @ColumnInfo(name = "data")
    final private String data;

    @ColumnInfo(name = "hora")
    final private String hora;

    @ColumnInfo(name = "nomeComportamento")
    final private String nomeComportamento;

    @ColumnInfo(name = "obs")
    final private String obs;

    public AnotacaoComportamento(int id, String nomeAnimal, int idAnimal, String data, String hora, String nomeComportamento, String obs){
        this.id = id;
        this.nomeAnimal = nomeAnimal;
        this.idAnimal = idAnimal;
        this.data = data;
        this.hora = hora;
        this.nomeComportamento = nomeComportamento;
        this.obs = obs;
    }

    public int getId() { return id; }
    public String getNomeAnimal() { return nomeAnimal; }
    public int getIdAnimal() { return idAnimal; }
    public String getData() { return data; }
    public String getHora() { return hora; }
    public String getNomeComportamento() { return nomeComportamento; }
    public String getObs() { return obs; }

}

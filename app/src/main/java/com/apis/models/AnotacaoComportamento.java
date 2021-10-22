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

    @ColumnInfo(name = "hora")
    final private String hora;

    @ColumnInfo(name = "data")
    final private String data;

    @ColumnInfo(name = "obs")
    final private String obs;

    @ColumnInfo(name = "comportamento")
    final private Comportamento comportamento;

    public AnotacaoComportamento(int id, String nomeAnimal, int idAnimal, String data, String hora, String obs, Comportamento comportamento){
        this.id = id;
        this.nomeAnimal = nomeAnimal;
        this.idAnimal = idAnimal;
        this.data = data;
        this.hora = hora;
        this.obs = obs;
        this.comportamento = comportamento;
    }

    public int getId() { return id; }
    public String getNomeAnimal() { return nomeAnimal; }
    public int getIdAnimal() { return idAnimal; }
    public String getDataHora() { return data+" "+hora; }
    public String getObs() { return obs; }
    public Comportamento getComportamento() { return comportamento; }

}

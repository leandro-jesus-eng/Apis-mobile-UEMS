package com.apis.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "tb_tipoComportamento")
public class TipoComportamento implements Serializable {

    @PrimaryKey(autoGenerate = true)
    final private int id;

    @ColumnInfo(name = "descricao")
    private String descricao;

    final private int idFormularioComportamento;

    public TipoComportamento(int id, String descricao, int idFormularioComportamento){
        this.id = id;
        this.descricao = descricao;
        this.idFormularioComportamento = idFormularioComportamento;
    }

    public int getId() { return id;}
    public String getDescricao() { return descricao; }
    public int getIdFormularioComportamento() { return idFormularioComportamento; }
    public void setDescricao(String novaDescricao){
        this.descricao = novaDescricao;
    }
}
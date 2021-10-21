package com.apis.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "tb_formularioComportamento")
public class FormularioComportamento implements Serializable {

    @PrimaryKey(autoGenerate = true)
    final private int id;

    @ColumnInfo(name = "dataCriacao")
    final private Date dataCriacao;

    @ColumnInfo(name = "tiposComportamento")
    final private ArrayList<TipoComportamento> tiposComportamento;

    @ColumnInfo(name = "formularioPadrao")
    final private boolean formularioPadrao;

    public FormularioComportamento(
            int id, Date dataCriacao, ArrayList<TipoComportamento> tiposComportamento, boolean formularioPadrao){
        this.id = id;
        this.dataCriacao = dataCriacao;
        this.tiposComportamento = tiposComportamento;
        this.formularioPadrao = formularioPadrao;
    }
    public int getId() { return id; }
    public Date getDataCriacao() { return dataCriacao; }
    public ArrayList<TipoComportamento> getTiposComportamento() { return tiposComportamento; }
    public boolean getPadrao() { return formularioPadrao; }

}

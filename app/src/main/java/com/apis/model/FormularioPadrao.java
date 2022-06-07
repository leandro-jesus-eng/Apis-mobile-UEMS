package com.apis.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "tb_formulario_padrao")
public class FormularioPadrao implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "userId")
    private Integer userId;

    public FormularioPadrao(int id, Integer userId){
        this.id = id;
        this.userId = userId;
    }

    public FormularioPadrao(){}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}

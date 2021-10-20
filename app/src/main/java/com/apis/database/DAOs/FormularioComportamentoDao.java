package com.apis.database.DAOs;

import androidx.room.Dao;

import com.apis.models.Comportamento;

import java.util.List;

@Dao
public interface FormularioComportamentoDao {

    void insertFormulario();

    void deleteFormulario();

    Comportamento returnFormulario();

    List<Comportamento> returnAllFormulario();
}

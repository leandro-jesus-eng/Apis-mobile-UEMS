package com.apis.database.DAOs;

import androidx.room.Dao;

import com.apis.models.Comportamento;

import java.util.List;

@Dao
public interface AnotacaoComportamentoDao {

    void insertAnotacao();

    void deleteAnotacao();

    Comportamento returnAnotacao();

    List<Comportamento> returnAllAnotacoes();
}

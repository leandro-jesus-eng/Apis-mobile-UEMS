package com.apis.database.DAOs;

import androidx.room.Dao;
import com.apis.models.TipoComportamento;
import java.util.List;

@Dao
public interface TipoComportamentoDao {

    void insertAnotacao();

    void deleteAnotacao();

    TipoComportamento returnAnotacao();

    List<TipoComportamento> returnAllAnotacoes();

}

package com.apis.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apis.models.TipoComportamento;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TipoComportamentoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTipo(TipoComportamento tipoComportamento);

    @Delete
    void deleteTipo(TipoComportamento tipoComportamento);

    @Query("DELETE FROM tb_tipoComportamento")
    void deleteAllTipos();

    @Query("SELECT * FROM tb_tipoComportamento WHERE id = :idTipo")
    TipoComportamento returnTipo(int idTipo);

    @Query("SELECT * FROM tb_tipoComportamento")
    ArrayList<TipoComportamento> returnAllTipos();

}

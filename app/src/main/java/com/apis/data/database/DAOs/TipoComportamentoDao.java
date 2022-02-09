package com.apis.data.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.apis.model.TipoComportamento;
import java.util.List;

@Dao
public interface TipoComportamentoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTipo(TipoComportamento tipoComportamento);

    @Delete
    void deleteTipo(TipoComportamento tipoComportamento);

    @Query("DELETE FROM tb_tipoComportamento")
    void deleteAllTipos();

    @Query("SELECT * FROM tb_tipoComportamento WHERE id = :idTipo")
    TipoComportamento getTipo(int idTipo);

    @Query("SELECT * FROM tb_tipoComportamento ORDER BY id ASC")
    List<TipoComportamento> getAllTipos();

    @Update
    void updateTipo(TipoComportamento tipoComportamento);

}

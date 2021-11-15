package com.apis.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.apis.models.Comportamento;
import com.apis.models.TipoComportamento;

import java.util.List;

@Dao
public interface ComportamentoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComportamento(Comportamento comportamento);

    @Delete
    void deleteComportamento(Comportamento comportamento);

    @Query("DELETE FROM tb_comportamento")
    void deleteAllComportamentos();

}

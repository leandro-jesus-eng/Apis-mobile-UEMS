package com.apis.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("SELECT * FROM tb_comportamento")
    List<Comportamento> getAllComportamentos();

    @Update
    void updateComportamento(Comportamento comportamento);

}

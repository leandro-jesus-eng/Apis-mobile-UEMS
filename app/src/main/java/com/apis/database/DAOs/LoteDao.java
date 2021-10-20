package com.apis.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apis.models.Animal;
import com.apis.models.Lote;

import java.util.List;

@Dao
public interface LoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertLote(String nome, String experimento);

    @Delete
    void deleteLote(int loteId);

    @Query("SELECT * FROM tb_lote WHERE id = :loteId")
    Lote returnLote(int loteId);

    @Query("SELECT * FROM tb_lote")
    List<Lote> returnAllLotes();
}

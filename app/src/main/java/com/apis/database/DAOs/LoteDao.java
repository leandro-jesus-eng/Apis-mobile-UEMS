package com.apis.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apis.model.Lote;

import java.util.List;

@Dao
public interface LoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLote(Lote lote);

    @Delete
    void deleteLote(Lote lote);

    @Query("DELETE FROM tb_lote")
    void deleteAllLotes();

    @Query("SELECT * FROM tb_lote WHERE id = :loteId")
    Lote getLote(int loteId);

    @Query("SELECT * FROM tb_lote")
    List<Lote> getAllLotes();
}

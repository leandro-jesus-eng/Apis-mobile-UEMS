package com.apis.data.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apis.model.FormularioLote;

import java.util.List;

@Dao
public interface FormularioLoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFormulario(FormularioLote formularioComportamento);

    @Delete
    void deleteFormulario(FormularioLote formularioComportamento);

    @Query("DELETE FROM tb_formulario_lote")
    void deleteAllFormularios();

    @Query("SELECT * FROM tb_formulario_lote WHERE loteId = :loteId")
    FormularioLote getFormulario(int loteId);

    @Query("SELECT * FROM tb_formulario_lote WHERE formularioPadrao = :ehPadrao")
    FormularioLote getFormularioPadrao(boolean ehPadrao);

    @Query("SELECT * FROM tb_formulario_lote")
    List<FormularioLote> getAllFormularioComportamento();
}
package com.apis.data.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apis.model.Animal;
import com.apis.model.FormularioComportamento;

import java.util.List;

@Dao
public interface FormularioComportamentoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFormulario(FormularioComportamento formularioComportamento);

    @Delete
    void deleteFormulario(FormularioComportamento formularioComportamento);

    @Query("DELETE FROM tb_formularioComportamento")
    void deleteAllFormularios();

    @Query("SELECT * FROM tb_formularioComportamento WHERE loteId = :loteId")
    FormularioComportamento getFormulario(int loteId);

    @Query("SELECT * FROM tb_formularioComportamento WHERE formularioPadrao = :ehPadrao")
    FormularioComportamento getFormularioPadrao(boolean ehPadrao);

    @Query("SELECT * FROM tb_formularioComportamento")
    List<FormularioComportamento> getAllFormularioComportamento();
}

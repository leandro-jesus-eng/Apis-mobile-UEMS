package com.apis.data.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.apis.model.FormularioPadrao;

import java.util.List;

@Dao
public interface FormularioPadraoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFormularioPadrao(FormularioPadrao formularioComportamento);

    @Delete
    void deleteFormularioPadrao(FormularioPadrao formularioComportamento);

    @Query("SELECT * FROM tb_formulario_padrao WHERE userId = :userId")
    FormularioPadrao getFormularioPadrao(Integer userId);

    @Query("SELECT * FROM tb_formulario_padrao")
    List<FormularioPadrao> getAllFormularioPadrao();
}
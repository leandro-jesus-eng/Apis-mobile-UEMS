package com.apis.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apis.models.Comportamento;
import com.apis.models.FormularioComportamento;

import java.util.List;

@Dao
public interface FormularioComportamentoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertFormulario(FormularioComportamento formularioComportamento);

    @Delete
    void deleteFormulario(FormularioComportamento formularioComportamento);

    @Query("DELETE FROM tb_formularioComportamento")
    void deleteAllFormularios();

    @Query("SELECT * FROM tb_formularioComportamento WHERE id = :idFormulario")
    Comportamento returnFormulario(int idFormulario);

}

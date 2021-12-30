package com.apis.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.apis.model.AnotacaoComportamento;
import java.util.List;

@Dao
public interface AnotacaoComportamentoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAnotacao(AnotacaoComportamento anotacaoComportamento);

    @Delete
    void deleteAnotacao(AnotacaoComportamento anotacaoComportamento);

    @Query("DELETE FROM tb_anotacaoComportamento")
    void deleteAllAnotacoes();

    @Query("SELECT * FROM tb_anotacaoComportamento WHERE animalId = :animalId")
    List<AnotacaoComportamento> getAllAnotacoesAnimal(int animalId);
}

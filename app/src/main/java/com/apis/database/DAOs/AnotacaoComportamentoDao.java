package com.apis.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apis.models.Animal;
import com.apis.models.AnotacaoComportamento;
import com.apis.models.Comportamento;

import java.util.Date;
import java.util.List;

@Dao
public interface AnotacaoComportamentoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAnotacao(AnotacaoComportamento anotacaoComportamento);

    @Delete
    void deleteAnotacao(AnotacaoComportamento anotacaoComportamento);

    @Query("DELETE FROM tb_anotacaoComportamento")
    void deleteAllAnotacoes();

    @Query("SELECT * FROM tb_anotacaoComportamento WHERE id = :idAnotacao")
    AnotacaoComportamento returnAnotacao(int idAnotacao);

    @Query("SELECT * FROM tb_anotacaoComportamento WHERE animalId = :animalId")
    List<AnotacaoComportamento> returnAllAnotacoesAnimal(int animalId);
}

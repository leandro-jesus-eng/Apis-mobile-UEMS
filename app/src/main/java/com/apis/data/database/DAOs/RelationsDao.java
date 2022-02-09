package com.apis.data.database.DAOs;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.apis.data.database.relations.AnimalWithAnotacao;
import com.apis.data.database.relations.FormularioWithTipoComportamento;
import com.apis.data.database.relations.LoteAndFormulario;
import com.apis.data.database.relations.LoteWithAnimal;
import com.apis.data.database.relations.TipoComportamentoWithComportamento;

import java.util.List;

@Dao
public interface RelationsDao {

    @Transaction
    @Query("SELECT * FROM tb_lote WHERE id = :loteId")
    List<LoteWithAnimal> getLoteWithAnimal(int loteId);

    @Transaction
    @Query("SELECT * FROM tb_lote WHERE id = :loteId")
    List<LoteAndFormulario> getLoteAndFormulario(int loteId);

    @Transaction
    @Query("SELECT * FROM tb_formularioComportamento WHERE id = :formularioId")
    List<FormularioWithTipoComportamento> getFormularioWithTipoComportamento(int formularioId);

    @Transaction
    @Query("SELECT * FROM tb_tipoComportamento WHERE id = :tipoComportamentoId")
    List<TipoComportamentoWithComportamento> getTipoComportamentoWithComportamento(int tipoComportamentoId);

    @Transaction
    @Query("SELECT * FROM tb_animal WHERE id = :idAnimal")
    List<AnimalWithAnotacao> getAnimalWithAnotacao(int idAnimal);


}

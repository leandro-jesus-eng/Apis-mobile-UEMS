package com.apis.data.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.apis.data.database.relations.AnimalWithAnotacao;
import com.apis.data.database.relations.FormularioWithTipoComportamento;
import com.apis.data.database.relations.LoteAndFormulario;
import com.apis.data.database.relations.LoteWithAnimal;
import com.apis.data.database.relations.LoteWithUsers;
import com.apis.data.database.relations.TipoComportamentoWithComportamento;
import com.apis.data.database.relations.UserLoteCrossRef;

import java.util.List;

@Dao
public interface RelationsDao {

    @Transaction
    @Query("SELECT * FROM tb_lote WHERE loteId = :loteId")
    List<LoteWithAnimal> getLoteWithAnimal(int loteId);

    @Transaction
    @Query("SELECT * FROM tb_lote WHERE loteId = :loteId")
    List<LoteAndFormulario> getLoteAndFormulario(int loteId);

    @Transaction
    @Query("SELECT * FROM tb_formulario_lote WHERE id = :formularioId")
    List<FormularioWithTipoComportamento> getFormularioWithTipoComportamento(int formularioId);

    @Transaction
    @Query("SELECT * FROM tb_tipoComportamento WHERE id = :tipoComportamentoId")
    List<TipoComportamentoWithComportamento> getTipoComportamentoWithComportamento(int tipoComportamentoId);

    @Transaction
    @Query("SELECT * FROM tb_animal WHERE id = :idAnimal")
    List<AnimalWithAnotacao> getAnimalWithAnotacao(int idAnimal);

    @Transaction
    @Query("SELECT * FROM UserLoteCrossRef")
    List<UserLoteCrossRef> getAllUserLoteCrossRef();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserLoteCrossRef(UserLoteCrossRef userLoteCrossRef);

    @Delete
    void deleteUserLoteCrossRef(UserLoteCrossRef userLoteCrossRef);

    @Transaction
    @Query("SELECT * FROM tb_lote WHERE loteId = :loteId")
    List<LoteWithUsers> getUsersOfLote(int loteId);
}
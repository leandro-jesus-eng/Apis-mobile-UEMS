package com.apis.data.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.apis.model.Animal;
import java.util.List;

@Dao
public interface AnimalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAnimal(Animal animal);

    @Delete
    void deleteAnimal(Animal animal);

    @Query("DELETE FROM tb_animal")
    void deleteAllAnimais();

    @Query("SELECT * FROM tb_animal WHERE loteId = :loteId AND id = :animalId")
    Animal getAnimal(int loteId, int animalId);

    @Query("SELECT * FROM tb_animal WHERE loteId = :loteId")
    List<Animal> getAnimaisLote(int loteId);

    @Query("SELECT * FROM tb_animal")
    List<Animal> getAllAnimais();

    @Query("UPDATE tb_animal SET lastUpdate = :lastUpdate WHERE id = :animalId")
    void setLastUpdate(int animalId, String lastUpdate);
}

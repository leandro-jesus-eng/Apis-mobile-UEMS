package com.apis.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apis.models.Animal;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AnimalDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
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

}

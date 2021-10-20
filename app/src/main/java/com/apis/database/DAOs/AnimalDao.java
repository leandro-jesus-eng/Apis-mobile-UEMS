package com.apis.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apis.models.Animal;

import java.util.List;

@Dao
public interface AnimalDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAnimal(String nome, int loteId);

    @Delete
    void deleteAnimal(Animal animal);

    @Query("SELECT * FROM tb_animal WHERE idLote = :loteId AND id = :animalId")
    Animal returnAnimal(int loteId, int animalId);

    @Query("SELECT * FROM tb_animal WHERE idLote = :loteId")
    List<Animal> returnAnimaisLote(int loteId);

    @Query("SELECT * FROM tb_animal")
    List<Animal> returnAllAnimais(String nomeAnimal);

}
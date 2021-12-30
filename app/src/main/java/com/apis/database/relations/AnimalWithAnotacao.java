package com.apis.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.apis.model.Animal;
import com.apis.model.AnotacaoComportamento;

import java.util.List;

public class AnimalWithAnotacao {

    @Embedded
    public Animal animal;
    @Relation(
            parentColumn = "id",
            entityColumn = "animalId"
    )
    public List<AnotacaoComportamento> anotacaoComportamentos;
}

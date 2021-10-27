package com.apis.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.apis.models.Animal;
import com.apis.models.AnotacaoComportamento;
import com.apis.models.Comportamento;
import com.apis.models.TipoComportamento;
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

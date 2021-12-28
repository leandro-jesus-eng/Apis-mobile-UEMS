package com.apis.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.apis.model.Animal;
import com.apis.model.Lote;

import java.util.List;

public class LoteWithAnimal {

    @Embedded
    public Lote lote;
    @Relation(
            parentColumn = "id",
            entityColumn = "loteId"
    )
    public List<Animal> animais;
}

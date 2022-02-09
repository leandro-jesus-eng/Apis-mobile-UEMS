package com.apis.data.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.apis.model.FormularioComportamento;
import com.apis.model.Lote;

public class LoteAndFormulario {

    @Embedded
    public Lote lote;
    @Relation(
            parentColumn = "id",
            entityColumn = "loteId"
    )
    public FormularioComportamento formularioComportamento;
}

package com.apis.data.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.apis.model.FormularioLote;
import com.apis.model.Lote;

public class LoteAndFormulario {

    @Embedded
    public Lote lote;
    @Relation(
            parentColumn = "loteId",
            entityColumn = "loteId"
    )
    public FormularioLote formularioLote;
}

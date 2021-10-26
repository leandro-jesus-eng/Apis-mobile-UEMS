package com.apis.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.apis.models.Animal;
import com.apis.models.AnotacaoComportamento;
import com.apis.models.FormularioComportamento;
import com.apis.models.Lote;

import java.util.List;

public class LoteAndFormulario {

    @Embedded
    public Lote lote;
    @Relation(
            parentColumn = "id",
            entityColumn = "idLote"
    )
    public FormularioComportamento formularioComportamento;
}

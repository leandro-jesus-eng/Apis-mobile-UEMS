package com.apis.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.apis.models.FormularioComportamento;
import com.apis.models.TipoComportamento;

import java.util.List;

public class FormularioWithTipoComportamento {

    @Embedded public FormularioComportamento formularioComportamento;
    @Relation(
            parentColumn = "id",
            entityColumn = "idFormularioComportamento"
    )
    public List<TipoComportamento> tiposComportamento;
}

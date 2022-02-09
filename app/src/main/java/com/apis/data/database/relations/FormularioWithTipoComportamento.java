package com.apis.data.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.apis.model.FormularioComportamento;
import com.apis.model.TipoComportamento;

import java.util.List;

public class FormularioWithTipoComportamento {

    @Embedded public FormularioComportamento formularioComportamento;
    @Relation(
            parentColumn = "id",
            entityColumn = "idFormularioComportamento"
    )
    public List<TipoComportamento> tiposComportamento;
}

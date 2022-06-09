package com.apis.data.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.apis.model.FormularioPadrao;
import com.apis.model.TipoComportamento;

import java.util.List;

public class FormularioPadraoWithTipoComportamento {

    @Embedded public FormularioPadrao formularioPadrao;
    @Relation(
            parentColumn = "id",
            entityColumn = "idFormularioComportamento"
    )
    public List<TipoComportamento> tiposComportamento;
}
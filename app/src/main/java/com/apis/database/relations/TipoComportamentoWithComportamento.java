package com.apis.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.apis.models.Comportamento;
import com.apis.models.TipoComportamento;

import java.util.List;

public class TipoComportamentoWithComportamento {

    @Embedded public TipoComportamento tipoComportamento;
    @Relation(
            parentColumn = "id",
            entityColumn = "idTipo"
    )
    public List<Comportamento> comportamentos;
}

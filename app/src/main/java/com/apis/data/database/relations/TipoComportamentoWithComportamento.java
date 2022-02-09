package com.apis.data.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.apis.model.Comportamento;
import com.apis.model.TipoComportamento;

import java.util.List;

public class TipoComportamentoWithComportamento {

    @Embedded public TipoComportamento tipoComportamento;
    @Relation(
            parentColumn = "id",
            entityColumn = "idTipo"
    )
    public List<Comportamento> comportamentos;
}

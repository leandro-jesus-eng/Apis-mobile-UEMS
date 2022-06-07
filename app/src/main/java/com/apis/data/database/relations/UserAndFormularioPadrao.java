package com.apis.data.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.apis.model.FormularioPadrao;
import com.apis.model.User;

public class UserAndFormularioPadrao {

    @Embedded
    public User user;
    @Relation(
            parentColumn = "userId",
            entityColumn = "userId"
    )
    public FormularioPadrao formularioPadrao;
}
package com.apis.data.database.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.apis.model.Lote;
import com.apis.model.User;

import java.util.List;

public class LoteWithUsers {
    @Embedded Lote lote;
    @Relation(
            parentColumn = "id",
            entityColumn = "userId",
            associateBy = @Junction(UserLoteCrossRef.class)
    )
    List<User> userList;
}
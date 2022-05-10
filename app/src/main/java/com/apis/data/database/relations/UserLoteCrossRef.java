package com.apis.data.database.relations;

import androidx.room.Entity;

@Entity(primaryKeys = {"userId", "loteId"})
public class UserLoteCrossRef {
    Integer userId;
    Integer loteId;
}
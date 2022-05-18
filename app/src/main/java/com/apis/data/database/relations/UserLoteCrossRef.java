package com.apis.data.database.relations;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"userId", "loteId"})
public class UserLoteCrossRef {
    @NonNull public Integer userId;
    @NonNull public Integer loteId;

    public UserLoteCrossRef(@NonNull Integer userId, @NonNull Integer loteId) {
        this.userId = userId;
        this.loteId = loteId;
    }

    public UserLoteCrossRef() {}
}
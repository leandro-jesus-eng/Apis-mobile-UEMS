package com.apis.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.apis.models.Comportamento;
import java.util.List;

@Dao
public interface ComportamentoDao {

    void insertComportamento();

    void deleteComportamento();

    Comportamento returnComportamento();

    List<Comportamento> returnAllComportamentos();
}

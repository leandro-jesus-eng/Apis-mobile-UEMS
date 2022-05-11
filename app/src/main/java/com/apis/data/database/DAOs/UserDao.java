package com.apis.data.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.apis.model.Lote;
import com.apis.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM tb_user")
    void deleteAllUsers();

    @Query("SELECT * FROM tb_user WHERE userId = :userId")
    User getUser(int userId);

    @Query("SELECT * FROM tb_user")
    List<User> getAllUsers();
}
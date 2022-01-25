package com.example.pmuprojekat.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlayerDao {

    @Insert
    void insert(PlayerEntity... playerEntity);

    @Update
    void update(PlayerEntity... playerEntity);

    @Delete
    void delete(PlayerEntity playerEntity);

    @Query("Select * from PlayerEntity")
    List<PlayerEntity> getAll();

    @Query("Select * from PlayerEntity Where gameId = :id")
    List<PlayerEntity> getAllPlayersFromGame(long id);

    @Query("Delete from PlayerEntity Where gameId = :id ")
    void deleteAllPlayersFromGame(long id);
}

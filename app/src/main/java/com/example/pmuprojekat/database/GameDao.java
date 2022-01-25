package com.example.pmuprojekat.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GameDao {

    @Insert
    long insert(GameEntity gameEntity);

    @Update
    void update(GameEntity gameEntity);

    @Delete
    void delete(GameEntity gameEntity);

    @Query("Select * from GameEntity")
    List<GameEntity> getAll();

    @Query("Select * from GameEntity Where gameFinished = 0")
    List<GameEntity> getAllRunningGames();

    @Query("Select * from GameEntity Where gameFinished = 1")
    List<GameEntity> getAllFinishedGames();

    @Query("Delete from GameEntity where gameFinished = 0")
    void deleteAllRunningGames();

    @Query("Delete from GameEntity where gameFinished = 1")
    void deleteAllFinishedGames();

    @Query("Select * from GameEntity Where gameFinished = 1")
    LiveData<List<GameEntity>> getAllFinishedGamesLive();
}

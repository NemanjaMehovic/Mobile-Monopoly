package com.example.pmuprojekat.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ActionDao {

    @Insert
    long insert(ActionEntity actionEntity);

    @Update
    void update(ActionEntity actionEntity);

    @Delete
    void delete(ActionEntity actionEntity);

    @Query("Select * from ActionEntity")
    List<ActionEntity> getAll();

    @Query("Select * from ActionEntity Where gameId = :id")
    List<ActionEntity> getAllActionsFromGame(long id);

    @Query("Delete from ActionEntity Where gameId = :id ")
    void deleteAllActionsFromGame(long id);
}

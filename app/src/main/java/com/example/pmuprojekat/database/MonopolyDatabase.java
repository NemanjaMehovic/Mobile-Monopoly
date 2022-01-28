package com.example.pmuprojekat.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {GameEntity.class, ActionEntity.class, PlayerEntity.class}, version = 1, exportSchema = false)
public abstract class MonopolyDatabase extends RoomDatabase {

    public abstract GameDao gameDao();
    public abstract ActionDao actionDao();
    public abstract PlayerDao playerDao();

    private static final String DATABASE_NAME = "monopoly2-app.db";
    private static MonopolyDatabase instance = null;

    public static MonopolyDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (MonopolyDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MonopolyDatabase.class,
                            DATABASE_NAME)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return instance;
    }
}

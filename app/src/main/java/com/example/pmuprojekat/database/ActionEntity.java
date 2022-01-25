package com.example.pmuprojekat.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ActionEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long gameId;
    private String actionPerformed;

    public ActionEntity(long id, long gameId, String actionPerformed) {
        this.id = id;
        this.gameId = gameId;
        this.actionPerformed = actionPerformed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getActionPerformed() {
        return actionPerformed;
    }

    public void setActionPerformed(String actionPerformed) {
        this.actionPerformed = actionPerformed;
    }
}

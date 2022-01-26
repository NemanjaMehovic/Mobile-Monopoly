package com.example.pmuprojekat.database;

import androidx.lifecycle.LiveData;
import androidx.room.RoomDatabase;

import com.example.pmuprojekat.MainActivity;
import com.example.pmuprojekat.monopoly.Fields.BuyableField;
import com.example.pmuprojekat.monopoly.Fields.ChanceChestField;
import com.example.pmuprojekat.monopoly.Game;
import com.example.pmuprojekat.monopoly.Player;

import java.util.ArrayList;
import java.util.List;

public class GameRepository {

    private MainActivity mainActivity;
    private List<PlayerEntity> playerEntitiesList;
    private GameEntity game;

    public GameRepository(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void startNewGame(){
        Game currGame = Game.getInstance();
        List<Player> playersList = currGame.getPlayers();
        String[] data = getStringData(currGame);
        game = new GameEntity(0,0,currGame.isGameFinished(), data[0],
                data[1], data[2], currGame.getNumOfHousesLeft(),
                currGame.getNumOfHotelsLeft(),currGame.getCurrPlayerNum(),currGame.isAlreadyRolled());
        long newGameId = MonopolyDatabase.getInstance(mainActivity).gameDao().insert(game);
        game.setId(newGameId);
        playerEntitiesList = new ArrayList<>();
        for(Player p:playersList)
        {
            playerEntitiesList.add(new PlayerEntity(0, newGameId, p.getPlayerName(),
                    p.getCurrMoney(),p.getPosition(),p.getJailTime(),p.getNumRolled(),
                    p.getChanceJailFree(),p.getChestJailFree(),p.isLost()));
        }
        MonopolyDatabase.getInstance(mainActivity).playerDao().insert(playerEntitiesList.toArray(new PlayerEntity[0]));
        playerEntitiesList = MonopolyDatabase.getInstance(mainActivity).playerDao().getAllPlayersFromGame(game.getId());
    }

    public void deleteActiveGame(){
        List<GameEntity> gameEntities = MonopolyDatabase.getInstance(mainActivity).gameDao().getAllRunningGames();
        MonopolyDatabase.getInstance(mainActivity).gameDao().deleteAllRunningGames();
        for(GameEntity gameEntity:gameEntities)
        {
            MonopolyDatabase.getInstance(mainActivity).playerDao().deleteAllPlayersFromGame(gameEntity.getId());
            MonopolyDatabase.getInstance(mainActivity).actionDao().deleteAllActionsFromGame(gameEntity.getId());
        }
    }

    public List<GameEntity> getAllRunningGames(){
        return MonopolyDatabase.getInstance(mainActivity).gameDao().getAllRunningGames();
    }

    public void update(){
        Game currGame = Game.getInstance();
        String[] data = getStringData(currGame);
        game.setBuyableFieldOwners(data[0]);
        game.setBuyableFieldHouses(data[1]);
        game.setBuyableFieldMortage(data[2]);
        game.setCurrPlayerNum(currGame.getCurrPlayerNum());
        game.setNumOfHousesLeft(currGame.getNumOfHousesLeft());
        game.setNumOfHotelsLeft(currGame.getNumOfHotelsLeft());
        game.setGameFinished(currGame.isGameFinished());
        game.setRolled(currGame.isAlreadyRolled());

        List<Player> players = currGame.getPlayers();
        for(int i = 0; i < players.size(); i++)
        {
            Player currPlayer = players.get(i);
            PlayerEntity playerEntity = playerEntitiesList.get(i);
            playerEntity.setChanceJailFree(currPlayer.getChanceJailFree());
            playerEntity.setChestJailFree(currPlayer.getChestJailFree());
            playerEntity.setLost(currPlayer.isLost());
            playerEntity.setJailTime(currPlayer.getJailTime());
            playerEntity.setMoney(currPlayer.getCurrMoney());
            playerEntity.setName(currPlayer.getPlayerName());
            playerEntity.setNumRolled(currPlayer.getNumRolled());
            playerEntity.setPosition(currPlayer.getPosition());
        }

        MonopolyDatabase.getInstance(mainActivity).gameDao().update(game);
        MonopolyDatabase.getInstance(mainActivity).playerDao().update(playerEntitiesList.toArray(new PlayerEntity[0]));
    }

    public void loadGameFromDataBase(){
        List<GameEntity> list = getAllRunningGames();
        if(list.size() == 0)
            return;
        game = list.get(0);
        playerEntitiesList = MonopolyDatabase.getInstance(mainActivity).playerDao().getAllPlayersFromGame(game.getId());
        Game currGame = Game.getInstance();
        List<Player> playerList = new ArrayList<>();

        for(int i = 0; i < playerEntitiesList.size(); i++)
        {
            PlayerEntity currEntity = playerEntitiesList.get(i);
            Player tmpPlayer = new Player(currEntity.getName());
            tmpPlayer.setLost(currEntity.isLost());
            tmpPlayer.setCurrMoney(currEntity.getMoney());
            tmpPlayer.setNumRolled(currEntity.getNumRolled());
            tmpPlayer.setJailTime(currEntity.getJailTime());
            tmpPlayer.setPosition(currEntity.getPosition());
            tmpPlayer.setChanceJailFree(currEntity.getChanceJailFree());
            tmpPlayer.setChestJailFree(currEntity.getChestJailFree());
            if(!currEntity.getChestJailFree().equals(""))
                ChanceChestField.getChests().remove(currEntity.getChestJailFree());
            if(!currEntity.getChanceJailFree().equals(""))
                ChanceChestField.getChances().remove(currEntity.getChanceJailFree());
            playerList.add(tmpPlayer);
        }

        currGame.setNumOfHotelsLeft(game.getNumOfHotelsLeft());
        currGame.setNumOfHousesLeft(game.getNumOfHousesLeft());
        currGame.setAlreadyRolled(game.isRolled());
        currGame.setPlayers(playerList);
        currGame.setCurrPlayerNum(game.getCurrPlayerNum());
        currGame.setCurrPlayer(currGame.getPlayers().get(currGame.getCurrPlayerNum()));

        String[] ownersIdString = game.getBuyableFieldOwners().split(",");
        String[] housesString = game.getBuyableFieldHouses().split(",");
        String[] mortageString = game.getBuyableFieldMortage().split(",");

        List<Integer> ownerId = new ArrayList<>();
        List<Integer> houses = new ArrayList<>();
        List<Boolean> mortages = new ArrayList<>();

        for(int i = 0; i < ownersIdString.length; i++)
        {
            ownerId.add(Integer.parseInt(ownersIdString[i]));
            houses.add(Integer.parseInt(housesString[i]));
            mortages.add(Integer.parseInt(mortageString[i]) == 1);
        }

        for(int i = 0; i < currGame.getBuyableFields().size(); i++)
        {
            Player owner = ownerId.get(i) == -1 ? null : playerList.get(ownerId.get(i));
            BuyableField field = currGame.getBuyableFields().get(i);
            field.setOwner(owner);
            field.setHousesOwned(houses.get(i));
            field.setMortgage(mortages.get(i));
            if(owner != null)
                owner.addOwned(field);
        }
    }

    public void newAction(String s){
        ActionEntity actionEntity = new ActionEntity(0, game.getId(), s);
        MonopolyDatabase.getInstance(mainActivity).actionDao().insert(actionEntity);
    }

    public void setTime(long time){
        game.setGameLength(game.getGameLength() + time);
        MonopolyDatabase.getInstance(mainActivity).gameDao().update(game);
    }

    private String[] getStringData(Game currGame)
    {
        List<Player> playersList = currGame.getPlayers();
        StringBuilder buyablaFields = new StringBuilder();
        StringBuilder houses = new StringBuilder();
        StringBuilder mortage = new StringBuilder();
        for(int i = 0; i < currGame.getBuyableFields().size(); i++)
        {
            BuyableField f = currGame.getBuyableFields().get(i);
            buyablaFields.append(f.getOwner() != null ? playersList.indexOf(f.getOwner()) : -1);
            houses.append(f.getHousesOwned());
            mortage.append(f.isMortgage() ? 1 : 0);
            if(i != (currGame.getBuyableFields().size()-1))
            {
                buyablaFields.append(",");
                houses.append(",");
                mortage.append(",");
            }
        }
        return buyablaFields.append("/").append(houses).append("/").append(mortage).toString().split("/");
    }

    public void deleteAllFinishedGames(){
        List<GameEntity> gameEntities = MonopolyDatabase.getInstance(mainActivity).gameDao().getAllFinishedGames();
        MonopolyDatabase.getInstance(mainActivity).gameDao().deleteAllFinishedGames();
        for(GameEntity gameEntity:gameEntities)
        {
            MonopolyDatabase.getInstance(mainActivity).playerDao().deleteAllPlayersFromGame(gameEntity.getId());
            MonopolyDatabase.getInstance(mainActivity).actionDao().deleteAllActionsFromGame(gameEntity.getId());
        }
    }

    public PlayerEntity getWinner(long id){
        return MonopolyDatabase.getInstance(mainActivity).playerDao().getGameWinner(id);
    }

    public LiveData<List<GameEntity>> getAllFinishedGamesLive(){
        return MonopolyDatabase.getInstance(mainActivity).gameDao().getAllFinishedGamesLive();
    }

    public List<PlayerEntity> getALlPlayersFromGame(GameEntity game){
        return MonopolyDatabase.getInstance(mainActivity).playerDao().getAllPlayersFromGame(game.getId());
    }

    public List<ActionEntity> getAllActionsFromGame(GameEntity game){
        return MonopolyDatabase.getInstance(mainActivity).actionDao().getAllActionsFromGame(game.getId());
    }
}

package pl.edu.agh.kis.pz1.gameLogic;

import pl.edu.agh.kis.pz1.gameAssets.Deck;
import pl.edu.agh.kis.pz1.gameExceptions.NotEnoughMoney;
import pl.edu.agh.kis.pz1.gameExceptions.NumberOfPlayersOutOfBounds;
import pl.edu.agh.kis.pz1.gameExceptions.PlayerOfThisIdAlreadyExists;

import java.util.*;

/**
 * Class to hold and manage information about game
 */
public class Game {
    private String currentPlayerId;
    private String startingPlayerId;
    private Map<String, Player> players = new HashMap<>();
    private Deck deck = new Deck(true);
    private int ante;
    private int tableMoney = 0;


    /**
     * Creates a player with given id to game unless he already exists
     * @param playerId playerID to initialize new player with
     * @throws PlayerOfThisIdAlreadyExists thrown when player of given ID already exists
     */
    public void addPlayer(String playerId) throws PlayerOfThisIdAlreadyExists {
        if(players.get(playerId) == null){
            throw new PlayerOfThisIdAlreadyExists("Player with ID " + playerId + "already exists", playerId);
        }
        players.put(playerId, new Player(playerId));
    }

    /**
     * Removes a player from players list
     * @param playerId ID of a player which should be removed
     * @return true if player of given ID have existed in the list, otherwise false
     */
    public boolean removePlayer(String playerId){
        return players.remove(playerId) != null;
    }

    /**
     * Initializes game data
     * @param startingMoney money every player starts with
     * @param ante amount of money players are obligated to spend
     * @throws NumberOfPlayersOutOfBounds number of players is out of 2-4 bounds
     */
    public void initializeGame(int startingMoney, int ante) throws NumberOfPlayersOutOfBounds {
        if(players.size() < 2 || players.size() > 4){
            throw new NumberOfPlayersOutOfBounds("Invalid number of players: " + players.size(), players.size());
        }

        this.ante = ante;
        for (Player player: players.values()) {
            player.setPlayersMoney(startingMoney);
        }

        deck.shuffle();
        startingPlayerId = ((SortedSet<String>)players.keySet()).first();
        currentPlayerId = startingPlayerId;
    }

    /**
     * Deals 5 cards to each player
     */
    public void dealCards(){
        for (int i = 0; i < 5; i++) {
            for (Player player: players.values()) {
                player.addCard(deck.popFirst());
            }
        }
    }

    /**
     * Clears players' hands and resets and shuffles deck
     */
    void restartDeckAndPlayers(){
        for(Player player: players.values()){
            player.reset();
        }
        deck.resetDeck();
        deck.shuffle();
    }

    public void takeMoney(String PlayerId, int money) throws NotEnoughMoney {
        players.get(PlayerId).betMoney(money);
    }

    public Player getPlayer(String playerId){
        return players.get(playerId);
    }

    public List<String> getPlayersIds() {
        return new ArrayList<>(players.keySet());
    }
}

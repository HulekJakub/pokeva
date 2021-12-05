package pl.edu.agh.kis.pz1.game_logic;

import pl.edu.agh.kis.pz1.game_assets.Deck;
import pl.edu.agh.kis.pz1.game_exceptions.InvalidPlayerIdException;

import java.util.*;

/**
 * Class to hold and manage information about game
 */
public class Game {
    private Map<String, Player> players = new HashMap<>();
    private Deck deck = new Deck(true);
    private int tableMoney = 0;


    /**
     * Creates a player with given id to game unless he already exists
     * @param playerId playerID to initialize new player with
     * @throws InvalidPlayerIdException thrown when player of given ID already exists
     */
    public void addPlayer(String playerId) throws InvalidPlayerIdException {
        if(!playerId.matches("^\\s*\\w+\\s*$")){
            throw new InvalidPlayerIdException(playerId + " is not a valid name, please enter a name containing only [a-zA-Z_0-9]");
        }else if(players.get(playerId) != null){
            throw new InvalidPlayerIdException("Player with ID " + playerId + " already exists");
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
     */
    public void initializeGame(int startingMoney, int ante) {
        for (Player player: players.values()) {
            player.setPlayersMoney(startingMoney);
        }

        deck.shuffle();
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

    public String takeMoney(String playerId, int money){
        int toBet = Math.min(money, players.get(playerId).getPlayersMoney());
        players.get(playerId).betMoney(toBet);

        tableMoney += toBet;
        return toBet + " money has been bet.";
    }

    public void giveMoney(String playerId, int money){
        players.get(playerId).takeMoney(money);
        tableMoney -= money;
    }

    public int getTableMoney() {
        return tableMoney;
    }

    public Player getPlayer(String playerId){
        return players.get(playerId);
    }

    public List<String> getPlayersIds() {
        return new ArrayList<>(players.keySet());
    }

    public Deck getDeck() {
        return deck;
    }
}

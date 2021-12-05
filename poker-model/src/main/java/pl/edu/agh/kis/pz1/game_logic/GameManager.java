package pl.edu.agh.kis.pz1.game_logic;


import pl.edu.agh.kis.pz1.game_assets.Card;
import pl.edu.agh.kis.pz1.game_exceptions.InvalidPlayerIdException;

import java.util.*;

/**
 * Class to manage game logic and control game information
 * Defaults:
 * ante = 1
 * startingMoney = 100
 */


public class GameManager {

    public enum GamePhase{
            ANTE, BET1, EXCHANGE_PHASE, BET2, ROUND_CLOSURE;

            private static final GamePhase[] values = values();
            public GamePhase nextPhase(){
                return values[(this.ordinal() + 1) % values.length];
            }
        }

    private final Game game = new Game();
    private final String gameId;
    private int ante = 1;
    private int startingMoney = 100;

    private List<String> playersIds = new ArrayList<>();
    private int numberOfPlayers;

    private GamePhase gamePhase = GamePhase.ANTE;
    private String currentPlayerId;

    public void nextPhase(){
        if(gamePhase == GamePhase.BET1 || gamePhase == GamePhase.BET2){
            for(String playerId: playersIds){
                Player player = game.getPlayer(playerId);
                if(!player.isPassed() && !player.isAllIn() && player.getBetMoney() != maxBet()){
                    return;
                }
            }
        }
        gamePhase = gamePhase.nextPhase();
    }

    public void nextPlayer(){
        if (currentPlayerId.equals(playersIds.get(numberOfPlayers - 1))){
            gamePhase = gamePhase.nextPhase();
            currentPlayerId = playersIds.get(0);
        } else {
            currentPlayerId = playersIds.get(playersIds.indexOf(currentPlayerId) + 1);
        }
    }

    public String printMoneyAndHand(){
        StringBuilder moneyAndText = new StringBuilder();
        moneyAndText.append("Your money: " + game.getPlayer(currentPlayerId).getPlayersMoney() + "\n");
        moneyAndText.append("Money you have bet: " + game.getPlayer(currentPlayerId).getBetMoney() + "\n");
        moneyAndText.append("Money on table: " + game.getTableMoney());
        int cardNumber = 1;
        for(Card card:  game.getPlayer(currentPlayerId).getHand()){
            moneyAndText.append("\n"+ cardNumber + ": " + card.getSuit().toString() + " " + card.getRank().toString());
        }
        return moneyAndText.toString();
    }

    public String resolveAnte() {
        return game.takeMoney(currentPlayerId, ante);
    }

    public String resolveRoundClosure() {
        return "";
    }

    public String resolveExchange(GameMove gameMove) {
        StringBuilder sb = new StringBuilder();
        sb.append("Exchanged this cards: ");
        Set<Integer> indexes = new HashSet<>();
        for (int i = 0; i < gameMove.getParameter().length(); i++) {
            indexes.add(Integer.parseInt(String.valueOf(gameMove.getParameter().charAt(i))) - 1);
        }
        for (int i = 0; i < indexes.size(); i++) {
            sb.append("\n" + (i+1)+ ". " + game.getPlayer(currentPlayerId).getHand().get(i).toString());
            game.getPlayer(currentPlayerId).removeCard(i);
        }
        sb.append("\nFor these: ");
        for (int i = 0; i < indexes.size(); i++) {
            Card addCard = game.getDeck().popFirst();
            game.getPlayer(currentPlayerId).addCard(addCard);
            sb.append("\n" + addCard.toString());
        }
        return sb.toString();
    }

    public String resolveBet(GameMove gameMove) {
        int moneyToBet = 0;
        switch (gameMove.getGameMoveType()){
            case ALLIN:
                moneyToBet = game.getPlayer(currentPlayerId).getPlayersMoney();
                break;
            case PASS:
                return passCurrentPlayer();
            case CHECK:
                moneyToBet = minimalBet();
                break;
            case BET:
                moneyToBet = Integer.parseInt(gameMove.getParameter());
                if(moneyToBet < minimalBet()){
                    moneyToBet = minimalBet();
                }
                break;
        }
        return game.takeMoney(currentPlayerId, moneyToBet);
    }

    public int minimalBet() {
        return maxBet() - game.getPlayer(currentPlayerId).getBetMoney();
    }

    private int maxBet() {
        List<Integer> list = new ArrayList<>();
        for (String playerId: playersIds){
            list.add(game.getPlayer(playerId).getBetMoney());
        }
        return  Collections.max(list);
    }

    private Player winner() {
        int passed = 0;
        String notPassedPlayerId = null;
        for (String playerId: playersIds){
            Player player = game.getPlayer(playerId);
            if(player.getPlayersMoney() == startingMoney * playerId.length()){
                return player;
            }
            if(player.isPassed()){
                passed++;
            } else {
                notPassedPlayerId = playerId;
            }
        }
        if(passed == numberOfPlayers - 1){
            return game.getPlayer(notPassedPlayerId);
        }
        return null;
    }

    public String passCurrentPlayer(){
        return game.getPlayer(currentPlayerId).pass();
    }

    public GameManager(String gameId){
        this.gameId = gameId;
    }

    public boolean createPlayer(String playerId) throws InvalidPlayerIdException {
        game.addPlayer(playerId);
        return true;
    }

    public boolean removePlayer(String playerId){
        return game.removePlayer(playerId);
    }

    public void startGame(){
        game.initializeGame(startingMoney, ante);
        game.dealCards();
        playersIds = game.getPlayersIds();
        currentPlayerId = playersIds.get(0);
        numberOfPlayers = playersIds.size();
    }

    public Game getGame() {
        return game;
    }

    public List<String> getPlayersIds() {
        return playersIds;
    }

    public void setAnte(int ante) {
        this.ante = ante;
    }

    public int getAnte() {
        return ante;
    }

    public int getStartingMoney() {
        return startingMoney;
    }

    public void setStartingMoney(int startingMoney) {
        this.startingMoney = startingMoney;
    }

    public String getGameId() {
        return gameId;
    }

    public String getCurrentPlayerId() {
        return currentPlayerId;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }
}

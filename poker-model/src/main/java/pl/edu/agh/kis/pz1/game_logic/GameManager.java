package pl.edu.agh.kis.pz1.game_logic;


import pl.edu.agh.kis.pz1.game_assets.Card;
import pl.edu.agh.kis.pz1.game_exceptions.InvalidPlayerIdException;

import java.util.*;

/**
 * Class to manage game logic and control game information
 * Defaults:
 * ante = 2
 * startingMoney = 100
 */


public class GameManager {

    /**
     * Enum class to control game flow
     */
    public enum GamePhase{
            ANTE, BET1, EXCHANGE_PHASE, BET2, ROUND_CLOSURE;

            private static final GamePhase[] values = values();
            public GamePhase nextPhase(){
                return values[(this.ordinal() + 1) % values.length];
            }
        }

    private final Game game = new Game();
    private final String gameId;
    private int ante = 2;
    private int startingMoney = 100;

    private List<String> playersIds = new ArrayList<>();
    private int numberOfPlayers;

    private GamePhase gamePhase = GamePhase.ANTE;
    private String currentPlayerId;
    private String roundWinner;

    /**
     * Switches phase to next one if conditions are met
     */
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

    /**
     * Switches to next eligible player and changes phase if needed
     */
    public void nextPlayer(){
        if (currentPlayerId.equals(playersIds.get(numberOfPlayers - 1))){
            nextPhase();
            currentPlayerId = playersIds.get(0);
        } else {
            currentPlayerId = playersIds.get(playersIds.indexOf(currentPlayerId) + 1);
        }

        if(game.getPlayer(currentPlayerId).isPassed() || game.getPlayer(currentPlayerId).isEliminated()){
            nextPlayer();
        }
    }

    /**
     * Builds info message
     * @return info message
     */
    public String printMoneyHandStatus(){
        Player player = game.getPlayer(currentPlayerId);
        StringBuilder infoBuilder = new StringBuilder();
        infoBuilder.append("Your money: " + player.getPlayersMoney() + "\n");
        infoBuilder.append("Money you have bet: " + player.getBetMoney() + "\n");
        infoBuilder.append("Money on table: " + game.getTableMoney());
        int cardNumber = 1;
        for(Card card:  player.getHand()){
            infoBuilder.append("\n"+ cardNumber + ": " + card.getSuit().toString() + " " + card.getRank().toString());
            cardNumber++;
        }
        if(player.isPassed()){
            infoBuilder.append("\nYou have passed. Skipping turn.");
        } else if(player.isAllIn()){
            infoBuilder.append("\nYou are all-in. Skipping turn.");
        }

        return infoBuilder.toString();
    }

    /**
     * Proceeds with ante phase for current player
     * @return message to player
     */
    public String resolveAnte() {
        return game.takeMoney(currentPlayerId, ante);
    }

    /**
     * Checks if phase should be skipped
     * @return true if all players are either passed or allin
     */
    public boolean checkForRoundSkip(){
        for (String playerId: playersIds){
            if(!game.getPlayer(playerId).isAllIn()){
                return false;
            }
        }
        return true;
    }

    /**
     * Proceeds with round closure for all players
     * @return message to player to all players
     */
    public String resolveRoundClosure() {
        Map<String, Integer> playerWins = new HashMap<>();
        List<String> notPassedPlayerIds = new ArrayList<>();
        for (String playerId: playersIds){
            if(!game.getPlayer(playerId).isPassed() && !game.getPlayer(playerId).isEliminated()){
                notPassedPlayerIds.add(playerId);
            }
        }
        for(String playerId: notPassedPlayerIds){
            playerWins.putIfAbsent(playerId, 0);
            Player player = game.getPlayer(playerId);
            for (String playerId2: notPassedPlayerIds){
                Player player2 = game.getPlayer(playerId2);
                if(!playerId.equals(playerId2) && player.winsWith(player2)){
                        playerWins.put(playerId, playerWins.get(playerId) + 1);
                    }
                }
        }
        for(String playerId: notPassedPlayerIds){
            if(playerWins.get(playerId) == notPassedPlayerIds.size() - 1){
                roundWinner = playerId;
            }
        }
        int wonMoney = giveMoneyToWinner();
        game.restartDeckAndPlayers();
        for (String playerId: playersIds){
            if(game.getPlayer(playerId).getPlayersMoney() == 0){
                game.getPlayer(playerId).setEliminated(true);
            }
        }
        return "Player " + roundWinner + " has won and got " + wonMoney + " money.";
    }

    private int giveMoneyToWinner(){
        int res = game.getTableMoney();
        game.giveMoney(roundWinner, game.getTableMoney());
        return res;
    }

    public String resolveExchange(GameMove gameMove) {
        if(gameMove.getParameter().equals("0")){
            return "No cards exchanged";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Exchanged this cards: ");
        List<Integer> indexes = new ArrayList<>();
        for (int i = gameMove.getParameter().length() - 1; i >= 0 ; i--) {
            indexes.add(Integer.parseInt(String.valueOf(gameMove.getParameter().charAt(i))) - 1);
        }
        for (Integer i: indexes) {
            sb.append("\n" + (i+1)+ ". " + game.getPlayer(currentPlayerId).getHand().get(i).toString());
            game.getPlayer(currentPlayerId).removeCard(i /*- deleted*/);
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
            default:
                break;
        }
        return game.takeMoney(currentPlayerId, moneyToBet);
    }

    public boolean playerIsEliminated(){
        return game.getPlayer(currentPlayerId).isEliminated();

    }

    public boolean playerPassedOrAllIn(){
        return game.getPlayer(currentPlayerId).isAllIn() || game.getPlayer(currentPlayerId).isPassed();
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

    public Player winner() {
        int eliminated = 0;
        String notEliminatedPlayerId = null;
        for (String playerId: playersIds){
            Player player = game.getPlayer(playerId);
            if(player.getPlayersMoney() == startingMoney * playersIds.size()){
                return player;
            }
            if(player.isPassed()){
                eliminated++;
            } else {
                notEliminatedPlayerId = playerId;
            }
        }
        if(eliminated == numberOfPlayers - 1){
            return game.getPlayer(notEliminatedPlayerId);
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



    public void setAnte(int ante) {
        this.ante = ante;
    }

    public int getAnte() {
        return ante;
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

    public String getRoundWinner() {
        return roundWinner;
    }
}

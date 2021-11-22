package pl.edu.agh.kis.pz1.gameLogic;


import pl.edu.agh.kis.pz1.gameExceptions.BadMoveException;
import pl.edu.agh.kis.pz1.gameExceptions.NotEnoughMoneyException;
import pl.edu.agh.kis.pz1.gameExceptions.NumberOfPlayersOutOfBoundsException;
import pl.edu.agh.kis.pz1.gameExceptions.PlayerOfThisIdAlreadyExistsException;

import java.util.List;

/**
 * Class to manage game logic and control game information
 * Defaults:
 * ante = 5
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
    private int ante = 5;
    private int startingMoney = 100;

    private List<String> playersIds;
    private int numberOfPlayers;

    private GamePhase gamePhase = GamePhase.ANTE;
    private String currentPlayerId;

    public GameMove stringToGameMove(String s) throws BadMoveException {
        s = s.toUpperCase();
        if(!s.matches("^\\w+\\s+\\w+\\s+[A-Z]+\\s+\\d*")){
            throw new BadMoveException("Move doesn't follow \"PLAYERID GAMEID MOVE PARAMETER\"", true);
        }

    }

    public String resolveMove(GameMove gameMove){
        if
        switch (gamePhase){
            case ANTE: resolveAnte(gameMove);break;
            case BET1: case BET2: resolveBet(gameMove);break;
            case EXCHANGE_PHASE: resolveExchange(gameMove);break;
            case ROUND_CLOSURE: resolveRoundClosure(gameMove);break;
        }

        if (currentPlayerId.equals(playersIds.get(numberOfPlayers - 1))){
            gamePhase = gamePhase.nextPhase();
            currentPlayerId = playersIds.get(0);
        }
    }

    private void mainLoop(){
        Player winner = null;
        while(winner == null){
            winner = winner();

            gamePhase = gamePhase.nextPhase();
        }
    }

    private void resolveRoundClosure(GameMove gameMove) {
    }

    private void resolveExchange(GameMove gameMove) {
    }

    private void resolveBet(GameMove gameMove) {
    }

    private void resolveAnte(GameMove gameMove) {
        for (String playerId: playersIds){
            try{
                game.getPlayer(.betMoney(ante));
            } catch (NotEnoughMoneyException notEnoughMoney){

            }
        }
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


    public GameManager(String gameId){
        this.gameId = gameId;
    }

    public boolean createPlayer(String playerId) throws PlayerOfThisIdAlreadyExistsException {
        game.addPlayer(playerId);
        return true;
    }

    public boolean removePlayer(String playerId){
        return game.removePlayer(playerId);
    }

    public void startGame() throws NumberOfPlayersOutOfBoundsException {
        game.initializeGame(startingMoney, ante);
        playersIds = game.getPlayersIds();
        currentPlayerId = playersIds.get(0);
        numberOfPlayers = playersIds.size();
    }

    public void setAnte(int ante) {
        this.ante = ante;
    }

    public void setStartingMoney(int startingMoney) {
        this.startingMoney = startingMoney;
    }

    public String getGameId() {
        return gameId;
    }
}

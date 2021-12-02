package pl.edu.agh.kis.pz1.game_logic;

import pl.edu.agh.kis.pz1.communication.GameToken;
import pl.edu.agh.kis.pz1.game_exceptions.BadMoveException;

import java.util.ArrayList;
import java.util.List;

public class GameMove {
    public enum GameMoveType {
        EXCHANGE, PASS, BET, CHECK, ALLIN;
    }

    private final Game game;
    private final String playerId;
    private GameMoveType gameMoveType;
    private String parameter;


    public GameMove(GameToken token, Game game) throws BadMoveException {
        this.gameMoveType = GameMoveType.valueOf(token.getGameMoveType());
        this.playerId = token.getPlayerId();
        this.game = game;
        setParameter(token.getParameter());
    }

    private void setParameter(String parameter) throws BadMoveException {
        if (gameMoveType == GameMoveType.EXCHANGE && !isExchangeParameterValid(parameter)) {
            throw new BadMoveException("Your parameter for EXCHANGE move is improper.\n" +
                    "It should be a string with maximal length of 5 and contain only digits from set {1, 2, 3, 4, 5}\n" +
                    "where empty string means no exchange and digits mean which card to change.", true);
        }
        if(gameMoveType == GameMoveType.BET && !isBetParameterValid(parameter)){
            throw new BadMoveException("BET move's parameter should be a positive number.\n" +
                    "If it's greater than your money, move will be changed to ALLIN.", true);
        }
        if(parameter.length() != 0 && (gameMoveType == GameMoveType.PASS ||
                gameMoveType == GameMoveType.CHECK ||
                gameMoveType == GameMoveType.ALLIN)){
            this.parameter = parameter;
            throw new BadMoveException("You don't need to add a parameter to PASS, CHECK and ALLIN moves.", false);
        }
        this.parameter = parameter;
    }

    private boolean isBetParameterValid(String parameter){
        try {
            if(Integer.parseInt(parameter, 10) <= 0){
                return false;
            }
        }
        catch (Exception e){
            return false;
        }
        if(game.getPlayer(playerId).getPlayersMoney() <= Integer.parseInt(parameter, 10)){
            gameMoveType = GameMoveType.ALLIN;
        }

        return true;
    }

    private boolean isExchangeParameterValid(String parameter){
        final String allowedCharacters = "12345";

        if(parameter.length() > 5){
            return false;
        }
        for (int i = 0; i < parameter.length(); i++) {
            if(!allowedCharacters.contains("" + parameter.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public GameMoveType getGameMoveType() {
        return gameMoveType;
    }

    public String getParameter() {
        return parameter;
    }

    public List<Integer> getCardsToExchange() {
        List<Integer> cardsIndexes = new ArrayList<>();

        if (gameMoveType != GameMoveType.EXCHANGE) {
            return cardsIndexes;
        }

        for (int i = 1; i <= 5; i++) {
            if (parameter.contains(String.valueOf(i))) {
                cardsIndexes.add(i);
            }
        }

        return cardsIndexes;
    }
}

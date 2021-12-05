package pl.edu.agh.kis.pz1.communication;

import pl.edu.agh.kis.pz1.communication.exceptions.BadGameTokenException;

import java.util.Locale;

public class GameToken {
    private final String gameId;
    private final String playerId;
    private final String gameMoveType;
    private final String parameter;

    public GameToken(String token) throws BadGameTokenException {
        String[] arguments = token.split(" +", 4);
        if(!token.matches("^\\w+\\s+\\w+\\s+[A-Z]+\\s+\\d*")){
            throw new BadGameTokenException("Move doesn't follow \"MOVE PARAMETER\" pattern.", true);
        }

        gameId = arguments[0];
        playerId = arguments[1];
        gameMoveType = arguments[2].toUpperCase();
        parameter = arguments[3].toUpperCase();
    }

    public GameToken(String gameId, String playerId, String gameMoveType, String parameter) throws BadGameTokenException {
        this.gameId = gameId;
        this.playerId = playerId;
        this.gameMoveType = gameMoveType.toUpperCase();
        this.parameter = parameter.toUpperCase();

        if(!toToken().matches("^\\w+\\s+\\w+\\s+[A-Z]+\\s+\\d*")){
            throw new BadGameTokenException("Move doesn't follow \"MOVE PARAMETER\" pattern.", true);
        }
    }


    public String toToken() {
        return gameId + " " + playerId + " " + gameMoveType + " " + parameter;
    }

    public String getGameId() {
        return gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getGameMoveType() {
        return gameMoveType;
    }

    public String getParameter() {
        return parameter;
    }
}

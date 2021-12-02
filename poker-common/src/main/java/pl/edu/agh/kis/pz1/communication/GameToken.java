package pl.edu.agh.kis.pz1.communication;

import pl.edu.agh.kis.pz1.communication.exceptions.BadGameTokenException;

public class GameToken {
    private final String gameId;
    private final String playerId;
    private final String gameMoveType;
    private final String parameter;

    public GameToken(String token) throws BadGameTokenException {
        token = token.toUpperCase();
        String[] arguments = token.split(" ");

        if(!token.matches("^\\w+\\s+\\w+\\s+[A-Z]+\\s+\\d*")){
            throw new BadGameTokenException("Move doesn't follow \"MOVE PARAMETER\" pattern.", true);
        }
        gameId = arguments[0];
        playerId = arguments[1];
        gameMoveType = arguments[2];
        parameter = arguments[3];
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

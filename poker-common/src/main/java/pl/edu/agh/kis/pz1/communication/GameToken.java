package pl.edu.agh.kis.pz1.communication;

import pl.edu.agh.kis.pz1.communication.exceptions.BadGameTokenException;

/**
 * Class to check if player input is in good format
 */
public class GameToken {
    private final String playerId;
    private final String gameMoveType;
    private final String parameter;

    /**
     * Initializes GameToken class object from given string
     * @param token string to convert to GameToken object
     * @throws BadGameTokenException thrown if token is not valid
     */
    public GameToken(String token) throws BadGameTokenException {
        String[] arguments = token.split(" +", 4);
        if(!token.matches("^\\w+\\s+\\w+\\s+[A-Za-z]+\\s*\\d*")){
            throw new BadGameTokenException("Move doesn't follow \"MOVE PARAMETER\" pattern.", true);
        }

        playerId = arguments[1];
        gameMoveType = arguments[2].toUpperCase();
        if (arguments.length < 4) {
            parameter = "";
        } else {
            parameter = arguments[3].toUpperCase();
        }

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

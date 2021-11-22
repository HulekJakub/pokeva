package pl.edu.agh.kis.pz1.gameExceptions;

public class PlayerOfThisIdAlreadyExistsException extends Exception{
    private final String playerId;

    public PlayerOfThisIdAlreadyExistsException(String s, String playerId){
        super(s);
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }
}

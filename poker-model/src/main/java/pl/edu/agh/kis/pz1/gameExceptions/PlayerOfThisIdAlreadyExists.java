package pl.edu.agh.kis.pz1.gameExceptions;

public class PlayerOfThisIdAlreadyExists extends Exception{
    private final String playerId;

    public PlayerOfThisIdAlreadyExists(String s, String playerId){
        super(s);
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }
}

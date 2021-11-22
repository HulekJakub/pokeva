package pl.edu.agh.kis.pz1.gameExceptions;

public class NumberOfPlayersOutOfBoundsException extends Exception{
    private final int numberOfPlayers;

    public NumberOfPlayersOutOfBoundsException(String s, int numberOfPlayers){
        super(s);
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
}

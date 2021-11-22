package pl.edu.agh.kis.pz1.gameExceptions;

public class NumberOfPlayersOutOfBounds extends Exception{
    private final int numberOfPlayers;

    public NumberOfPlayersOutOfBounds(String s, int numberOfPlayers){
        super(s);
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
}

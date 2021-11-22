package pl.edu.agh.kis.pz1.gameExceptions;

public class BadMoveException extends Exception{
    private final boolean askForMoveAgain;
    public BadMoveException(String s, boolean askForMoveAgain){
        super(s);
        this.askForMoveAgain = askForMoveAgain;
    }
}

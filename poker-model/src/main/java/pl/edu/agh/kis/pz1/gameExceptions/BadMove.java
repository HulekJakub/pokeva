package pl.edu.agh.kis.pz1.gameExceptions;

public class BadMove extends Exception{
    private final boolean askForMoveAgain;
    public BadMove(String s, boolean askForMoveAgain){
        super(s);
        this.askForMoveAgain = askForMoveAgain;
    }
}

package pl.edu.agh.kis.pz1.communication.exceptions;

public class BadGameTokenException extends Exception{
    protected final boolean askForMoveAgain;
    public BadGameTokenException(String s, boolean askForMoveAgain){
        super(s);
        this.askForMoveAgain = askForMoveAgain;
    }

    public boolean isAskForMoveAgain() {
        return askForMoveAgain;
    }
}

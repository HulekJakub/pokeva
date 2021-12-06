package pl.edu.agh.kis.pz1.communication.exceptions;

/**
 * Exception that indicates bad player input format
 */
public class BadGameTokenException extends Exception{
    private final boolean askForMoveAgain;
    public BadGameTokenException(String s, boolean askForMoveAgain){
        super(s);
        this.askForMoveAgain = askForMoveAgain;
    }

    public boolean isAskingForMoveAgain() {
        return askForMoveAgain;
    }
}

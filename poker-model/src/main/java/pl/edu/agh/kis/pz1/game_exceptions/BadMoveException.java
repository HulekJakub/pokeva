package pl.edu.agh.kis.pz1.game_exceptions;

/**
 * Exception that indicates bad player input
 */
public class BadMoveException extends  Exception{
    private final boolean askForMoveAgain;
    public BadMoveException(String s, boolean askForMoveAgain){
        super(s);
        this.askForMoveAgain = askForMoveAgain;
    }

    public boolean isAskingForMoveAgain() {
        return askForMoveAgain;
    }
}

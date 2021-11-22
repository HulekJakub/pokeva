package pl.edu.agh.kis.pz1.game_exceptions;

public class BadMoveException extends Exception{
    private final boolean askForMoveAgain;
    public BadMoveException(String s, boolean askForMoveAgain){
        super(s);
        this.askForMoveAgain = askForMoveAgain;
    }

    public boolean isAskForMoveAgain() {
        return askForMoveAgain;
    }
}

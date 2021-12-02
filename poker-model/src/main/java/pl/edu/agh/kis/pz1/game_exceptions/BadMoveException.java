package pl.edu.agh.kis.pz1.game_exceptions;

import pl.edu.agh.kis.pz1.communication.exceptions.BadGameTokenException;

public class BadMoveException extends BadGameTokenException {
    public BadMoveException(String s, boolean askForMoveAgain){
        super(s, askForMoveAgain);
    }
}

package pl.edu.agh.kis.pz1.game_exceptions;

/**
 * Exceptions class that indicates that given player name isn't valid or is already taken
 */
public class InvalidPlayerIdException extends Exception{
    public InvalidPlayerIdException(String s){
        super(s);
    }
}

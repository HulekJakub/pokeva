package pl.edu.agh.kis.pz1.communication;

/**
 * Data class with constants needed for comunication
 */
public class CommunicationConstants {
    private CommunicationConstants(){}
    public static final String LOGIN = "LOGIN";
    public static final String INFO = "INFO";
    public static final String GAME_MESSAGE = "GAME_MESSAGE";
    public static final String MESSAGE_PARTS_DELIMITER = "$###$";
    public static final String MESSAGE_PARTS_DELIMITER_REGEXP = "\\$###\\$";
}

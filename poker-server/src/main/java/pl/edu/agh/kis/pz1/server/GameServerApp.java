package pl.edu.agh.kis.pz1.server;

import pl.edu.agh.kis.pz1.communication.TerminalPrinter;

import java.util.Scanner;

/**
 * Class with main function, operates with GameServer class
 */
public class GameServerApp {
    private static final TerminalPrinter terminalPrinter = new TerminalPrinter();
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean restartGame = true;
    private static GameServer gameServer;

    public static void main(String[] args) {
        String argument = args.length > 0 ? args[0] : "";
        try {
            gameServer = new GameServer(9090);
        }
        catch (Exception e){
            terminalPrinter.printError(e);
        }
        while (restartGame){
            game(argument);
        }
    }

    private static void game(String num){
        try {

            int numberOfPlayers = getNumberOfPlayers(num);
            if(!restartGame){
                return;
            }
            gameServer.startGameServer(numberOfPlayers, "testowa");
        }
        catch (Exception e){
            terminalPrinter.printError("Error, closing game because " + e.getCause(), e);
        }
    }

    private static int getNumberOfPlayers(String num){
        terminalPrinter.print("Enter number of players (0 to close): ");
        boolean badInput = true;
        int numberOfPlayers = 0;
        while (badInput) {

            badInput = false;

            if(num.equals("")){
                numberOfPlayers = scanner.nextInt();
            } else {
                numberOfPlayers = Integer.parseInt(num);
            }

            if(numberOfPlayers == 0){
                restartGame = false;
                return 0;
            }
            if(numberOfPlayers < 2 || numberOfPlayers > 4){
                badInput = true;
                terminalPrinter.print("Game is made for 2-4 players.");
            }

        }
        return numberOfPlayers;
    }

}



package pl.edu.agh.kis.pz1.server;

import pl.edu.agh.kis.pz1.communication.TerminalPrinter;

import java.util.Scanner;

public class GameServerApp {
    private static final TerminalPrinter terminalPrinter = new TerminalPrinter();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            GameServer gameServer = new GameServer(9090);
            gameServer.startGameServer(getNumberOfPlayers(), "testowa");
        }
        catch (Exception e){
           terminalPrinter.printError("Error, closing because " + e.getCause(), e);
        }
    }

    private static int getNumberOfPlayers(){
        terminalPrinter.print("Enter number of players: ");
        while (true) {
            try {
                int numberOfPlayers = scanner.nextInt();
                if(numberOfPlayers < 2 || numberOfPlayers > 4){
                    terminalPrinter.print("Game is made for 2-4 players.");
                    throw new Exception();
                }
                return numberOfPlayers;
            } catch (Exception e){
                terminalPrinter.print("Enter number of players: ");
            }
        }
    }

}



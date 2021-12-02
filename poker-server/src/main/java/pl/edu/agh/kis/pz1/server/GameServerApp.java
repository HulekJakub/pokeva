package pl.edu.agh.kis.pz1.server;

import pl.edu.agh.kis.pz1.communication.TerminalPrinter;

public class GameServerApp {
    private static final TerminalPrinter terminalPrinter = new TerminalPrinter();

    public static void main(String[] args) {
        try {
            GameServer gameServer = new GameServer(9090);
            //gameServer.mainLoop();
        } catch (Exception e){
           terminalPrinter.printError("Error, closing.", e);
        }
    }

}



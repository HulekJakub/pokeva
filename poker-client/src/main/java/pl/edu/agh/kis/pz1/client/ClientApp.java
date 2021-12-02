package pl.edu.agh.kis.pz1.client;

import pl.edu.agh.kis.pz1.communication.TerminalPrinter;

public class ClientApp {
    private static final TerminalPrinter terminalPrinter = new TerminalPrinter();

    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 9090);
            client.mainLoop();
        } catch (Exception e){
            terminalPrinter.printError("Error, closing.", e);
        }
    }



}
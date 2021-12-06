package pl.edu.agh.kis.pz1.client;

import pl.edu.agh.kis.pz1.communication.TerminalPrinter;

/**
 * Class with main function, which runs client terminal application
 */
public class ClientApp {
    private static final TerminalPrinter terminalPrinter = new TerminalPrinter();

    public static void main(String[] args) {
        boolean notConnected = true;
        while (notConnected) {
            try {
                Client client = new Client();
                client.connectTo("localhost", 9090);
                notConnected = false;
                client.mainLoop();
            } catch (Exception e) {
                terminalPrinter.printError( e);
            }

        }
    }



}
package pl.edu.agh.kis.pz1.client;

import pl.edu.agh.kis.pz1.communication.TerminalPrinter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static pl.edu.agh.kis.pz1.communication.CommunicationConstants.*;

/**
 * Class that manages communication with game server
 */
public class Client {
    private static final TerminalPrinter terminalPrinter = new TerminalPrinter();
    private Scanner scanner = new Scanner(System.in);
    private SocketChannel channel;
    private ByteBuffer buffer;
    private String clientId;
    private String gameId;
    private boolean continuePlaying = true;

    /**
     * Intializes Client class object and allocates 2048 Bytes of buffer memory
     */
    public Client() {
        buffer = ByteBuffer.allocate(2048);
        buffer.clear();
    }

    /**
     * Connects to server
     * @param hostname hostname
     * @param port port
     * @throws IOException IOException
     */
    public void connectTo(String hostname, int port) throws IOException{
        channel = SocketChannel.open(new InetSocketAddress(hostname, port));
    }

    /**
     * Main loop that manages communication with server
     * @throws IOException IOException
     */
    public void mainLoop() throws IOException {
        String toSend;
        while (continuePlaying){
            if(channel.read(buffer) >= 1){
                String message = getMessageFromBuffer();
                String[] messageParts = message.split(MESSAGE_PARTS_DELIMITER_REGEXP, 3);

                switch (messageParts[0]){
                    case LOGIN:
                        gameId = messageParts[1];
                        terminalPrinter.print(messageParts[2]);
                        toSend = getInput();
                        clientId = toSend;
                        sendMessage(toSend);
                        break;
                    case INFO:
                        terminalPrinter.print(messageParts[2] + " " + messageParts[1]);
                        sendConfirmation();
                        break;
                    case GAME_MESSAGE:
                        terminalPrinter.print("Commands: EXCHANGE BET ALLIN CHECK PASS");
                        terminalPrinter.print(messageParts[2] + " " + messageParts[1]);
                        toSend = getInput();
                        sendMessage(toSend);
                        break;
                    default:
                        terminalPrinter.print("Got \"" + message + "\"\n And something went wrong.");
                        sendConfirmation();
                        break;
                }
            }
        }
        endConnection();
    }

    private void sendMessage(String message) throws IOException {
        buffer.clear();
        buffer.put((gameId + " " + clientId + " " + message).getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        channel.write(buffer);
        buffer.clear();
    }

    private void sendConfirmation() throws IOException {
        buffer.clear();
        buffer.put((gameId + " " + clientId + " " + "CONFIRM").getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        channel.write(buffer);
        buffer.clear();
    }

    private String getInput(){
        String input = scanner.nextLine();
        if(input.matches("(?i)^exit.*")){
            continuePlaying = false;
        }
        return input;
    }

    private String getMessageFromBuffer(){
        buffer.flip();
        StringBuilder sb = new StringBuilder();
        for(int j = 0; j < buffer.limit();j++)
        {
            sb.append((char) buffer.get(j));
        }
        buffer.clear();
        return sb.toString();
    }

    private void endConnection() throws IOException{
        channel.close();
    }
}

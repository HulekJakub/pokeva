package pl.edu.agh.kis.pz1.server;

import pl.edu.agh.kis.pz1.communication.GameToken;
import pl.edu.agh.kis.pz1.communication.TerminalPrinter;
import pl.edu.agh.kis.pz1.communication.exceptions.BadGameTokenException;
import pl.edu.agh.kis.pz1.game_exceptions.BadMoveException;
import pl.edu.agh.kis.pz1.game_exceptions.InvalidPlayerIdException;
import pl.edu.agh.kis.pz1.game_logic.GameManager;
import pl.edu.agh.kis.pz1.game_logic.GameMove;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GameServer {
    private Map<SocketChannel, ByteBuffer> channelBuffers = new HashMap<>();
    private Map<SocketChannel, String> clientIds = new HashMap<>();
    private ServerSocketChannel channel;
    private Selector selector;
    private TerminalPrinter terminalPrinter = new TerminalPrinter();
    private GameManager gameManager;
    private boolean gameNotEnded = true;

    public GameServer(int port) throws IOException{
        selector = Selector.open();
        channel = ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress(port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void startGameServer(int numberOfPlayers, String gameId) throws IOException {
        gameManager = new GameManager(gameId);
        connectWithPlayers(numberOfPlayers);
        terminalPrinter.print("D00pa");
        gameManager.startGame();
        roundLoop();
    }

    public void connectWithPlayers(int numberOfPlayers) throws IOException {
        terminalPrinter.print("Waiting for players to log...");
        Map<SocketChannel, Boolean> isLogged = new HashMap<>();
        int currentNumberOfPlayers = 0;
        while (currentNumberOfPlayers < numberOfPlayers){
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();

            for(Iterator<SelectionKey> it = keys.iterator(); it.hasNext();) {

                    SelectionKey key = it.next();
                    it.remove();
                try{
                    if (key.isValid()) {
                        if(key.isAcceptable()) {
                            connectToClient(key);
                        }
                        else if (key.isReadable()) {
                            SocketChannel playerChannel = (SocketChannel) key.channel();
                            String playerId = readFromChannel(key, playerChannel).split(gameManager.getGameId() + " +")[1].split(" ")[0];
                            if(playerId !=null){
                                try{
                                    gameManager.createPlayer(playerId);
                                    currentNumberOfPlayers++;
                                    clientIds.put(playerChannel, playerId);
                                    isLogged.put(playerChannel, true);
                                    String message = generateMessage("INFO", "", "You are now logged as " + playerId);
                                    writeToChannel(key, playerChannel, message, false);
                                } catch (InvalidPlayerIdException invalidPlayerIdException){
                                    String message = generateMessage("RETRY_LOGIN", "",invalidPlayerIdException.getMessage());
                                    writeToChannel(key, playerChannel, message, true);
                                }
                            }
                        }
                        else if(key.isWritable() && isLogged.get((SocketChannel) key.channel()) == null){
                            SocketChannel playerChannel = (SocketChannel) key.channel();
                            String message = generateMessage("LOGIN", gameManager.getGameId(), "Enter your nickname: ");
                            writeToChannel(key, playerChannel, message, true);
                        }
                    }
                } catch (IOException ioException){
                    SocketChannel errorChannel = (SocketChannel) key.channel();
                    terminalPrinter.print("Closing connection with " + clientIds.get(errorChannel));
                    closeConnection(errorChannel);
                }
            }
        }
    }

    private void roundLoop() throws IOException {
        String gameLog, message;
        while(gameNotEnded) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();

            for(Iterator<SelectionKey> it = keys.iterator(); it.hasNext();){
                SelectionKey key = it.next();
                it.remove();

                terminalPrinter.print("DUPA");
                if(key.isValid() && key.isWritable()) {
                    SocketChannel playerChannel = (SocketChannel) key.channel();
                    terminalPrinter.print("DUPA1");
                    if(clientIds.get(playerChannel).equals(gameManager.getCurrentPlayerId())){
                        terminalPrinter.print("DUPA12");
                        writeToChannel(key, playerChannel, generateMessage("INFO","",gameManager.printMoneyAndHand()), false);
                        terminalPrinter.print("DUPA123");
                        switch (gameManager.getGamePhase()){
                            case ANTE:
                                terminalPrinter.print("DUPA3");
                                gameLog = gameManager.resolveAnte();
                                message = generateMessage("INFO", gameManager.getAnte() + "", gameLog);
                                writeToChannel(key, playerChannel, message, false);
                                break;
                            case BET1:
                            case BET2:
                                String betAsk = "Bet money so it at least equals maximal bet of other player, you can also bet 0 if it is allowed.";
                                message = generateMessage("GAME_MESSAGE", gameManager.minimalBet() + "", betAsk);
                                writeToChannel(key, playerChannel, message, true);
                                while(true){
                                    try{
                                        String clientMessage = readFromChannel(key, playerChannel);
                                        while(clientMessage == null){clientMessage = readFromChannel(key, playerChannel);}
                                        GameToken gameToken = new GameToken(clientMessage);
                                        GameMove gameMove = new GameMove(gameToken, gameManager.getGame());
                                        if(gameMove.getGameMoveType() == GameMove.GameMoveType.EXCHANGE){
                                            throw new BadMoveException("You can't exchange cards now.", true);
                                        }
                                        message = generateMessage("INFO", "", gameManager.resolveBet(gameMove));
                                        writeToChannel(key, playerChannel, message, false);
                                        break;
                                    } catch (BadGameTokenException badGameTokenException){
                                        message = generateMessage("GAME_MESSAGE", gameManager.minimalBet() + "",
                                                badGameTokenException.getMessage() + "\n" + betAsk);
                                        writeToChannel(key, playerChannel, message, badGameTokenException.isAskingForMoveAgain());
                                    } catch (BadMoveException badMoveException) {
                                        message = generateMessage("GAME_MESSAGE", gameManager.minimalBet() + "",
                                                badMoveException.getMessage() + "\n" + betAsk);
                                        writeToChannel(key, playerChannel, message, badMoveException.isAskingForMoveAgain());
                                    }
                                }
                                break;
                            case EXCHANGE_PHASE:
                                String exchangeAsk = "Choose cards to exchange, pass corresponding digits or non if you are happy with your hand.";
                                message = generateMessage("GAME_MESSAGE", "", exchangeAsk);
                                writeToChannel(key, playerChannel, message, true);
                                while (true){
                                    try{
                                        String clientMessage = readFromChannel(key, playerChannel);
                                        while(clientMessage == null){clientMessage = readFromChannel(key, playerChannel);}
                                        GameToken gameToken = new GameToken(clientMessage);
                                        GameMove gameMove = new GameMove(gameToken, gameManager.getGame());
                                        if(gameMove.getGameMoveType() != GameMove.GameMoveType.EXCHANGE){
                                            throw new BadMoveException("You can only exchange cards now.", true);
                                        }
                                        message = generateMessage("INFO", "", gameManager.resolveExchange(gameMove));
                                        writeToChannel(key, playerChannel, message, false);
                                        break;
                                    } catch (BadGameTokenException badGameTokenException){
                                        message = generateMessage("GAME_MESSAGE", "",
                                                badGameTokenException.getMessage() + "\n" + exchangeAsk);
                                        writeToChannel(key, playerChannel, message, badGameTokenException.isAskingForMoveAgain());
                                    } catch (BadMoveException badMoveException) {
                                        message = generateMessage("GAME_MESSAGE", "",
                                                badMoveException.getMessage() + "\n" + exchangeAsk);
                                        writeToChannel(key, playerChannel, message, badMoveException.isAskingForMoveAgain());
                                    }
                                }
                                break;
                            case ROUND_CLOSURE:
                                gameLog = gameManager.resolveRoundClosure();
                                message = generateMessage("INFO", "", gameLog);
                                writeToChannel(key, playerChannel, message, false);
                                break;
                        }
                        gameManager.nextPlayer();
                    }
                }
            }
        }
    }
    private String readFromChannel(SelectionKey key, SocketChannel playerChannel) throws IOException {
        String read = null;
        ByteBuffer buffer = channelBuffers.get(playerChannel);
        buffer.clear();
        while (true) {
            if (playerChannel.read(buffer) > 0) {
                read = getMessageFromBuffer(buffer);
                key.interestOps(SelectionKey.OP_WRITE);
                terminalPrinter.print("Recieved message from \"" + clientIds.get(playerChannel) + "\": " + read);
                return read;
            }
        }
    }

    private void writeToChannel(SelectionKey key, SocketChannel playerChannel, String message, boolean waitForResponse) throws IOException {
        ByteBuffer buffer = channelBuffers.get(playerChannel);

        buffer.clear();
        buffer.put(message.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        playerChannel.write(buffer);

        if(waitForResponse){
            key.interestOps(SelectionKey.OP_READ);
        } else{
            key.interestOps(SelectionKey.OP_READ);
            getConfirmation(playerChannel);
            key.interestOps(SelectionKey.OP_WRITE);
        }
        terminalPrinter.print("Sended \"" + message +"\" to " + clientIds.get(playerChannel));
    }

    private void getConfirmation(SocketChannel playerChannel) throws IOException {

        ByteBuffer buffer = channelBuffers.get(playerChannel);
        buffer.clear();
        while (true) {
            if (playerChannel.read(buffer) > 0) {
               return;
            }
        }
    }

    private void connectToClient(SelectionKey key) throws IOException {
        SocketChannel sc = channel.accept();
        if(sc != null){
            sc.configureBlocking(false);
            sc.register(selector,SelectionKey.OP_WRITE);
            channelBuffers.put(sc,ByteBuffer.allocate(2048));
            clientIds.put(sc, clientIds.size() + 1 + ". client");
        }
    }

    private String getMessageFromBuffer(ByteBuffer buffer){
        buffer.flip();
        StringBuilder sb = new StringBuilder();
        for(int j = 0; j < buffer.limit();j++)
        {
            sb.append((char) buffer.get(j));
        }
        buffer.clear();
        return sb.toString();
    }

    private String generateMessage(String type, String parameter, String toPrint){
        return type + "$###$" + parameter + "$###$" + toPrint;
    }

    private void closeConnection(SocketChannel playerChannel) throws IOException {
        channelBuffers.remove(playerChannel);
        clientIds.remove(playerChannel);
        playerChannel.close();
    }
}

package pl.edu.agh.kis.pz1.server;

import pl.edu.agh.kis.pz1.communication.TerminalPrinter;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GameServer {
    Map<SocketChannel, ByteBuffer> channelBuffers = new HashMap<>();
    ServerSocketChannel channel;
    Selector selector;
    TerminalPrinter terminalPrinter = new TerminalPrinter();

    public GameServer(int port) throws IOException{
        selector = Selector.open();
        channel = ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress(port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

//    public void mainLoop() throws IOException, ClosedChannelException {
//        while(true) {
//            selector.select();
//            Set<SelectionKey> keys = selector.selectedKeys();
//
//            for(Iterator<SelectionKey> it = keys.iterator(); it.hasNext();){
//                SelectionKey key = it.next();
//                it.remove();
//                if(key.isValid()) {
//                    if(key.isAcceptable())
//                    {
//                        SocketChannel sc = channel.accept();
//                        if(sc != null){
//                            sc.configureBlocking(false);
//                            sc.register(selector,SelectionKey.OP_READ);
//                        }
//                        channelBuffers.put(sc,ByteBuffer.allocate(1024));
//                    }
//                    else if(key.isReadable())
//                    {
//                        SocketChannel sc = (SocketChannel) key.channel();
//                        ByteBuffer buffer = channelBuffers.get(sc);
//
//                        int read = sc.read(buffer);
//                        if(read == -1)
//                        {
//                            channelBuffers.remove(sc);
//                            sc.close();
//                        }
//                        else if (read > 0)
//                        {
//                            buffer.flip();
//                            int limit = buffer.limit();
//                            for(int i = 0; i < limit/2; i++)
//                            {
//                                byte temp = buffer.get(i);
//                                buffer.put(i, buffer.get(limit-i-1));
//                                buffer.put(limit-i-1, temp);
//                            }
//                            key.interestOps(SelectionKey.OP_WRITE);
//                        }
//
//
//                    }
//                    else if(key.isWritable())
//                    {
//                        SocketChannel sc = (SocketChannel) key.channel();
//                        ByteBuffer buffer = channelBuffers.get(sc);
//
//                        int write = sc.write(buffer);
//                        if(write == -1)
//                        {
//                            channelBuffers.remove(sc);
//                            sc.close();
//                        }
//                        else if (write > 0)
//                        {
//                            buffer.clear();
//                            key.interestOps(SelectionKey.OP_READ);
//                        }
//                    }
//
//                }
//            }
//
//        }
//    }
}

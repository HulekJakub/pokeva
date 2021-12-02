package pl.edu.agh.kis.pz1.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    private SocketChannel channel;
    ByteBuffer buffer;

    public Client(String hostname, int port) throws IOException {
        channel = SocketChannel.open(new InetSocketAddress(hostname, port));
        buffer = ByteBuffer.allocate(1024);
    }

    public void mainLoop() throws IOException{
        while (true){
            buffer.put(words[i].getBytes());
            buffer.flip();


            System.out.println("Sending " + words[i] + "...");
            channel.write(buffer);

            buffer.clear();
            channel.read(buffer);
            buffer.flip();
            StringBuilder builder = new StringBuilder();
            for(int j = 0; j < buffer.limit();j++)
            {
                builder.append((char) buffer.get(j));
            }
            System.out.println("Response is: " + builder);
            buffer.clear();

            Thread.sleep(2000);
        }
        endConnection();
    }

    private void endConnection() throws IOException{
        channel.close();
    }
}

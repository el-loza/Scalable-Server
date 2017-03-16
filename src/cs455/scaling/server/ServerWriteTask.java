package cs455.scaling.server;

import cs455.scaling.utilities.SyncKey;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by eloza on 3/8/17.
 */
public class ServerWriteTask implements Runnable{
    private final Server server;
    private final SyncKey key;
    private final String message;
    private final ServerStats ss;

    public ServerWriteTask(Server server, SyncKey key, String message, ServerStats ss){
        this.ss = ss;
        this.server = server;
        this.key = key;
        this.message = message;
    }

    @Override
    public void run() {
        String socketName = key.getSocketName();
        byte[] messageArray = new String(message).getBytes();
        ByteBuffer messageBuffer = ByteBuffer.wrap(messageArray);
        int result;
        try {
            //System.out.println(socketName + " : Attemping to write");
            result = key.write(messageBuffer);
            if (result == 0 ){
                System.out.println("Did not send " + message+ " :Send Buffer Full");
            }
            else {
                ss.increaseSent();
                //System.out.println(socketName + " : Sent");
            }
        } catch (IOException e) {
            System.out.println("Could Not Send:" + message);
            e.printStackTrace();
        }
    }
}

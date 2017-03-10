package cs455.scaling.server;

import cs455.scaling.threadpool.ThreadPoolManager;
import cs455.scaling.utilities.ByteGenerator;
import cs455.scaling.utilities.SyncKey;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by eloza on 3/8/17.
 */
public class ServerReadTask implements Runnable{
    private final Server server;
    private final ByteBuffer messageBuffer;
    private final SyncKey key;
    private final ThreadPoolManager tpm;

    public ServerReadTask(Server server,  SyncKey key, ThreadPoolManager tpm){
        this.server = server;
        this.key = key;
        this.tpm = tpm;
        //messageBuffer = ByteBuffer.allocate(8 *1024);
        messageBuffer = ByteBuffer.allocate(1024 * 9);
    }


    @Override
    public void run() {
        String socketName = key.getSocketName();
        int read = 0;
        messageBuffer.clear();
        try{
            read = key.read(messageBuffer);
        } catch (IOException e){
            key.close();
            return;
        }

        if (read == -1){
            key.close();
            return;
        } else if (read != 0){
            messageBuffer.flip();
            byte[] arr = new byte[8 * 1024];
            messageBuffer.get(arr);
            String hashString = ByteGenerator.SHA1FromBytes(arr);
            System.out.println(socketName +" : created hash: " + hashString);
            tpm.enqueueTask(new ServerWriteTask(server, key, hashString));
        }


    }
}

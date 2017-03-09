package cs455.scaling.server;

import cs455.scaling.threadpool.ThreadPoolManager;
import cs455.scaling.utilities.ByteGenerator;
import cs455.scaling.utilities.SyncKey;

import java.io.IOException;
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
        messageBuffer = ByteBuffer.allocate(8000);
    }

    @Override
    public void run() {
        int read = 0;
        messageBuffer.clear();
        try{
            while (messageBuffer.hasRemaining() && read !=-1){
                read = key.read(messageBuffer);
            }
        } catch (IOException e){
            key.close();
            return;
        }

        if (read == -1){
            key.close();
            return;
        }
        byte[] arr = new byte[messageBuffer.remaining()];
        messageBuffer.get(arr);
        String hashString = ByteGenerator.SHA1FromBytes(arr);
        tpm.enqueueTask(new ServerWriteTask(server, key, hashString));
    }
}

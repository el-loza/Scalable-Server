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
    private final ServerStats ss;

    public ServerReadTask(Server server,  SyncKey key, ThreadPoolManager tpm, ByteBuffer readBuff, ServerStats ss){
        this.ss = ss;
        this.server = server;
        this.key = key;
        this.tpm = tpm;
        messageBuffer = readBuff;
        //messageBuffer = ByteBuffer.allocate(1024 * 9);
    }


    @Override
    public void run() {
            String socketName = key.getSocketName();
            //messageBuffer.flip();
            byte[] arr = new byte[8 * 1024];
            messageBuffer.get(arr);
            String hashString = ByteGenerator.SHA1FromBytes(arr);
            //System.out.println(socketName +" : created hash: " + hashString);
            //server.addOPStateChange(new OPNode(key, SelectionKey.OP_WRITE));
            tpm.enqueueTask(new ServerWriteTask(server, key, hashString, ss));
            //server.wakeUpSelector();
    }
}

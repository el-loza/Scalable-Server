package cs455.scaling.client;

import cs455.scaling.server.Server;
import cs455.scaling.utilities.ByteGenerator;
import cs455.scaling.utilities.SyncKey;
import cs455.scaling.utilities.SyncLinkedList;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by eloza on 3/9/17.
 */
public class NIOSender extends Thread{
    private final SelectionKey key;

    private SyncLinkedList<String> hashList;
    private int messSize;
    private int rate;
    private SocketChannel socket;
    private ClientStats cs;



    public NIOSender(SelectionKey key, SyncLinkedList<String> hashList, int messSize, int rate, ClientStats cs) {
        this.key = key;
        socket = (SocketChannel) key.channel();
        this.hashList = hashList;
        this.messSize = messSize;
        this.rate = rate;
        this.cs = cs;
    }

    public void run() {


        while(true){
            byte[] rando = ByteGenerator.randomMessage(messSize);
            ByteBuffer randoBuff = ByteBuffer.wrap(rando);
            String hashString = ByteGenerator.SHA1FromBytes(rando);
            //System.out.println("Hash String Generated: " + hashString);
            int result;
            try {
                result = socket.write(randoBuff);
                if (result == 0 ){
                    System.out.println("Did not send " + rando+ " :Send Buffer Full");
                } else{
                    cs.increaseSent();
                    hashList.push(hashString);
                    //System.out.println("Send Successful: Hash added to List");

                }
                Thread.sleep(1000/rate);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

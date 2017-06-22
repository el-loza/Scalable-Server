package cs455.scaling.client;

//import cs455.scaling.test.ChangeRequest;
import cs455.scaling.utilities.SyncKey;
import cs455.scaling.utilities.SyncLinkedList;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;

/**
 * Created by eloza on 3/9/17.
 */
public class NIOReceiver extends Thread{

    private InetAddress hostAddress;
    private int port;
    private Selector selector;
    private ByteBuffer readBuffer = ByteBuffer.allocate(8192);
    private SyncLinkedList<String> hashes;
    private NIOSender sender;
    private int messSize;
    private int rate;
    private ClientStats cs;


    public NIOReceiver(InetAddress hostAddress, int port, int messSize, int rate, SyncLinkedList<String> hashes, ClientStats cs) throws IOException {
        this.hostAddress = hostAddress;
        this.port = port;
        this.selector = Selector.open();
        this.hashes = hashes;
        this.messSize = messSize;
        this.rate = rate;
        this.cs = cs;
    }



    private	void connect(SelectionKey key)	throws	IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        channel.finishConnect();
        key.interestOps(SelectionKey.OP_READ);
        sender = new NIOSender(key,hashes, messSize, rate, cs);
        sender. start();
    }

    private void read(SelectionKey key){
        SocketChannel socketChannel = (SocketChannel) key.channel();
        readBuffer.clear();

        int numRead = 0;
        try {
            numRead = socketChannel.read(readBuffer);
        } catch (IOException e) {
            key.cancel();
            try {
                socketChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }

        if (numRead == -1) {
            try {
                key.channel().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            key.cancel();
            return;
        } else if (numRead != 0){
            cs.increaseReceived();
            readBuffer.flip();
            byte[] byteArr = new byte[readBuffer.remaining()];
            readBuffer.get(byteArr);
            String recvHash = new String(byteArr);
            //System.out.println(recvHash);
            boolean check = hashes.removeIfPresent(recvHash);
            if (check == true){
                //System.out.println("Successfully removed from has list :)");
            } else {
                System.out.println("ERROR: Hash not successfully removed from has list.");
            }


        }



    }

    public void run(){
        try{
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(this.hostAddress, this.port));
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }catch (IOException e){
            System.out.println("ERROR: Could not connect to Server!");
            e.printStackTrace();
        }
        while (true) {
            try {
                selector.select();
                Iterator selectedKeys = selector.selectedKeys().iterator();

                while (selectedKeys.hasNext()) {
                    SelectionKey key = (SelectionKey) selectedKeys.next();
                    selectedKeys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isConnectable()) {
                        this.connect(key);
                    } else if (key.isReadable()) {
                        this.read(key);
                    } else if (key.isWritable()) {
                        // Not Implemented
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

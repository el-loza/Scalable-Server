package cs455.scaling.server;

import cs455.scaling.threadpool.ThreadPoolManager;
import cs455.scaling.utilities.SyncKey;
import cs455.scaling.utilities.SyncLinkedList;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * Created by eloza on 3/8/17.
 */
public class Server extends Thread {
    private InetAddress serverAddress;
    private int port;
    private ServerSocketChannel serverChannel;
    private Selector selector;

    private final ThreadPoolManager tpm;

    private final HashMap<SelectionKey, SyncKey> keyMap = new HashMap<SelectionKey, SyncKey>();

    private ByteBuffer messageBuffer = ByteBuffer.allocate(10000);

    public Server(InetAddress serverAddress, int port, ThreadPoolManager tpm) throws IOException{
        this.tpm = tpm;
        this.serverAddress = serverAddress;
        this.port = port;
        this.selector = startSelector(serverAddress, port);
    }

    private Selector startSelector(InetAddress serverAddress, int port) throws IOException{
        Selector serverSelector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        InetSocketAddress isa = new InetSocketAddress(serverAddress, port);
        serverChannel.socket().bind(isa);
        serverChannel.register(serverSelector, SelectionKey.OP_ACCEPT);
        return serverSelector;
    }


    private void accept(SelectionKey key) throws IOException{
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        keyMap.put(key, new SyncKey(key));
    }

    private void read(SelectionKey key) throws IOException{
        SyncKey keyTemp = keyMap.get(key);
        tpm.enqueueTask(new ServerReadTask(this, keyTemp, tpm));
    }

    public void run(){
        while (true){
            try{
                selector.select();

                Iterator selectedKeys = this.selector.selectedKeys().iterator();
                while(selectedKeys.hasNext()){
                    SelectionKey key = (SelectionKey) selectedKeys.next();
                    selectedKeys.remove();

                    if(!key.isValid()){
                        continue;
                    } else if(key.isAcceptable()){
                        accept(key);
                    } else if (key.isReadable()){
                        read(key);
                    } else if (key.isWritable()){
                        //write(key);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args){
        if (args.length != 2){
            System.out.println("ERROR: Please follow this format, java cs455.scaling.server.Server portnum thread-pool-size");
            return;
        }

        int portnum = Integer.getInteger(args[0]);
        int numThreads = Integer.getInteger(args[1]);
        ThreadPoolManager tpm = new ThreadPoolManager(numThreads);
        try {
            Server ss = new Server(InetAddress.getLocalHost(), portnum, tpm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
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

    private SyncLinkedList opNodeList = new SyncLinkedList();

    public Server(InetAddress serverAddress, int port, ThreadPoolManager tpm) throws IOException{
        this.tpm = tpm;
        this.serverAddress = serverAddress;
        this.port = port;
        this.selector = startSelector(serverAddress, port);
    }

    private Selector startSelector(InetAddress serverAddress, int port) throws IOException{
        Selector serverSelector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.socket().setReceiveBufferSize(128*1024);
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
        socketChannel.socket().setSendBufferSize(128*1024);
        socketChannel.register(selector, SelectionKey.OP_READ);
        keyMap.put(socketChannel.keyFor(selector), new SyncKey(socketChannel.keyFor(selector), tpm, this));
    }

    private void read(SelectionKey key) throws IOException{
        SyncKey keyTemp = keyMap.get(key);
        keyTemp.enqueReadTask();
    }

    public void addOPStateChange(OPNode opn){
        synchronized (opNodeList){
            opNodeList.add(opn);
        }
    }

    private void checkOPStateChanges() throws IOException {
        synchronized (opNodeList) {
            Iterator nodes = opNodeList.iterator();
            while (nodes.hasNext()) {
                OPNode node = (OPNode) nodes.next();
                        node.socket.interestOps(node.ops);
            }
        }
            opNodeList.clear();
    }

    public void wakeUpSelector(){
        synchronized (selector){
            selector.wakeup();
        }
    }


    public void run(){
        while (true){
            try{
                selector.select();

                checkOPStateChanges();

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

        int portnum = Integer.parseInt(args[0]);
        int numThreads = Integer.parseInt(args[1]);
        ThreadPoolManager tpm = new ThreadPoolManager(numThreads);
        tpm.startWorkers();
        tpm.start();
        try {
            //InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println(inetAddress.toString());
            Server ss = new Server(inetAddress, portnum, tpm);
            ss.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

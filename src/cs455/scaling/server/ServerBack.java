//package cs455.scaling.server;
//
//import cs455.scaling.threadpool.ThreadPoolManager;
//import cs455.scaling.utilities.SyncLinkedList;
//
//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.nio.ByteBuffer;
//import java.nio.channels.SelectionKey;
//import java.nio.channels.Selector;
//import java.nio.channels.ServerSocketChannel;
//import java.nio.channels.SocketChannel;
//import java.util.*;
//
///**
// * Created by eloza on 3/8/17.
// */
//public class Server extends Thread {
//    private InetAddress serverAddress;
//    private int port;
//    private ServerSocketChannel serverChannel;
//    private Selector selector;
//
//    private final ThreadPoolManager tpm;
//
//
//    private final SyncLinkedList<ChannelChange> channelChanges = new SyncLinkedList<ChannelChange>();
//    private final Map pendingData = new HashMap();
//
//    private ByteBuffer messageBuffer = ByteBuffer.allocate(10000);
//
//    public Server(InetAddress serverAddress, int port, ThreadPoolManager tpm) throws IOException{
//        this.tpm = tpm;
//        this.serverAddress = serverAddress;
//        this.port = port;
//        this.selector = startSelector(serverAddress, port);
//    }
//
//    private Selector startSelector(InetAddress serverAddress, int port) throws IOException{
//        Selector serverSelector = Selector.open();
//        serverChannel = ServerSocketChannel.open();
//        serverChannel.configureBlocking(false);
//        InetSocketAddress isa = new InetSocketAddress(serverAddress, port);
//        serverChannel.socket().bind(isa);
//        serverChannel.register(serverSelector, SelectionKey.OP_ACCEPT);
//        return serverSelector;
//    }
//
//    private void checkOPChanges() throws IOException{
//        synchronized (channelChanges){
//            while(!channelChanges.isEmpty()){
//                ChannelChange request = channelChanges.remove();
//                SelectionKey key = request.socketChannel.keyFor(selector);
//                if(request.requestType == ChannelChange.CHANGEOPS){
//                    key.interestOps(request.ops);
//                }
//                else if (request.requestType ==ChannelChange.CLOSECHANNEL){
//                    key.channel().close();
//                    key.cancel();
//                }
//            }
//        }
//    }
//
//    public void addOPChange(ChannelChange request){
//        synchronized (channelChanges){
//            channelChanges.add(request);
//        }
//    }
//
//    public void addPendingData(SocketChannel socket, String data){
//        addOPChange(new ChannelChange(socket, ChannelChange.CHANGEOPS, SelectionKey.OP_WRITE));
//        synchronized (pendingData){
//            List queue = (List) this.pendingData.get(socket);
//            if (queue == null){
//                queue = new ArrayList<String>();
//                pendingData.put(socket, queue);
//            }
//            queue.add(data);
//        }
//        this.selector.wakeup();
//    }
//
//    public String getPendingData(SocketChannel socket){
//        synchronized (pendingData){
//            List queue = (ArrayList) pendingData.get(socket);
//            if(queue == null){
//                return null;
//            } else {
//                return (String) queue.remove(0);
//            }
//        }
//    }
//
//    private void accept(SelectionKey key) throws IOException{
//        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
//        SocketChannel socketChannel = serverSocketChannel.accept();
//        socketChannel.socket().setSendBufferSize(8000);
//        socketChannel.socket().setReceiveBufferSize(40000);
//        socketChannel.configureBlocking(false);
//        socketChannel.register(selector, SelectionKey.OP_READ);
//    }
//
//    private void read(SelectionKey key) throws IOException{
//        SocketChannel socket = (SocketChannel) key.channel();
//        tpm.enqueueTask(new ServerReadTask(this, socket));
//    }
//
//    public void run(){
//        while (true){
//            try{
//                checkOPChanges();
//                selector.select();
//
//                Iterator selectedKeys = this.selector.selectedKeys().iterator();
//                while(selectedKeys.hasNext()){
//                    SelectionKey key = (SelectionKey) selectedKeys.next();
//                    selectedKeys.remove();
//
//                    if(!key.isValid()){
//                        continue;
//                    } else if(key.isAcceptable()){
//                        accept(key);
//                    } else if (key.isReadable()){
//                        read(key);
//                    } else if (key.isWritable()){
//                        //write(key);
//                    }
//                }
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }
//    }
//}

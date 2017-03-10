package cs455.scaling.utilities;

import cs455.scaling.server.Server;
import cs455.scaling.server.ServerReadTask;
import cs455.scaling.threadpool.ThreadPoolManager;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by eloza on 3/8/17.
 */
public class SyncKey {
    private final SelectionKey key;
    private final SocketChannel socket;
    private final Lock readLock = new ReentrantLock();
    private final Lock writeLock = new ReentrantLock();
    private final ThreadPoolManager tpm;
    private final Server server;
    private final int messSize = (8 * 1024);

    public SyncKey (SelectionKey key, ThreadPoolManager tpm, Server server){
        this.tpm = tpm;
        this.key = key;
        this.server = server;
        socket = (SocketChannel) key.channel();
    }

    //-----Custom Methods
    public void enqueReadTask(){
        if(readLock.tryLock()){
            try{
                tpm.enqueueTask(new ServerReadTask(server, this, tpm));
                //System.out.println("SERVER: Added reading task to queue");
            } finally {
                readLock.unlock();
            }
        }
    }

    public String getSocketName(){
        return socket.socket().getInetAddress().toString();
    }

    //------- Socket Methods--------------
    public int read(ByteBuffer dst) throws IOException{
        int read = 0;
        int count = 0;
        if (readLock.tryLock()){
            try{
                //System.out.println("SERVERREADTASK: Reading message");
                while (dst.hasRemaining() && read !=-1){
                    read = socket.read(dst);
                    count += read;
                }
            } finally {
                readLock.unlock();
            }
        }
        return count;
    }

    public int write(ByteBuffer src) throws IOException{
        synchronized (writeLock){
            socket.socket().getInetAddress().toString();
            //System.out.println("Attemping to write....");
            return socket.write(src);
        }
    }

    public synchronized  SelectionKey register(Selector selector, int ops) throws IOException{
        synchronized (writeLock){
            synchronized (readLock){
                return socket.register(selector, ops);

            }
        }
    }

    public synchronized void close(){
        synchronized (writeLock){
            synchronized (readLock){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                key.channel();
            }
        }
    }

    public SelectionKey keyFor(Selector sel) throws IOException{
        return socket.keyFor(sel);
    }

    //-----Key Methods
    public SelectionKey interestOps(int ops) throws IOException{
        return key.interestOps(ops);
    }

    public boolean isValid(){
        return key.isValid();
    }

    public boolean isReadable(){
        return key.isReadable();
    }

    public boolean isWriteable(){
        return key.isWritable();
    }

}

package cs455.scaling.utilities;

import java.io.IOException;
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

    public SyncKey (SelectionKey key){
        this.key = key;
        socket = (SocketChannel) key.channel();
    }

    //------- Socket Methods--------------
    public int read(ByteBuffer dst) throws IOException{
        synchronized (readLock){
            return socket.read(dst);
        }
    }

    public int write(ByteBuffer src) throws IOException{
        synchronized (writeLock){
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

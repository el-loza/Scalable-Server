package cs455.scaling.client;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by eloza on 3/10/17.
 */
public class ClientStats {
    private final ClientStatNode csn = new ClientStatNode();

    public synchronized void increaseSent(){
        csn.increaseSent();
    }

    public synchronized void increaseReceived(){
        csn.increaseReceived();
    }

    public synchronized ClientStatNode getStats(){
        return csn.getStats();
    }
}

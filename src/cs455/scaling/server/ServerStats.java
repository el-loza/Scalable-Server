package cs455.scaling.server;

/**
 * Created by eloza on 3/16/17.
 */
public class ServerStats {
    private final ServerStatNode ssn = new ServerStatNode();

    public synchronized void increaseSent(){
        ssn.increaseSent();
    }

    public synchronized void increaseConnections(){
        ssn.increaseConnections();
    }

    public synchronized ServerStatNode getStats(){
        ServerStatNode statTemp = ssn.getStats();
        ssn.clear();
        return statTemp;
    }

}

package cs455.scaling.server;

/**
 * Created by eloza on 3/16/17.
 */
public class ServerStatNode {
    private int connections;
    private int messagesSent ;

    public ServerStatNode(){
        connections = 0;
        messagesSent = 0;
    }

    private ServerStatNode(int s, int r){
        this.connections = s;
        this.messagesSent = r;
    }

    public void increaseConnections(){
        connections++;
    }

    public void increaseSent(){
        messagesSent++;
    }

    public ServerStatNode getStats(){
        return new ServerStatNode(connections, messagesSent);
    }

    public int getConnections(){
        return connections;
    }

    public int getSent(){
        return messagesSent;
    }

    public void clear() {
        messagesSent = 0;
    }
}

package cs455.scaling.client;

/**
 * Created by eloza on 3/10/17.
 */
public class ClientStatNode {
    private int sentM ;
    private int receivedM ;

    public ClientStatNode(){
        sentM = 0;
        receivedM = 0;
    }

    private ClientStatNode(int s, int r){
        this.sentM = s;
        this.receivedM = r;
    }

    public void increaseSent(){
        sentM++;
    }

    public void increaseReceived(){
        receivedM++;
    }

    public ClientStatNode getStats(){
        return new ClientStatNode(sentM, receivedM);
    }

    public int getSentM(){
        return sentM;
    }

    public int getReceivedM(){
        return receivedM;
    }
}

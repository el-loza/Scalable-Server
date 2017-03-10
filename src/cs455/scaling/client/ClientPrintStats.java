package cs455.scaling.client;

import java.util.TimerTask;
import java.sql.Timestamp;

/**
 * Created by eloza on 3/10/17.
 */
public class ClientPrintStats extends TimerTask {
    private ClientStats cs;
    private Timestamp timeStamp = new Timestamp(System.currentTimeMillis());

    public ClientPrintStats(ClientStats cs){
        this.cs = cs;
    }


    @Override
    public void run() {
        ClientStatNode csn = cs.getStats();
        int sentM = csn.getSentM();
        int receivedM = csn.getReceivedM();
        System.out.println("[" + timeStamp + "] Total Sent Count: " + sentM + ",Total Received Count: " + receivedM);
    }
}

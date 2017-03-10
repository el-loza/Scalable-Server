package cs455.scaling.client;

import java.time.LocalDateTime;
import java.util.TimerTask;

/**
 * Created by eloza on 3/10/17.
 */
public class ClientPrintStats extends TimerTask {
    private ClientStats cs;

    public ClientPrintStats(ClientStats cs){
        this.cs = cs;
    }


    @Override
    public void run() {
        ClientStatNode csn = cs.getStats();
        int sentM = csn.getSentM();
        int receivedM = csn.getReceivedM();
        LocalDateTime today = LocalDateTime.now();
        System.out.println("[" + today + "] Total Sent Count: " + sentM + ",Total Received Count: " + receivedM);
    }
}

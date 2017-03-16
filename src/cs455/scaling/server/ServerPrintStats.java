package cs455.scaling.server;

import java.time.LocalDateTime;
import java.util.TimerTask;

/**
 * Created by eloza on 3/16/17.
 */
public class ServerPrintStats extends TimerTask {
    private ServerStats ss;
    private final int frequency;

    public ServerPrintStats(ServerStats ss, int frequency){
        this.frequency = frequency;
        this.ss = ss;
    }


    @Override
    public void run() {
        ServerStatNode ssn = ss.getStats();
        int connections = ssn.getConnections();
        int sentM = ssn.getSent();
        LocalDateTime today = LocalDateTime.now();
        System.out.println("[" + today + "] Current Server Throughput: " + (sentM/frequency) + "/s, Active Client Connections: " + connections);
    }
}

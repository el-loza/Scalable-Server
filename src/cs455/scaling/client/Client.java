package cs455.scaling.client;

import cs455.scaling.utilities.ByteGenerator;
import cs455.scaling.utilities.SyncLinkedList;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Timer;

/**
 * Created by eloza on 3/4/17.
 */
public class Client{

    private int rate;
    private int messSize;
    private SyncLinkedList<String> hashes;
    private InetAddress serverIP;
    private int serverPort;
    private NIOReceiver receiver;
    private ClientStats cs = new ClientStats();
    protected ClientPrintStats cps = new ClientPrintStats(cs);

    public Client(InetAddress serverIP, int serverPort,int rate, int messSize) throws IOException {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.rate = rate;
        this.messSize = messSize;
        hashes = new SyncLinkedList<String>();
        receiver = new NIOReceiver(serverIP,serverPort,messSize,rate, hashes, cs);
    }

    private void startReceiver(){
        receiver.start();
    }

    public static void main(String[] args){
        if (args.length != 3){
            System.out.println("USAGE: Please follow this format java cs455.scaling.client.Client [server-host] [server-port] [message-rate]");
            System.exit(1);
        }
        int serverPort = Integer.parseInt(args[1]);
        int messRate = Integer.parseInt(args[2]);


        String serverIP = args[0];
        Client ca = null;
        try {
            InetAddress inetAddress = InetAddress.getByName(serverIP);
            ca = new Client(inetAddress, serverPort, messRate, (8 * 1024));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ca.startReceiver();
        Timer timer = new Timer();
        timer.schedule(ca.cps, 0, 10000);
    }
}

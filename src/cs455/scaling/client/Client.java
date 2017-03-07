package cs455.scaling.client;

import cs455.scaling.utilities.ByteGenerator;
import cs455.scaling.utilities.SyncLinkedList;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by eloza on 3/4/17.
 */
public class Client{

    private int rate;
    private int messSize;
    private SyncLinkedList<String> hashes;
    private String serverIP;
    private int serverPort;
    private Receiver receiver;

    public Client(String serverIP, int serverPort,int rate, int messSize) throws IOException {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.rate = rate;
        this.messSize = messSize;
        hashes = new SyncLinkedList<String>();
        receiver = new Receiver(serverPort, serverIP, hashes, 8000, rate);
    }

    private void startReceiver(){
        receiver.start();
    }

    public static void main(String[] args){
        if (args.length != 3){
            System.out.println("USAGE: Please follow this format java cs455.scaling.client.Client [server-host] [server-port] [message-rate]");
            System.exit(1);
        }

        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);
        int messRate = Integer.parseInt(args[2]);

        Client ca = null;
        try {
            ca = new Client(serverIP, serverPort, messRate, 8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ca.startReceiver();
    }
}

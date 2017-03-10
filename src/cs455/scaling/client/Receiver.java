//package cs455.scaling.client;
//
//import cs455.scaling.utilities.SyncLinkedList;
//import cs455.scaling.wireformats.Message;
//import cs455.scaling.wireformats.MessageType;
//
//import java.io.DataInputStream;
//import java.io.IOException;
//import java.net.Socket;
//import java.util.LinkedList;
//
///**
// * Created by eloza on 3/6/17.
// */
//public class Receiver extends Thread {
//    private final int serverPort;
//    private final String serverIP;
//    private final Socket sSocket;
//    private DataInputStream din;
//    private SyncLinkedList<String> hashList;
//    private int messSize;
//    private Sender tcpSender;
//    private int rate;
//
//    public Receiver(int serverPort, String serverIP, SyncLinkedList<String> hashList, int messSize, int rate) throws IOException{
//        this.serverPort = serverPort;
//        this.serverIP = serverIP;
//        this.hashList = hashList;
//        this.messSize = messSize;
//        this.rate = rate;
//        sSocket = new Socket(serverIP, serverPort);
//        din = new DataInputStream(sSocket.getInputStream());
//        tcpSender = new Sender(sSocket, hashList, messSize, rate);
//    }
//
//    private void startSender(){
//        tcpSender.start();
//    }
//
//    public void run(){
//        int dataLength;
//        Message message;
//        MessageType mt = new MessageType();
//        startSender();
//        while (sSocket != null){
//            try{
//                dataLength = din.readInt();
//                byte[] data = new byte[dataLength];
//                din.readFully(data, 0, dataLength);
//                message = new Message(data);
//                System.out.println("Received From Server: " + message.getMessage());
//                if (message.getType() == mt.SERVER_RHASH){
//                    String rHash = message.getMessage();
//                    hashList.removeIfPresent(rHash);
//                    System.out.println("Successfully Removed Hash: " + rHash);
//                }
//            } catch (IOException e){
//                System.out.println("ERROR: troubles reading from socket.");
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//
//}

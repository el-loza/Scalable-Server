package cs455.scaling.client;

import cs455.scaling.utilities.ByteGenerator;
import cs455.scaling.utilities.SyncLinkedList;
import cs455.scaling.wireformats.MessageType;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by eloza on 3/6/17.
 */
public class Sender extends Thread {
    private Socket sSocket;
    private DataOutputStream dout;
    private SyncLinkedList<String> hashList;
    private int messSize;
    private int rate;

    public Sender(Socket sSocket, SyncLinkedList<String> hashList, int messSize, int rate) throws IOException{
        this.sSocket = sSocket;
        this.hashList = hashList;
        this.messSize = messSize;
        this.rate = rate;
        dout = new DataOutputStream(sSocket.getOutputStream());
    }

    private byte[] toByteArray(byte[] rando) throws IOException{
        MessageType mt = new MessageType();
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream ddout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

        ddout.writeInt(mt.CLIENT_HASH);

        int elementLength = rando.length;
        ddout.writeInt(elementLength);
        ddout.write(rando);


        ddout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        ddout.close();
        return marshalledBytes;
    }

    public void run(){
        //while (sSocket != null){
        for(int i = 0; i < 20; i++){
            byte[] rando = ByteGenerator.randomMessage(messSize);
            String hashString = ByteGenerator.SHA1FromBytes(rando);
            System.out.println("Hash String Generated: " + hashString);
            try {
                byte[] messageToServer = toByteArray(rando);
                int dataLength = messageToServer.length;
                dout.writeInt(dataLength);
                dout.write(messageToServer, 0, dataLength);
                dout.flush();
                hashList.push(hashString);
                System.out.println("Send Successful: Hash added to List");
                Thread.sleep(1000/rate);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }
}

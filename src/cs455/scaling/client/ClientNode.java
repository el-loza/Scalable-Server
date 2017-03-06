package cs455.scaling.client;

import cs455.scaling.utilities.ByteGenerator;

/**
 * Created by eloza on 3/4/17.
 */
public class ClientNode extends Thread{

    private int rate;
    private int messSize;

    public ClientNode(int rate, int messSize){
        this.rate = rate;
        this.messSize = messSize;
    }

    public void run (){
        for(int i = 0; i < iteraa; i++){
            sendMessage(messSize);
            try {
                Thread.sleep(1000/rate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(int messSize){
        byte[] rando = ByteGenerator.randomMessage(messSize);
        System.out.println(ByteGenerator.SHA1FromBytes(rando));
    }

    public static void main(String[] args){
        ClientNode ca = new ClientNode(2, 80, 8000);
        ca.start();
    }
}

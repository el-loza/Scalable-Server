package cs455.scaling.server;

import cs455.scaling.test.ChangeRequest;

import java.nio.channels.SocketChannel;

/**
 * Created by eloza on 3/8/17.
 */
public class ChannelChange {
    public static final int REGISTER = 1;
    public static final int CHANGEOPS = 2;
    public static final int CLOSECHANNEL =3;

    public SocketChannel socketChannel;
    public int requestType;
    public int ops;

    public ChannelChange(SocketChannel socketChannel, int requestType, int ops){
        this.socketChannel = socketChannel;
        this. requestType = requestType;
        this.ops = ops;
    }


}

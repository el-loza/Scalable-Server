package cs455.scaling.wireformats;

import java.io.*;

/**
 * Created by el-loza on 2/3/17.
 */
public class Message {
    protected int type;
    protected String message;

    public Message(int type, String message){
        this.type = type;
        this.message = message;
    }

    public Message(byte[] marshalledBytes) throws IOException{
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

        type = din.readInt();

        int messLeng = din.readInt();
        byte[] messageBytes = new byte[messLeng];
        din.readFully(messageBytes);

        message = new String(messageBytes);

        baInputStream.close();
        din.close();
    }

    public Message(){}

    public byte[] toByteArray() throws IOException{
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

        dout.writeInt(type);

        byte[] messageBytes = message.getBytes();
        int elementLength = messageBytes.length;
        dout.writeInt(elementLength);
        dout.write(messageBytes);


        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();
        return marshalledBytes;
    }

    public String getMessage() {
        return message;
    }

    public int getType(){
        return type;
    }

}

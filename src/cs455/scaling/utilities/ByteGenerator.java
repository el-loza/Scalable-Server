package cs455.scaling.utilities;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by eloza on 3/4/17.
 */
public class ByteGenerator {

    public static byte[] randomMessage(int messSize){
        byte[] byteArr = new byte[messSize];
        new Random().nextBytes(byteArr);
        return byteArr;
    }

    public static String SHA1FromBytes(byte[] data) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = digest.digest(data);
        BigInteger hashInt = new BigInteger(1, hash);
        return String.format("%40s", hashInt.toString(16).replaceAll(" ", "0"));
    }

    public static void main(String[] args){
        byte[] rando = ByteGenerator.randomMessage(8000);
        String hashers = ByteGenerator.SHA1FromBytes(rando);
        String randoString = new String(rando);
        System.out.println(randoString);
        System.out.println(hashers);
    }
}


package cs455.scaling.server;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import cs455.scaling.utilities.SyncKey;

import java.nio.channels.SocketChannel;

/**
 * Created by eloza on 3/10/17.
 */
public class OPNode {

    public SyncKey socket;
    public int ops;

    public OPNode(SyncKey socket, int ops) {
        this.socket = socket;
        this.ops = ops;
    }
}

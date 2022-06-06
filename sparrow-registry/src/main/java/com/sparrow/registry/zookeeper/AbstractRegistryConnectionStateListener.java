package com.sparrow.registry.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;

/**
 * Registry Connection State Listener
 *
 * @author harry
 */
public abstract class AbstractRegistryConnectionStateListener implements ConnectionStateListener {

    /**
     * connection lost
     */
    protected abstract void lost();

    /**
     * reconnected
     */
    protected abstract void reconnected();

    /**
     * connect
     */
    protected abstract void connect();

    /**
     * read only
     */
    protected abstract void readOnly();

    /**
     * suspect
     */
    protected abstract void suspect();

    @Override
    public void stateChanged(final CuratorFramework client, final ConnectionState state) {
        switch (state) {
            case CONNECTED:
                connect();
                break;
            case LOST:
                lost();
                break;
            case READ_ONLY:
                readOnly();
                break;
            case RECONNECTED:
                reconnected();
                break;
            case SUSPENDED:
                suspect();
                break;
            default:
        }
    }
}

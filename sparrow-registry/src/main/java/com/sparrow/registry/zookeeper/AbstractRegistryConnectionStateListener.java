/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sparrow.registry.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;

/**
 * Registry Connection State Listener
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

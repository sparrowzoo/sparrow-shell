/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.gossip;

import com.codahale.metrics.MetricRegistry;
import org.apache.gossip.event.GossipListener;
import org.apache.gossip.event.GossipState;
import org.apache.gossip.manager.GossipManager;
import org.apache.gossip.model.SharedGossipDataMessage;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Tests support of using {@code StartupSettings} and thereby reading
 * setup config from file.
 */
public class StartupSettingsTest2 {
    private static final Logger log = Logger.getLogger(StartupSettingsTest2.class);
    private static final String CLUSTER = UUID.randomUUID().toString();

    public static void main(String args[]) throws IOException, InterruptedException, URISyntaxException {
        GossipSettings settings = new GossipSettings();
        int seedNodes = 3;
        List<GossipMember> startupMembers = new ArrayList<>();
        for (int i = 1; i < seedNodes + 1; ++i) {
            URI uri = new URI("udp://" + "127.0.0.1" + ":" + (50000 + i));
            startupMembers.add(new RemoteGossipMember("cluster", uri, i + ""));
        }


        int clusterMembers = 5;
        GossipManager gossipManager = null;
        GossipManager gossipManager1 = null;
        for (int i = 1; i < clusterMembers + 1; ++i) {
            URI uri = new URI("udp://" + "127.0.0.1" + ":" + (50000 + i));
            GossipService gossipService = new GossipService("cluster", uri, i + "", new HashMap<>(), startupMembers, settings, new GossipListener() {
                @Override
                public void gossipEvent(GossipMember member, GossipState state) {
                    System.out.println("member " + member.id + ",state" + state.name());
                }
            }, new MetricRegistry());
            if (gossipManager == null) {
                gossipManager = gossipService.getGossipManager();
            }
            gossipManager1 = gossipService.getGossipManager();
            gossipService.start();
        }

        final GossipManager finalGossipManager = gossipManager;
        final GossipManager finalGossipManager1 = gossipManager1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000L);
                        SharedGossipDataMessage v = finalGossipManager1.findSharedGossipData("aaa");
                        if (v != null) {
                            System.out.println(v.getPayload());
                        } else {
                            System.out.println("not found");
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(finalGossipManager.getLiveMembers().toString());
                }
            }
        }).start();
    }

    private static SharedGossipDataMessage sharedDatum(String key, String value) {
        SharedGossipDataMessage m = new SharedGossipDataMessage();
        m.setExpireAt(System.currentTimeMillis() + 5L);
        m.setKey(key);
        m.setPayload(value);
        m.setTimestamp(System.currentTimeMillis());
        return m;
    }

}

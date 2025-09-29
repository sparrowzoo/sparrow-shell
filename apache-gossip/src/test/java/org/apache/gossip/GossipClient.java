package org.apache.gossip;

import com.codahale.metrics.MetricRegistry;
import org.apache.gossip.model.GossipDataMessage;
import org.apache.gossip.model.SharedGossipDataMessage;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GossipClient {
    public static void main(String[] args) throws URISyntaxException, UnknownHostException, InterruptedException {
        GossipSettings settings = new GossipSettings();
        settings.setPersistRingState(false);
        settings.setGossipInterval(5000);
        settings.setPersistDataState(false);
        String cluster = UUID.randomUUID().toString();
        int seedNodes = 1;
        List<GossipMember> startupMembers = new ArrayList<>();
        for (int i = 1; i < seedNodes + 1; ++i) {
            URI uri = new URI("udp://" + "127.0.0.1" + ":" + (50000 + i));
            startupMembers.add(new RemoteGossipMember(cluster, uri, i + ""));
        }


        final List<GossipService> clients = new ArrayList<>();
        final int clusterMembers = 3;
        for (int i = 1; i < clusterMembers + 1; ++i) {
            URI uri = new URI("udp://" + "127.0.0.1" + ":" + (50000 + i));
            GossipService gossipService = new GossipService(cluster, uri, i + "",
                    new HashMap<String, String>(), startupMembers, settings,
                    (a, b) -> {
                        System.out.println("Node " + a + " changed state to " + b);
                    }, new MetricRegistry());
            clients.add(gossipService);
            gossipService.start();
        }


        clients.get(0).gossipPerNodeData(msg());
        clients.get(0).gossipSharedData(sharedMsg());


    }

    private static GossipDataMessage msg() {
        GossipDataMessage g = new GossipDataMessage();
        g.setExpireAt(Long.MAX_VALUE);
        g.setKey("a");
        g.setPayload(" here is localing");
        g.setTimestamp(System.currentTimeMillis());
        return g;
    }

    private static SharedGossipDataMessage sharedMsg() {
        SharedGossipDataMessage g = new SharedGossipDataMessage();
        g.setExpireAt(Long.MAX_VALUE);
        g.setKey("a");
        g.setPayload(" here is remoting");
        g.setTimestamp(System.currentTimeMillis());
        return g;
    }
}

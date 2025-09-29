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

package org.apache.gossip.examples;

import java.net.URI;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.gossip.GossipService;
import org.apache.gossip.GossipSettings;
import org.apache.gossip.RemoteGossipMember;
import org.apache.gossip.manager.DatacenterRackAwareActiveGossiper;

import com.codahale.metrics.MetricRegistry;

public class StandAloneDatacenterAndRack {

  public static void main (String [] args) throws UnknownHostException, InterruptedException {
    GossipSettings s = new GossipSettings();
    s.setWindowSize(10);
    s.setConvictThreshold(1.0);
    s.setGossipInterval(1000);
    s.setActiveGossipClass(DatacenterRackAwareActiveGossiper.class.getName());
    Map<String, String> gossipProps = new HashMap<>();
    gossipProps.put("sameRackGossipIntervalMs", "2000");
    gossipProps.put("differentDatacenterGossipIntervalMs", "10000");
    s.setActiveGossipProperties(gossipProps);
    
    
    Map<String, String> props = new HashMap<>();
    props.put(DatacenterRackAwareActiveGossiper.DATACENTER, args[4]);
    props.put(DatacenterRackAwareActiveGossiper.RACK, args[5]);
    GossipService gossipService = new GossipService("mycluster", URI.create(args[0]), args[1],
            props, Arrays.asList(new RemoteGossipMember("mycluster", URI.create(args[2]), args[3])),
            s, (a, b) -> { }, new MetricRegistry());
    gossipService.start();
    while (true){
      System.out.println("Live: " + gossipService.getGossipManager().getLiveMembers());
      System.out.println("Dead: " + gossipService.getGossipManager().getDeadMembers());
      Thread.sleep(2000);
    }
  }
}

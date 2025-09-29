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
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.codahale.metrics.JmxReporter;
import org.apache.gossip.event.GossipListener;
import org.apache.gossip.manager.GossipManager;
import org.apache.gossip.manager.random.RandomGossipManager;
import org.apache.gossip.model.GossipDataMessage;
import org.apache.gossip.model.SharedGossipDataMessage;
import org.apache.log4j.Logger;

/**
 * This object represents the service which is responsible for gossiping with other gossip members.
 * 
 */
public class GossipService {

  public static final Logger LOGGER = Logger.getLogger(GossipService.class);
  private final JmxReporter jmxReporter;
  
  private final GossipManager gossipManager;

  /**
   * Constructor with the default settings.
   * 
   * @throws InterruptedException
   * @throws UnknownHostException
   */
  public GossipService(StartupSettings startupSettings) throws InterruptedException,
          UnknownHostException {
    this(startupSettings.getCluster(), startupSettings.getUri()
            , startupSettings.getId(), new HashMap<String,String> (),startupSettings.getGossipMembers(),
            startupSettings.getGossipSettings(), null, new MetricRegistry());
  }

  /**
   * Setup the client's lists, gossiping parameters, and parse the startup config file.
   * 
   * @throws InterruptedException
   * @throws UnknownHostException
   */
  public GossipService(String cluster, URI uri, String id, Map<String,String> properties,
          List<GossipMember> gossipMembers, GossipSettings settings, GossipListener listener, MetricRegistry registry)
          throws InterruptedException, UnknownHostException {
    jmxReporter = JmxReporter.forRegistry(registry).build();
    jmxReporter.start();
    gossipManager = RandomGossipManager.newBuilder()
        .withId(id)
        .cluster(cluster)
        .uri(uri)
        .settings(settings)
        .gossipMembers(gossipMembers)
        .listener(listener)
        .registry(registry)
        .properties(properties)
        .build();
  }

  public void start() {
    gossipManager.init();
  }

  public void shutdown() {
    gossipManager.shutdown();
  }

  public GossipManager getGossipManager() {
    return gossipManager;
  }
  
  /**
   * Gossip data in a namespace that is per-node { node-id { key, value } }
   * @param message
   *    message to be gossip'ed across the cluster
   */
  public void gossipPerNodeData(GossipDataMessage message){
    gossipManager.gossipPerNodeData(message);
  }
  
  /**
   * Retrieve per-node gossip data by key
   * 
   * @param nodeId
   *          the id of the node that owns the data
   * @param key
   *          the key in the per-node map to find the data
   * @return the value if found or null if not found or expired
   */
  public GossipDataMessage findPerNodeData(String nodeId, String key){ 
    return getGossipManager().findPerNodeGossipData(nodeId, key);
  }

  /**
   * 
   * @param message
   *          Shared data to gossip around the cluster
   */
  public void gossipSharedData(SharedGossipDataMessage message){
    gossipManager.gossipSharedData(message);
  }
  
  /**
   * 
   * @param key
   *          the key to search for
   * @return the value associated with given key
   */
  public SharedGossipDataMessage findSharedData(String key){
    return getGossipManager().findSharedGossipData(key);
  }
}

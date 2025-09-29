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
package org.apache.gossip.manager;

import com.codahale.metrics.MetricRegistry;
import java.net.URI;

import org.apache.gossip.GossipSettings;
import org.apache.gossip.manager.random.RandomGossipManager;
import org.apache.gossip.model.GossipDataMessage;
import org.apache.gossip.model.SharedGossipDataMessage;
import org.junit.Assert;
import org.junit.Test;

import io.teknek.tunit.TUnit;

public class DataReaperTest {

  private final MetricRegistry registry = new MetricRegistry();
  String myId = "4";
  String key = "key";
  String value = "a";
  
  @Test
  public void testReaperOneShot() {
    GossipSettings settings = new GossipSettings();
    settings.setPersistRingState(false);
    settings.setPersistDataState(false);
    GossipManager gm = RandomGossipManager.newBuilder().cluster("abc").settings(settings)
            .withId(myId).uri(URI.create("udp://localhost:6000")).registry(registry).build();
    gm.init();
    gm.gossipPerNodeData(perNodeDatum(key, value));
    gm.gossipSharedData(sharedDatum(key, value));
    assertDataIsAtCorrectValue(gm);
    gm.getDataReaper().runPerNodeOnce();
    gm.getDataReaper().runSharedOnce();
    assertDataIsRemoved(gm);
    gm.shutdown();
  }

  private void assertDataIsAtCorrectValue(GossipManager gm){
    Assert.assertEquals(value, gm.findPerNodeGossipData(myId, key).getPayload());
    Assert.assertEquals(1, registry.getGauges().get(GossipCoreConstants.PER_NODE_DATA_SIZE).getValue());
    Assert.assertEquals(value, gm.findSharedGossipData(key).getPayload());
    Assert.assertEquals(1, registry.getGauges().get(GossipCoreConstants.SHARED_DATA_SIZE).getValue());
  }
  
  private void assertDataIsRemoved(GossipManager gm){
    TUnit.assertThat(() -> gm.findPerNodeGossipData(myId, key)).equals(null);
    TUnit.assertThat(() -> gm.findSharedGossipData(key)).equals(null);
  }
  
  private GossipDataMessage perNodeDatum(String key, String value) {
    GossipDataMessage m = new GossipDataMessage();
    m.setExpireAt(System.currentTimeMillis() + 5L);
    m.setKey(key);
    m.setPayload(value);
    m.setTimestamp(System.currentTimeMillis());
    return m;
  }
  
  private SharedGossipDataMessage sharedDatum(String key, String value) {
    SharedGossipDataMessage m = new SharedGossipDataMessage();
    m.setExpireAt(System.currentTimeMillis() + 5L);
    m.setKey(key);
    m.setPayload(value);
    m.setTimestamp(System.currentTimeMillis());
    return m;
  }
  
  @Test
  public void testHigherTimestampWins() {
    String myId = "4";
    String key = "key";
    String value = "a";
    GossipSettings settings = new GossipSettings();
    GossipManager gm = RandomGossipManager.newBuilder().cluster("abc").settings(settings)
            .withId(myId).uri(URI.create("udp://localhost:7000")).registry(registry).build();
    gm.init();
    GossipDataMessage before = perNodeDatum(key, value);
    GossipDataMessage after = perNodeDatum(key, "b");
    after.setTimestamp(after.getTimestamp() - 1);
    gm.gossipPerNodeData(before);
    Assert.assertEquals(value, gm.findPerNodeGossipData(myId, key).getPayload());
    gm.gossipPerNodeData(after);
    Assert.assertEquals(value, gm.findPerNodeGossipData(myId, key).getPayload());
    gm.shutdown();
  }

}

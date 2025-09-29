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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.gossip.manager.PassiveGossipConstants;
import org.apache.gossip.secure.KeyTool;
import org.junit.Assert;
import org.junit.Test;

import com.codahale.metrics.MetricRegistry;

import io.teknek.tunit.TUnit;

public class SignedMessageTest {

  @Test(expected=IllegalArgumentException.class)
  public void ifSignMustHaveKeys()
          throws URISyntaxException, UnknownHostException, InterruptedException {
    String cluster = UUID.randomUUID().toString();
    GossipSettings settings = gossiperThatSigns();
    List<GossipMember> startupMembers = new ArrayList<>();
    URI uri = new URI("udp://" + "127.0.0.1" + ":" + (30000 + 1));
    GossipService gossipService = new GossipService(cluster, uri, 1 + "",
            new HashMap<String, String>(), startupMembers, settings, (a, b) -> { }, 
            new MetricRegistry());
    gossipService.start();
  }
  
  private GossipSettings gossiperThatSigns(){
    GossipSettings settings = new GossipSettings();
    settings.setPersistRingState(false);
    settings.setPersistDataState(false);
    settings.setSignMessages(true);
    return settings;
  }
  
  @Test
  public void dataTest() throws InterruptedException, URISyntaxException, NoSuchAlgorithmException, NoSuchProviderException, IOException{
    String keys = "./keys";
    GossipSettings settings = gossiperThatSigns();
    setup(keys);
    String cluster = UUID.randomUUID().toString();
    List<GossipMember> startupMembers = new ArrayList<>();
    for (int i = 1; i < 2; ++i) {
      URI uri = new URI("udp://" + "127.0.0.1" + ":" + (30000 + i));
      startupMembers.add(new RemoteGossipMember(cluster, uri, i + ""));
    }
    final List<GossipService> clients = new ArrayList<>();
    for (int i = 1; i < 3; ++i) {
      URI uri = new URI("udp://" + "127.0.0.1" + ":" + (30000 + i));
      GossipService gossipService = new GossipService(cluster, uri, i + "",
              new HashMap<String,String>(), startupMembers, settings,
              (a,b) -> {}, new MetricRegistry());
      clients.add(gossipService);
      gossipService.start();
    }
    assertTwoAlive(clients);
    assertOnlySignedMessages(clients);
    cleanup(keys, clients);
  }
  
  private void assertTwoAlive(List<GossipService> clients){
    TUnit.assertThat(() -> {
      int total = 0;
      for (int i = 0; i < clients.size(); ++i) {
        total += clients.get(i).getGossipManager().getLiveMembers().size();
      }
      return total;
    }).afterWaitingAtMost(20, TimeUnit.SECONDS).isEqualTo(2);
  }
  
  private void assertOnlySignedMessages(List<GossipService> clients){
    Assert.assertEquals(0, clients.get(0).getGossipManager().getRegistry()
            .meter(PassiveGossipConstants.UNSIGNED_MESSAGE).getCount());
    Assert.assertTrue(clients.get(0).getGossipManager().getRegistry()
            .meter(PassiveGossipConstants.SIGNED_MESSAGE).getCount() > 0);
  }
  
  private void cleanup(String keys, List<GossipService> clients){
    new File(keys, "1").delete();
    new File(keys, "2").delete();
    new File(keys).delete();
    for (int i = 0; i < clients.size(); ++i) {
      clients.get(i).shutdown();
    }
  }
  
  private void setup(String keys) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
    new File(keys).mkdir();
    KeyTool.generatePubandPrivateKeyFiles(keys, "1");
    KeyTool.generatePubandPrivateKeyFiles(keys, "2");
  }
}

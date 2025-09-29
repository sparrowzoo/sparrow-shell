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
import io.teknek.tunit.TUnit;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;

@RunWith(JUnitPlatform.class)
public class TenNodeThreeSeedTest {

  @Test
  public void test() throws UnknownHostException, InterruptedException, URISyntaxException {
    abc(30150);
  }

  @Test
  public void testAgain() throws UnknownHostException, InterruptedException, URISyntaxException{
    abc(30100);
  }

  public void abc(int base) throws InterruptedException, UnknownHostException, URISyntaxException{
    GossipSettings settings = new GossipSettings(1000, 10000, 1000, 1, 2.0, "exponential");
    settings.setPersistRingState(false);
    settings.setPersistDataState(false);
    String cluster = UUID.randomUUID().toString();
    int seedNodes = 3;
    List<GossipMember> startupMembers = new ArrayList<>();
    for (int i = 1; i < seedNodes+1; ++i) {
      URI uri = new URI("udp://" + "127.0.0.1" + ":" + (base + i));
      startupMembers.add(new RemoteGossipMember(cluster, uri, i + ""));
    }
    final List<GossipService> clients = new ArrayList<>();
    final int clusterMembers = 5;
    for (int i = 1; i < clusterMembers+1; ++i) {
      URI uri = new URI("udp://" + "127.0.0.1" + ":" + (base + i));
      GossipService gossipService = new GossipService(cluster, uri, i + "", new HashMap<String,String>(),
              startupMembers, settings, (a,b) -> {}, new MetricRegistry());
      clients.add(gossipService);
      gossipService.start();
    }    
    TUnit.assertThat(new Callable<Integer> (){
      public Integer call() throws Exception {
        int total = 0;
        for (int i = 0; i < clusterMembers; ++i) {
          total += clients.get(i).getGossipManager().getLiveMembers().size();
        }
        return total;
      }}).afterWaitingAtMost(40, TimeUnit.SECONDS).isEqualTo(20);
          
    for (int i = 0; i < clusterMembers; ++i) {
      int j = i;
      new Thread(){
        public void run(){
          clients.get(j).shutdown();
        }
      }.start();
    }
  }
}

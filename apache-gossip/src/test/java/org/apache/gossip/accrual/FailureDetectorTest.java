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
package org.apache.gossip.accrual;

import org.apache.gossip.GossipSettings;
import org.apache.gossip.LocalGossipMember;
import org.apache.gossip.event.GossipState;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;


public class FailureDetectorTest {

    @Test
    public void aNormalTest() {
        int samples = 1;
        int windowSize = 1000;
        LocalGossipMember member = new LocalGossipMember("", URI.create("udp://127.0.0.1:1000"), "", 0L, null, windowSize, samples, "normal");
        member.recordHeartbeat(5);
        member.recordHeartbeat(10);
        System.out.println(member.detect(11));
    }

    @Test
    public void aTest() {
        int samples = 1;
        int windowSize = 1000;
        LocalGossipMember member = new LocalGossipMember("", URI.create("udp://127.0.0.1:1000"),
                "", 0L, null, windowSize, samples, "exponential");
        member.recordHeartbeat(5);
        member.recordHeartbeat(10);
        Assert.assertEquals(3.9D, member.detect(50), .01);
        
        GossipSettings gossipSettings = new GossipSettings();
        for (int i = 10; i < 100; i++) {
            double result = member.detect(i);
            if (result > gossipSettings.getConvictThreshold()) {
                System.out.println(i + "-" + result + " STATE " + GossipState.DOWN);
            }
            if (result <= gossipSettings.getConvictThreshold()) {
                System.out.println(i + "-" + result + " STATE " + GossipState.UP);
            }
        }
    }

    @Ignore
    public void sameHeartbeatTest() {
        int samples = 1;
        int windowSize = 1000;
        LocalGossipMember member = new LocalGossipMember("", URI.create("udp://127.0.0.1:1000"), "", 0L, null, windowSize, samples, "exponential");
        member.recordHeartbeat(5);
        member.recordHeartbeat(5);
        member.recordHeartbeat(5);
        Assert.assertEquals(new Double(0.4342944819032518), member.detect(10));
    }

}

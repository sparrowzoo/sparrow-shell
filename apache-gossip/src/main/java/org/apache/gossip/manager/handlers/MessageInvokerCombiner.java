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

package org.apache.gossip.manager.handlers;

import org.apache.gossip.manager.GossipCore;
import org.apache.gossip.manager.GossipManager;
import org.apache.gossip.model.Base;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageInvokerCombiner implements MessageInvoker {
  private final List<MessageInvoker> invokers = new CopyOnWriteArrayList<>();

  public MessageInvokerCombiner() {
  }

  public boolean invoke(GossipCore gossipCore, GossipManager gossipManager, Base base) {
    if (invokers == null) {
      return false;
    }
    boolean result = false;
    for (MessageInvoker mi : invokers) {
      result = mi.invoke(gossipCore, gossipManager, base) || result;
    }
    return result;
  }

  public void clear() {
    invokers.clear();
  }

  public void add(MessageInvoker mi) {
    if (mi == null) {
      throw new NullPointerException();
    }
    invokers.add(mi);
  }
}

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
import org.apache.gossip.model.*;

public class DefaultMessageInvoker implements MessageInvoker {
  private final MessageInvokerCombiner mic;

  /**
   * 消息收到后的处理逻辑
   * 1. SimpleMessageInvoker 用于将特定类型的消息与其对应的处理器关联起来。
   * 2. 当接收到消息时，SimpleMessageInvoker 会检查消息的类型是否与其关联的类型匹配。
   * 3. 如果匹配，则调用对应的处理器来处理该消息。
   * 4. 如果不匹配，则返回 false，表示该消息类型不在此处理器的处理范围内。
   */
  public DefaultMessageInvoker() {
    mic = new MessageInvokerCombiner();
    mic.add(new SimpleMessageInvoker(Response.class, new ResponseHandler()));
    mic.add(new SimpleMessageInvoker(ShutdownMessage.class, new ShutdownMessageHandler()));
    mic.add(new SimpleMessageInvoker(GossipDataMessage.class, new GossipDataMessageHandler()));
    mic.add(new SimpleMessageInvoker(SharedGossipDataMessage.class, new SharedGossipDataMessageHandler()));
    mic.add(new SimpleMessageInvoker(ActiveGossipMessage.class, new ActiveGossipMessageHandler()));
  }

  public boolean invoke(GossipCore gossipCore, GossipManager gossipManager, Base base) {
    return mic.invoke(gossipCore, gossipManager, base);
  }
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.pipeline;

public class HandlerNextAction {
    public HandlerNextAction(Action action, Object result) {
        this.action = action;
        this.result = result;
    }

    private Action action;
    private Object result;

    public enum Action {
        CONTINUE,
        BREAK,
        ASNY
    }

    public static HandlerNextAction next() {
        return new HandlerNextAction(Action.CONTINUE, true);
    }

    public static HandlerNextAction next(Object result) {
        return new HandlerNextAction(Action.CONTINUE, result);
    }

    public static HandlerNextAction terminate() {
        return new HandlerNextAction(Action.BREAK, true);
    }

    public static HandlerNextAction terminate(Object result) {
        return new HandlerNextAction(Action.BREAK, result);
    }

    public static HandlerNextAction asyn() {
        return new HandlerNextAction(Action.CONTINUE, true);
    }

    public Action getAction() {
        return action;
    }

    public Object getResult() {
        return result;
    }
}

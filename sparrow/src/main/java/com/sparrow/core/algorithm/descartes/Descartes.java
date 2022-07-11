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

package com.sparrow.core.algorithm.descartes;

import com.sparrow.support.LinkedNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Descartes {
    private List<LinkedNode> source;
    private List<LinkedNode> destination = new ArrayList<LinkedNode>();

    public Descartes(List<LinkedNode> source) {
        this.source = source;
    }

    public List<LinkedNode> getDestination() {
        this.recursive(0);
        return destination;
    }

    private void addDestination(Stack<LinkedNode> stack) {
        if (stack == null || stack.size() == 0) {
            return;
        }

        LinkedNode linkedNode = LinkedNode.assembleWithoutNode(stack.get(0));
        if (stack.size() == 1) {
            destination.add(linkedNode);
            return;
        }
        int index = 1;
        LinkedNode currentNode = linkedNode;
        while (index < stack.size()) {
            currentNode = currentNode.setNode(LinkedNode.assembleWithoutNode(stack.get(index++)));
        }
        destination.add(linkedNode);
    }

    private Stack<LinkedNode> stack = new Stack<LinkedNode>();

    public void setSource(List<LinkedNode> source) {
        this.source = source;
    }

    private void recursive(int level) {
        if (level >= source.size()) {
            return;
        }
        LinkedNode linkedNode = source.get(level);
        if (level == source.size() - 1) {

            while (linkedNode != null) {
                stack.push(linkedNode);
                this.addDestination(stack);
                stack.pop();
                linkedNode = linkedNode.getNode();
            }
        }
        level++;
        while (linkedNode != null) {
            stack.push(linkedNode);
            linkedNode = linkedNode.getNode();
            recursive(level);
            stack.pop();
        }
    }
}

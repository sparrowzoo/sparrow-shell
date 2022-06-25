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

package com.sparrow.support;

public class LinkedNode {
    private String id;
    private String nodeType;
    private String nodeName;
    private LinkedNode node;

    public LinkedNode() {
    }

    public LinkedNode(String id, String nodeType, String nodeName) {
        this.id = id;
        this.nodeType = nodeType;
        this.nodeName = nodeName;
    }

    public static LinkedNode assembleWithoutNode(LinkedNode node) {
        return new LinkedNode(node.getId(), node.getNodeType(), node.getNodeName());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public LinkedNode getNode() {
        return node;
    }

    public LinkedNode setNode(LinkedNode node) {
        this.node = node;
        return node;
    }

    @Override public String toString() {
        return "id='" + id + '\'' +
            ", nodeType='" + nodeType + '\'' +
            ", nodeName='" + nodeName + '\'';
    }

    public String toPath() {
        StringBuilder sb = new StringBuilder(this.getNodeName());
        LinkedNode currentNode = this.node;
        while (currentNode != null) {
            sb.append("→");
            sb.append(currentNode.getNodeName());
            currentNode = currentNode.node;
        }
        return sb.toString();
    }
}

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

package com.sparrow.utility;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** HTTP 和 WebSocket 共用的来源 IP 解析；只信任明确配置的直连代理。 */
public final class ClientIpResolver {
    public static final String TRUSTED_PROXIES_PROPERTY = "sparrow.client-ip.trusted-proxies";

    private final Set<InetAddress> trustedProxies;

    public ClientIpResolver(Set<InetAddress> trustedProxies) {
        this.trustedProxies = trustedProxies == null ? Set.of() : Set.copyOf(trustedProxies);
    }

    /** 在创建入口时读取配置；配置变更后需重启服务。 */
    public static ClientIpResolver fromSystemProperties() {
        return new ClientIpResolver(configuredTrustedProxies());
    }

    public static Set<InetAddress> configuredTrustedProxies() {
        return parseTrustedProxies(System.getProperty(TRUSTED_PROXIES_PROPERTY, ""));
    }

    public static Set<InetAddress> parseTrustedProxies(String configured) {
        if (configured == null || configured.isBlank()) {
            return Set.of();
        }
        Set<InetAddress> addresses = new HashSet<>();
        for (String value : configured.split(",", -1)) {
            InetAddress address = literalAddress(value);
            if (address == null) {
                throw new IllegalArgumentException("Trusted proxy must be a literal IP address: " + value);
            }
            addresses.add(address);
        }
        return Set.copyOf(addresses);
    }

    public String resolve(String peer, List<String> forwardedHeaders, List<String> realIpHeaders) {
        if (peer == null || peer.isEmpty()) {
            return "";
        }
        InetAddress address = literalAddress(peer);
        if (address == null) {
            throw new IllegalArgumentException("Client socket address is unavailable");
        }
        return resolve(address, forwardedHeaders, realIpHeaders);
    }

    public String resolve(InetAddress peer, List<String> forwardedHeaders, List<String> realIpHeaders) {
        Objects.requireNonNull(peer, "Client socket address is unavailable");
        String socketIp = peer.getHostAddress();
        if (!trustedProxies.contains(peer)) {
            return socketIp;
        }
        if (!forwardedHeaders.isEmpty()) {
            String[] hops = String.join(",", forwardedHeaders).split(",", -1);
            InetAddress client = peer;
            // 从直连代理向左回溯；遇到不可信来源后不再相信它提供的前缀。
            for (int i = hops.length - 1; i >= 0 && trustedProxies.contains(client); i--) {
                InetAddress hop = literalAddress(hops[i]);
                if (hop == null) {
                    return socketIp;
                }
                client = hop;
            }
            return client.getHostAddress();
        }
        InetAddress realIp = realIpHeaders.size() == 1 ? literalAddress(realIpHeaders.getFirst()) : null;
        return realIp == null ? socketIp : realIp.getHostAddress();
    }

    private static InetAddress literalAddress(String value) {
        if (value == null) {
            return null;
        }
        String ip = value.trim();
        try {
            if (ip.indexOf(':') >= 0) {
                // 仅将包含冒号的十六进制字面值交给 JDK，禁止主机名、作用域和端口。
                if (ip.startsWith("[") && ip.endsWith("]")) {
                    ip = ip.substring(1, ip.length() - 1);
                }
                if (!ip.matches("[0-9a-fA-F:.]+")) {
                    return null;
                }
                return InetAddress.getByName(ip);
            }
            String[] octets = ip.split("\\.", -1);
            if (octets.length != 4) {
                return null;
            }
            byte[] bytes = new byte[4];
            for (int i = 0; i < octets.length; i++) {
                if (!octets[i].matches("[0-9]{1,3}")) {
                    return null;
                }
                int octet = Integer.parseInt(octets[i]);
                if (octet > 255) {
                    return null;
                }
                bytes[i] = (byte) octet;
            }
            return InetAddress.getByAddress(bytes);
        } catch (UnknownHostException e) {
            return null;
        }
    }
}

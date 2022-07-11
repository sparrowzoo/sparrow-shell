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

package com.sparrow.support.ip;

import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.support.IpSupport;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class SparrowIpSupport implements IpSupport {

    private static volatile String cachedIpAddress;

    /**
     * 获取本机IP
     *
     * @return
     */
    @Override
    public String getLocalIp() {
        if (null != cachedIpAddress) {
            return cachedIpAddress;
        }
        String localIpAddress = null;
        synchronized (this) {
            if (null != cachedIpAddress) {
                return cachedIpAddress;
            }
            Enumeration<NetworkInterface> netInterfaces;
            try {
                netInterfaces = NetworkInterface.getNetworkInterfaces();
            } catch (final SocketException ex) {
                throw new RuntimeException("SocketException", ex);
            }
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = netInterfaces.nextElement();
                Enumeration<InetAddress> ipAddresses = netInterface.getInetAddresses();
                while (ipAddresses.hasMoreElements()) {
                    InetAddress ipAddress = ipAddresses.nextElement();
                    if (isPublicIpAddress(ipAddress)) {
                        String publicIpAddress = ipAddress.getHostAddress();
                        cachedIpAddress = publicIpAddress;
                        return publicIpAddress;
                    }
                    if (isLocalIpAddress(ipAddress)) {
                        localIpAddress = ipAddress.getHostAddress();
                    }
                }
            }
        }
        cachedIpAddress = localIpAddress;
        return localIpAddress;
    }

    private static boolean isPublicIpAddress(final InetAddress ipAddress) {
        return !ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress() && !isV6IpAddress(ipAddress);
    }

    private static boolean isLocalIpAddress(final InetAddress ipAddress) {
        return ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress() && !isV6IpAddress(ipAddress);
    }

    private static boolean isV6IpAddress(final InetAddress ipAddress) {
        return ipAddress.getHostAddress().contains(":");
    }

    /**
     * 获取本机Host名称.
     *
     * @return 本机Host名称
     */
    @Override
    public String getLocalHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (final UnknownHostException ex) {
            throw new RuntimeException("UnknownHostException", ex);
        }
    }

    @Override
    public Long toLong(String ip) {
        long result = 0;
        String[] ipAddressInArray = ip.split("\\.");
        if (ipAddressInArray.length < 4) {
            return result;
        }
        for (int i = 3; i >= 0; i--) {
            long ipSegment = Long.parseLong(ipAddressInArray[3 - i]);
            //left shifting 24,16,8,0 and bitwise OR
            result |= ipSegment << (i * 8);
        }
        return result;
    }

    @Override
    public String parse(Long ip) {
        if (ip == 0L) {
            return Symbol.EMPTY;
        }
        return ((ip >> 24) & 0xFF) + "."
            + ((ip >> 16) & 0xFF) + "."
            + ((ip >> 8) & 0xFF) + "."
            + (ip & 0xFF);
    }
}

package com.sparrow.support.ip;

import com.sparrow.protocol.constant.magic.SYMBOL;
import com.sparrow.support.IpSupport;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @author harry
 */
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
            return SYMBOL.EMPTY;
        }
        return ((ip >> 24) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "."
                + (ip & 0xFF);
    }
}

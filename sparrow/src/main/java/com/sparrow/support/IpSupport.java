package com.sparrow.support;

/**
 * @author harry
 */
public interface IpSupport {
    String getLocalIp();

    String getLocalHostName();

    Long toLong(String ip);

    String parse(Long ip);
}

package com.axing.points_ms.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.utils
 * @className: NetTools
 * @author: Axing
 * @description: TODO
 * @date: 2023/7/14 10:20
 * @version: 1.0
 */
public class NetTools {
    public static String getClientIPAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}


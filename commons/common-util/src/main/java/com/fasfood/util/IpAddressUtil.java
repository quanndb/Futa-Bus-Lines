package com.fasfood.util;

import com.fasfood.common.error.InternalServerError;
import com.fasfood.common.exception.ResponseException;

import jakarta.servlet.http.HttpServletRequest;

public class IpAddressUtil {

    public static String getClientIp(HttpServletRequest request) {
        try {
            String ip = request.getHeader("X-Forwarded-For");
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // Trong trường hợp có nhiều IP (proxy), lấy IP đầu tiên
                return ip.split(",")[0].trim();
            }

            ip = request.getHeader("Proxy-Client-IP");
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }

            ip = request.getHeader("WL-Proxy-Client-IP");
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }

            ip = request.getHeader("HTTP_CLIENT_IP");
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }

            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }

            return request.getRemoteAddr(); // fallback nếu không có header nào
        } catch (Exception e) {
            throw new ResponseException(InternalServerError.UNABLE_GET_IP);
        }
    }
}

package com.fasfood.util;

import com.fasfood.common.error.InternalServerError;
import com.fasfood.common.exception.ResponseException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class MacAddressUtil {
    public static String getMacAddress() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            if (network != null) {
                byte[] mac = network.getHardwareAddress();
                return formatMacAddress(mac);
            }
        } catch (Exception e) {
            throw new ResponseException(InternalServerError.UNABLE_GET_MAC);
        }
        return null;
    }

    public static String getAllMacAddresses() {
        StringBuilder macAddresses = new StringBuilder();
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while (networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                byte[] mac = network.getHardwareAddress();
                if (mac != null) {
                    macAddresses.append(network.getName())
                            .append(": ")
                            .append(formatMacAddress(mac))
                            .append("\n");
                }
            }
            if (!macAddresses.toString().isEmpty()) {
                return macAddresses.toString();
            }
            throw new RuntimeException();
        } catch (Exception e) {
            throw new ResponseException(InternalServerError.UNABLE_GET_MAC);
        }
    }

    private static String formatMacAddress(byte[] mac) {
        if (mac == null) return "Không có địa chỉ MAC";
        StringBuilder macAddress = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            macAddress.append(String.format("%02X", mac[i]));
            if (i < mac.length - 1) {
                macAddress.append("-");
            }
        }
        return macAddress.toString();
    }
}

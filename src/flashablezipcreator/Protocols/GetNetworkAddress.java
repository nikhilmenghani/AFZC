/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Protocols;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 *
 * @author Nikhil
 */
public class GetNetworkAddress {

    public static String GetAddress(String addressType) {
        String address = "";
        InetAddress lanIp = null;
        try {
            String ipAddress = null;
            Enumeration<NetworkInterface> net = null;
            net = NetworkInterface.getNetworkInterfaces();
            while (net.hasMoreElements()) {
                NetworkInterface element = net.nextElement();
                Enumeration<InetAddress> addresses = element.getInetAddresses();
                while (addresses.hasMoreElements() && !isVMMac(element.getHardwareAddress())) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address) {
                        if (ip.isSiteLocalAddress()) {
                            ipAddress = ip.getHostAddress();
                            lanIp = InetAddress.getByName(ipAddress);
                        }
                    }
                }
            }
            if (lanIp == null) {
                return null;
            }
            switch (addressType) {
                case "ip":
                    address = lanIp.toString().replaceAll("^/+", "");
                    break;
                case "mac":
                    address = getMacAddress(lanIp);
                    break;
                default:
                    throw new Exception("Specify \"ip\" or \"mac\"");
            }

        } catch (UnknownHostException ex) {
        } catch (SocketException ex) {
        } catch (Exception ex) {
        }
        return address;

    }

    private static String getMacAddress(InetAddress ip) {
        String address = null;
        try {
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            address = sb.toString();
        } catch (SocketException ex) {
        }
        return address;
    }

    private static boolean isVMMac(byte[] mac) {
        if (null == mac) {
            return false;
        }
        byte invalidMacs[][] = {
            {0x00, 0x05, 0x69}, //VMWare
            {0x00, 0x1C, 0x14}, //VMWare
            {0x00, 0x0C, 0x29}, //VMWare
            {0x00, 0x50, 0x56}, //VMWare
            {0x08, 0x00, 0x27}, //Virtualbox
            {0x0A, 0x00, 0x27}, //Virtualbox
            {0x00, 0x03, (byte) 0xFF}, //Virtual-PC
            {0x00, 0x15, 0x5D} //Hyper-V
        };
        for (byte[] invalid : invalidMacs) {
            if (invalid[0] == mac[0] && invalid[1] == mac[1] && invalid[2] == mac[2]) {
                return true;
            }
        }
        return false;
    }

    public static String getVMWareMacAddress() {
        InetAddress ip;
        StringBuilder sb = new StringBuilder();
        try {
            ip = InetAddress.getLocalHost();
            System.out.println("Current IP address : " + ip.getHostAddress());
            NetworkInterface.getNetworkInterfaces();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            System.out.print("Current MAC address : ");

            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            System.out.println(sb.toString());
        } catch (Exception e) {
        }
        return sb.toString();
    }
}

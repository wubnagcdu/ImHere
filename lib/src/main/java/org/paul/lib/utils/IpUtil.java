package org.paul.lib.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpUtil {
    private static final Pattern ipv_4 = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
    private static final Pattern ipv6_A = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
    private static final Pattern ipv6_B = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    public static boolean isIpv4(String ipv4)
    {
        return ipv_4.matcher(ipv4).matches();
    }

    public static boolean isIpv6_A(String ipv6a)
    {
        return ipv6_A.matcher(ipv6a).matches();
    }

    public static boolean isIpv6_B(String ipv6b)
    {
        return ipv6_B.matcher(ipv6b).matches();
    }

    public static boolean isIpv6(String ipv6)
    {
        return (isIpv6_A(ipv6)) || (isIpv6_B(ipv6));
    }
}

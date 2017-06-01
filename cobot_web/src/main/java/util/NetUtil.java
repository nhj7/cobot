package util;

import java.net.InetAddress;

public class NetUtil {
	public static String getLocalIp() throws Throwable{
		return InetAddress.getLocalHost().getHostAddress();

	}
}

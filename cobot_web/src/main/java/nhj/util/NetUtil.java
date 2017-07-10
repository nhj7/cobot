package nhj.util;

import java.net.InetAddress;

public class NetUtil {
	
	public static boolean isMyLocal() throws Throwable{
		return "172.30.1.7".equals(getLocalIp());
	}
	
	public static String getLocalIp() throws Throwable{
		return InetAddress.getLocalHost().getHostAddress();

	}
}

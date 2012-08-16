package com.ngt.jopenmetaverse.shared.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

public class PlatformUtils {
	public static String getPlatformOS() {
		String platform = "";
		if (isWindows()) {
			platform = "Win";
		} else if (isMac()) {
			platform = "Mac";			
		} else if (isUnix()) {
			platform = "Linux";
		} else if (isSolaris()) {
			platform = "Solaris";
		} else {
			platform = "";
		}

		return platform;
	}

	/*
	 * Return 
	 *  If success, Mac address of the current active interface
	 *  otherwise, emmpty string
	 */
	public static String getMACAddress()
	{
		StringBuilder sb = new StringBuilder();
		try{
		InetAddress ip;
		ip = InetAddress.getLocalHost();	 
		NetworkInterface network = NetworkInterface.getByInetAddress(ip);

		byte[] mac = network.getHardwareAddress();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
		}
		}
		catch(Exception e)
		{
		}
		return sb.toString();
	}

	public static boolean tryParseInetAddress(String ip,InetAddress[] result) 
	{
		try {
			result[0] = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			return false;
		}
		return true;
	}
	
	
	public static boolean isWindows() {

		String os = System.getProperty("os.name").toLowerCase();
		// windows
		return (os.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		String os = System.getProperty("os.name").toLowerCase();
		// Mac
		return (os.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {

		String os = System.getProperty("os.name").toLowerCase();
		// linux or unix
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);

	}

	public static boolean isSolaris() {

		String os = System.getProperty("os.name").toLowerCase();
		// Solaris
		return (os.indexOf("sunos") >= 0);

	}

	public static void sleep(int timeout)
	{
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			JLogger.info(e.getMessage());
		}
	}
	
}

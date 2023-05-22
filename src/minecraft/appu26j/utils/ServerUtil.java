package appu26j.utils;

import appu26j.interfaces.MinecraftInterface;

public class ServerUtil implements MinecraftInterface
{
	public static boolean isPlayerOnHypixel()
	{
		if (mc.getCurrentServerData() == null)
		{
			return false;
		}
		
		String IP = mc.getCurrentServerData().serverIP;
		return IP.toLowerCase().endsWith("hypixel.net") || IP.toLowerCase().endsWith("hypixel.io");
	}
}

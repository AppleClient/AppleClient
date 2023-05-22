package appu26j;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRichPresence.Builder;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;

public class DiscordRP
{
	private static volatile boolean running = true;
	private static long milliseconds = 0;
	
	public static void start()
	{
		milliseconds = System.currentTimeMillis();
		
		DiscordEventHandlers discordEventHandlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback()
		{
			@Override
			public void apply(DiscordUser discordUser)
			{
				update("Launching Apple Client");
			}
		}).build();
		
		DiscordRPC.discordInitialize("1096373593597812756", discordEventHandlers, true);
		
		new Thread(() ->
		{
			while (running)
			{
				DiscordRPC.discordRunCallbacks();
			}
		}).start();
	}
	
	public static void shutdown()
	{
		running = false;
		DiscordRPC.discordShutdown();
	}
	
	public static void update(String line)
	{
		Builder builder = new Builder(line);
		builder.setBigImage("appleclient", "Apple Client");
		builder.setStartTimestamps(milliseconds);
		DiscordRPC.discordUpdatePresence(builder.build());
	}
}

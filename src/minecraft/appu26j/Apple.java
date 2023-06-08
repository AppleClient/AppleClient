package appu26j;

import java.io.File;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import appu26j.config.Config;
import appu26j.events.mc.EventKey;
import appu26j.gui.DragGUI;
import appu26j.interfaces.MinecraftInterface;
import appu26j.mods.ModsManager;
import appu26j.settings.SettingsManager;
import appu26j.utils.UpdateUtil;

public enum Apple implements MinecraftInterface
{
	CLIENT;
	
	public static final File DEFAULT_DIRECTORY = new File(System.getProperty("user.home"), "appleclient"), CONFIG = new File(DEFAULT_DIRECTORY, "config.json");
	public static final String VERSION = "1.93", TITLE = "Apple Client " + VERSION;
	private AppleClientVersionChecker appleClientVersionChecker;
	private SettingsManager settingsManager;
	private ModsManager modsManager;
	private EventBus eventBus;
	private DragGUI dragGUI;
	private Config config;
	
	static
	{
		if (!DEFAULT_DIRECTORY.exists())
		{
			DEFAULT_DIRECTORY.mkdirs();
		}
	}
	
	public void initialize()
	{
		this.appleClientVersionChecker = new AppleClientVersionChecker();
		this.settingsManager = new SettingsManager().initialize();
        this.eventBus = new EventBus("Apple Client Event Bus");
		this.modsManager = new ModsManager().initialize();
		this.dragGUI = new DragGUI();
		this.config = new Config();
		
		BuiltInResourcePackDownloader.downloadPack();
		this.appleClientVersionChecker.run();
		this.eventBus.register(this);
        UpdateUtil.addHook();
	}
	
	@Subscribe
	public void onKey(EventKey e)
	{
		if (e.getKey() == Keyboard.KEY_RSHIFT)
		{
			this.mc.displayGuiScreen(this.dragGUI.initialize());
		}
	}
	
	public AppleClientVersionChecker getVersionChecker()
	{
		return this.appleClientVersionChecker;
	}
	
	public SettingsManager getSettingsManager()
	{
		return this.settingsManager;
	}
	
	public ModsManager getModsManager()
	{
		return this.modsManager;
	}
	
	public EventBus getEventBus()
	{
		return this.eventBus;
	}
	
	public DragGUI getDragGUI()
	{
		return this.dragGUI;
	}
	
	public Config getConfig()
	{
		return this.config;
	}
	
	public void shutdown()
	{
		this.eventBus.unregister(this);
	}
	
	public ArrayList<String> getSpecialPeople()
	{
	    ArrayList<String> specialPeople = new ArrayList<>();
	    specialPeople.add("nonYoutuber");
	    return specialPeople;
	}
}

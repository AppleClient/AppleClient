package appu26j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import appu26j.config.Config;
import appu26j.events.entity.EventTick;
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
	public static final String VERSION = "1.96", TITLE = "Apple Client " + VERSION;
    private ArrayList<String> usersPlayingAppleClient = new ArrayList<>();
	private AppleClientVersionChecker appleClientVersionChecker;
    private long time = System.currentTimeMillis();
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
		
        new Thread(() ->
        {
            HttpURLConnection httpURLConnection = null;
            
            try
            {
                httpURLConnection = (HttpURLConnection) new URL("http://217.160.192.85:10023/adduuid/?uuid=" + this.mc.getSession().getPlayerID()).openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();
                httpURLConnection.getInputStream();
            }
            
            catch (Exception exception)
            {
                ;
            }
            
            finally
            {
                if (httpURLConnection != null)
                {
                    httpURLConnection.disconnect();
                }
            }
        }).start();
		
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
	
	@Subscribe
	public void onTick(EventTick e)
	{
	    if ((this.time + 30000) < System.currentTimeMillis())
	    {
	        new Thread(() ->
	        {
	            HttpURLConnection httpURLConnection = null;
	            InputStreamReader inputStreamReader = null;
	            BufferedReader bufferedReader = null;
	            
	            try
	            {
                    this.usersPlayingAppleClient.clear();
	                httpURLConnection = (HttpURLConnection) new URL("http://217.160.192.85:10023/uuidlist").openConnection();
	                httpURLConnection.setDoInput(true);
	                httpURLConnection.setDoOutput(true);
	                httpURLConnection.connect();
	                inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
	                bufferedReader = new BufferedReader(inputStreamReader);
	                String line;
	                
	                while ((line = bufferedReader.readLine()) != null)
	                {
	                    if (!line.isEmpty())
	                    {
	                        this.usersPlayingAppleClient.add(line);
	                    }
	                }
	            }
	            
	            catch (Exception exception)
	            {
	                ;
	            }
	            
	            finally
	            {
	                if (bufferedReader != null)
	                {
	                    try
	                    {
	                        bufferedReader.close();
	                    }
	                    
	                    catch (Exception exception)
	                    {
	                        ;
	                    }
	                }
	                
	                if (inputStreamReader != null)
                    {
                        try
                        {
                            inputStreamReader.close();
                        }
                        
                        catch (Exception exception)
                        {
                            ;
                        }
                    }
	                
	                if (httpURLConnection != null)
                    {
                        httpURLConnection.disconnect();
                    }
	            }
	        }).start();
	        
	        this.time = System.currentTimeMillis();
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
	    specialPeople.add("eaa6e69a-966b-465d-a911-4cae0bf494401");
        specialPeople.add("cb6b6dd4-01d3-45ab-bbe6-46670c674b7b");
        specialPeople.add("3ef63c89-142c-4aed-9e28-52e8d717591c");
        specialPeople.add("94f73882-b359-4b9a-ab03-c37b0f698be7");
	    return specialPeople;
	}
	
	public ArrayList<String> getPeopleUsingAppleClient()
    {
	    return this.usersPlayingAppleClient;
    }
}

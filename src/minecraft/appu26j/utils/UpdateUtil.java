package appu26j.utils;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class UpdateUtil
{
    private static File updater = new File("updater.jar");
    
    public static void addHook()
    {
        checkUpdaterJar();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            try
            {
                Runtime.getRuntime().exec("java -jar updater.jar");
            }
            
            catch (Exception e)
            {
                ;
            }
        }));
    }
    
    private static void checkUpdaterJar()
    {
        if (!updater.exists())
        {
            try
            {
                updater.createNewFile();
                FileUtils.copyURLToFile(new URL("https://github.com/AppleClient/Updater/releases/download/Updater/updater.jar"), updater);
            }
            
            catch (Exception e)
            {
                ;
            }
        }
    }
}

package appu26j;

import appu26j.interfaces.MinecraftInterface;
import net.minecraft.client.gui.ScaledResolution; import appu26j.Scale;

// Class to cache ScaledResolution :D
public class Scale implements MinecraftInterface
{
    private static ScaledResolution scaledResolution;
    
    static
    {
        scaledResolution = new ScaledResolution(mc);
    }
    
    public static ScaledResolution getSR()
    {
        return scaledResolution;
    }
    
    public static void setSR(ScaledResolution scaledResolution)
    {
        Scale.scaledResolution = scaledResolution;
    }
}

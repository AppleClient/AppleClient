package appu26j.utils;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import appu26j.fontrenderer.CustomFontRenderer;
import appu26j.interfaces.MinecraftInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution; import appu26j.Scale;
import net.minecraft.util.ResourceLocation;

public class Fonts implements MinecraftInterface
{
    public static CustomFontRenderer NORMAL;
    private static volatile int completed;
    private static Font NORMAL_;
    
    private static Font getFont(final Map<String, Font> locationMap, final String location, final int size) {
        Font font = null;
        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(0, (float)size);
            }
            else {
                final InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(location)).getInputStream();
                font = Font.createFont(0, is);
                locationMap.put(location, font);
                font = font.deriveFont(0, (float)size);
            }
        }
        catch (Exception e) {
            font = new Font("default", 0, 9);
        }
        return font;
    }
    
    private static boolean hasLoaded() {
        return completed >= 3;
    }
    
    public static void initialize() {
        completed = 0;
        
        new Thread(() -> {
            Map<String, Font> locationMap = new HashMap<>();
            NORMAL_ = getFont(locationMap, "font.ttf", 9 * (Scale.getSR()).getScaleFactor());
            ++completed;
            return;
        }).start();
        new Thread(() -> {
            Map<String, Font> locationMap = new HashMap<>();
            ++completed;
        }).start();
        new Thread(() -> {
            Map<String, Font> locationMap = new HashMap<>();
            ++completed;
        }).start();
        while (!hasLoaded()) {
            try {
                Thread.sleep(5L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        NORMAL = new CustomFontRenderer(NORMAL_, true, true);
    }
}
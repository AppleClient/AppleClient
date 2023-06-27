package appu26j.interfaces;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import appu26j.Apple;
import appu26j.gui.ClickGUI;
import appu26j.gui.DragGUI;
import appu26j.gui.MusicPlayerGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution; import appu26j.Scale;

public interface GUIInterface
{
	public int backgroundColour = new Color(45, 45, 60).getRGB(), backgroundColourDarkened = new Color(25, 25, 40).getRGB(), backgroundColourLightened = new Color(65, 65, 80).getRGB();
	
	default void scissor(float x, float y, float width, float height)
	{
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution scaledResolution = Scale.getSR();
		x *= scaledResolution.getScaleFactor();
		y *= scaledResolution.getScaleFactor();
		width *= scaledResolution.getScaleFactor();
		height *= scaledResolution.getScaleFactor();
		
		if (Minecraft.getMinecraft().currentScreen instanceof ClickGUI || Minecraft.getMinecraft().currentScreen instanceof DragGUI || Minecraft.getMinecraft().currentScreen instanceof MusicPlayerGUI)
		{
			float zoomFactor =  Apple.CLIENT.getDragGUI().clickGUI.zoomFactor;
			x *= zoomFactor;
			y *= zoomFactor;
			width *= zoomFactor;
			height *= zoomFactor;
		}
		
		GL11.glScissor((int) x, (int) (mc.displayHeight - height), (int) (width - x), (int) (height - y));
	}
	
	default void scissor(float x, float y, float width, float height, float size)
	{
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution scaledResolution = Scale.getSR();
		x *= scaledResolution.getScaleFactor();
		y *= scaledResolution.getScaleFactor();
		width *= scaledResolution.getScaleFactor();
		height *= scaledResolution.getScaleFactor();
		
		if (Minecraft.getMinecraft().currentScreen instanceof ClickGUI || Minecraft.getMinecraft().currentScreen instanceof DragGUI || Minecraft.getMinecraft().currentScreen instanceof MusicPlayerGUI)
		{
			float zoomFactor =  Apple.CLIENT.getDragGUI().clickGUI.zoomFactor;
			x *= zoomFactor;
			y *= zoomFactor;
			width *= zoomFactor;
			height *= zoomFactor;
		}
		
		x *= size;
		y *= size;
		width *= size;
		height *= size;
		GL11.glScissor((int) x, (int) (mc.displayHeight - height), (int) (width - x), (int) (height - y));
	}
}

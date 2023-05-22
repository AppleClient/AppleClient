package club.marshadow;

import java.awt.Color;

public class ColorUtil
{
	private static final String author = "marshadow#2336";
	
	public static int getRainbowColor()
	{
		return Color.getHSBColor(((System.currentTimeMillis() / 10) % 255) / 255F, 0.5F, 1).getRGB();
	}
}

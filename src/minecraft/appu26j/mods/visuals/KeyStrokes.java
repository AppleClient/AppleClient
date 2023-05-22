package appu26j.mods.visuals;

import java.awt.Color;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import club.marshadow.ColorUtil;
import net.minecraft.client.renderer.GlStateManager;

@ModInterface(name = "Key Strokes", description = "Displays the keystrokes on your screen.", category = Category.VISUALS, width = 80, height = 80)
public class KeyStrokes extends Mod
{
	public KeyStrokes()
	{
		this.addSetting(new Setting("Background Transparency", this, false));
		this.addSetting(new Setting("Text Color (RGB)", this, new int[]{255, 255, 255}));
		this.addSetting(new Setting("Rainbow Text Color", this, false));
	}
	
	@Override
	public void onRender()
	{
		super.onRender();
		int[] colors = this.getSetting("Text Color (RGB)").getColors();
		int color = new Color(colors[0], colors[1], colors[2]).getRGB();
		
		if (this.getSetting("Rainbow Text Color").getCheckBoxValue())
		{
			color = ColorUtil.getRainbowColor();
		}
		
		this.drawRect(this.x + (this.width / 2) - 13, this.y, this.x + (this.width / 2) + 13, this.y + 26, this.mc.gameSettings.isKeyDown(this.mc.gameSettings.keyBindForward) ? this.backgroundColourDarkened : this.backgroundColour);
		this.drawString("W", (this.x + (this.width / 2)) - (this.getStringWidth("W", 12) / 2), this.y + 8, 12, color);
		this.drawRect(this.x, this.y + 27, this.x + (this.width / 2) - 14, this.y + 53, this.mc.gameSettings.isKeyDown(this.mc.gameSettings.keyBindLeft) ? this.backgroundColourDarkened : this.backgroundColour);
		this.drawString("A", (this.x + 13) - (this.getStringWidth("A", 12) / 2), this.y + 35, 12, color);
		this.drawRect(this.x + (this.width / 2) - 13, this.y + 27, this.x + (this.width / 2) + 13, this.y + 53, this.mc.gameSettings.isKeyDown(this.mc.gameSettings.keyBindBack) ? this.backgroundColourDarkened : this.backgroundColour);
		this.drawString("S", (this.x + (this.width / 2)) - (this.getStringWidth("S", 12) / 2), this.y + 35, 12, color);
		this.drawRect(this.x + (this.width / 2) + 14, this.y + 27, this.x + this.width, this.y + 53, this.mc.gameSettings.isKeyDown(this.mc.gameSettings.keyBindRight) ? this.backgroundColourDarkened : this.backgroundColour);
		this.drawString("D", ((this.x + this.width) - 13) - (this.getStringWidth("D", 12) / 2), this.y + 35, 12, color);
		this.drawRect(this.x, this.y + 54, this.x + this.width, this.y + 80, this.mc.gameSettings.isKeyDown(this.mc.gameSettings.keyBindJump) ? this.backgroundColourDarkened : this.backgroundColour);
		this.drawString("SPACE", (this.x + (this.width / 2)) - (this.getStringWidth("SPACE", 12) / 2), this.y + this.height - 18, 12, color);
	}
}

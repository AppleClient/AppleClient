package appu26j.mods.visuals;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;

@ModInterface(name = "MC Chat", description = "Allows you to change the behaviour of the chat.", category = Category.VISUALS)
public class Chat extends Mod
{
	public Chat()
	{
		this.addSetting(new Setting("Infinite History", this, false));
		this.addSetting(new Setting("Text Shadow", this, true));
	}
}

package appu26j.mods.visuals;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;

@ModInterface(name = "Potion Settings", description = "Allows you to customize potions.", category = Category.VISUALS)
public class PotionSettings extends Mod
{
    public PotionSettings()
    {
        this.addSetting(new Setting("Hide own potion effect particles", this, false));
    }
}

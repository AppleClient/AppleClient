package appu26j.mods.visuals;

import com.google.common.eventbus.Subscribe;

import appu26j.events.entity.EventTick;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;

@ModInterface(name = "Performance", description = "Improves FPS.", category = Category.VISUALS)
public class Performance extends Mod
{
    @Subscribe
    public void onTick(EventTick e)
    {
        ;
    }
}

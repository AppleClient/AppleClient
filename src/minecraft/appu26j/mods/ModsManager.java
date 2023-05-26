package appu26j.mods;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import appu26j.mods.general.*;
import appu26j.mods.multiplayer.*;
import appu26j.mods.visuals.*;

public class ModsManager
{
	private ArrayList<Mod> mods = new ArrayList<>();
	
	public ModsManager initialize()
	{
		// General
		this.mods.add(new ToggleSprint());
		this.mods.add(new ClockTimer());
		
		// Multiplayer
		this.mods.add(new AutoGG());
		this.mods.add(new AutoTip());
		this.mods.add(new SnipeSafe());
		this.mods.add(new Cooldown());
		
		// Visuals
		this.mods.add(new Visuals());
		this.mods.add(new FullBright());
		this.mods.add(new PingIndicator());
		this.mods.add(new DamageTilt());
		this.mods.add(new CrossHair());
		this.mods.add(new TimeChanger());
		this.mods.add(new Chat());
		this.mods.add(new Scoreboard());
		this.mods.add(new FPS());
		this.mods.add(new TimeClock());
		this.mods.add(new NameTags());
		this.mods.add(new PackDisplay());
		this.mods.add(new HurtCamera());
		this.mods.add(new DamageTint());
		this.mods.add(new NoBobbing());
		this.mods.add(new NoPumpkin());
		this.mods.add(new KeyStrokes());
		this.mods.add(new CPS());
		this.mods.add(new Combo());
		this.mods.add(new ReachDisplay());
		this.mods.add(new BetterZoom());
		this.mods.add(new ArmorStatus());
		this.mods.sort(Comparator.comparing(Mod::getName));
		this.getMod("Score Board").setEnabled(true);
		return this;
	}
	
	public ArrayList<Mod> getMods()
	{
		return this.mods;
	}
	
	public ArrayList<Mod> getMods(Category category)
	{
		return this.mods.stream().filter(mod -> mod.getCategory().equals(category)).collect(Collectors.toCollection(ArrayList::new));
	}
	
	public Mod getMod(String name)
	{
		return this.mods.stream().filter(mod -> mod.getName().equals(name)).findFirst().orElse(null);
	}
}

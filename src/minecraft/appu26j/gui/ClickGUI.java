package appu26j.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import appu26j.Apple;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import appu26j.utils.SoundUtil;
import appu26j.utils.TimeUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ClickGUI extends GuiScreen
{
	private boolean searching = false, aBoolean = false, closingGui = false;
	private String randomModName = "", searchingMessage = "";
	private Category selectedCategory = Category.ALL;
	private int scrollIndex = 10, scrollIndex2 = -10;
	private TimeUtil timeUtil = new TimeUtil();
	public float zoomFactor = 1, index = 0;
	private Mod selectedMod;
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
		if (this.closingGui)
		{
			if (this.index > 0)
			{
				this.index -= 0.1F;
				
				if (this.index < 0)
				{
					this.index = 0;
				}
				
				if (this.index == 0)
				{
					Keyboard.enableRepeatEvents(false);
					this.mc.displayGuiScreen(Apple.CLIENT.getDragGUI());
					this.closingGui = false;
				}
			}
		}
		
		else
		{
			if (this.index < 1)
			{
				this.index += 0.1F;
				
				if (this.index > 1)
				{
					this.index = 1;
				}
			}
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		GlStateManager.pushMatrix();
		GlStateManager.scale(this.zoomFactor, this.zoomFactor, this.zoomFactor);
		float i = this.width / 2;
		float j = this.height / 2;
		i /= this.zoomFactor;
		j /= this.zoomFactor;
		mouseX /= this.zoomFactor;
		mouseY /= this.zoomFactor;
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.85F + (this.index * 0.15F), 0.85F + (this.index * 0.15F), 0.85F + (this.index * 0.15F));
		i /= 0.85F + (this.index * 0.15F);
		j /= 0.85F + (this.index * 0.15F);
		mouseX /= 0.85F + (this.index * 0.15F);
		mouseY /= 0.85F + (this.index * 0.15F);
		Color temp1 = new Color(this.backgroundColourDarkened, true);
        Color temp2 = new Color(this.backgroundColourLightened, true);
        Color temp3 = new Color(this.backgroundColour, true);
        Color backgroundColourDarkened = new Color(temp1.getRed(), temp1.getGreen(), temp1.getBlue(), (int) (this.index * 200));
        Color backgroundColourLightened = new Color(temp2.getRed(), temp2.getGreen(), temp2.getBlue(), (int) (this.index * 200));
        Color backgroundColour = new Color(temp3.getRed(), temp3.getGreen(), temp3.getBlue(), (int) (this.index * 200));
        this.drawBackground(this.width / this.zoomFactor / 0.75F + (this.index * 0.25F), this.height / this.zoomFactor / 0.75F + (this.index * 0.25F));
		this.drawRect(i - 200, j - 140, i + 200, j + 140, backgroundColourDarkened.getRGB());
        backgroundColourDarkened = new Color(temp1.getRed(), temp1.getGreen(), temp1.getBlue(), (int) (this.index * 200));
        backgroundColourLightened = new Color(temp2.getRed(), temp2.getGreen(), temp2.getBlue(), (int) (this.index * 200));
        backgroundColour = new Color(temp3.getRed(), temp3.getGreen(), temp3.getBlue(), (int) (this.index * 200));
        
		if (this.selectedMod == null)
		{
			int xOffset = 0, yOffset = this.scrollIndex, categoryOffset = 0;
			j -= 20;
            i += 5;
			
			for (Category category : Category.values())
			{
				float f = i - 170;
				
				if (!this.searching && this.isInsideBox(mouseX, mouseY, f + categoryOffset, j - 100, (f + this.getStringWidth(category.name(), 8)) + 10 + categoryOffset, j - 80))
				{
				    this.drawRect(f + categoryOffset, j - 100, (f + this.getStringWidth(category.name(), 8)) + 10 + categoryOffset, j - 80, backgroundColour.getRGB());
				}
				
				else
				{
				    this.drawRect(f + categoryOffset, j - 100, (f + this.getStringWidth(category.name(), 8)) + 10 + categoryOffset, j - 80, backgroundColourLightened.getRGB());
				}
				
				this.drawStringAlpha((this.selectedCategory.equals(category) ? "�n" : "") + category.name(), f + 5 + categoryOffset, j - 93.5F, -1, (int) (this.index * 255));
				categoryOffset += this.getStringWidth(category.name(), 8) + 15;
			}
			
			if (!this.searching && this.isInsideBox(mouseX, mouseY, i + 55, j - 100, i + 160, j - 80))
			{
			    this.drawRect(i + 55, j - 100, i + 160, j - 80, backgroundColour.getRGB());
			}
			
			else
			{
			    this.drawRect(i + 55, j - 100, i + 160, j - 80, backgroundColourLightened.getRGB());
			}
			
			this.drawStringAlpha(this.randomModName + "...", i + 60, j - 94, new Color(135, 135, 135).getRGB(), (int) (this.index * 255));
			GlStateManager.color(1, 1, 1, this.index);
			GlStateManager.enableBlend();
			this.mc.getTextureManager().bindTexture(new ResourceLocation("icons/search.png"));
			this.drawScaledCustomSizeModalRect(i + 140, j - 99, 0, 0, 16, 16, 16, 16, 16, 16);
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			this.scissor(i - 200, j - 70, i + 200, j + 150, 0.85F + (this.index * 0.15F));
			
			for (Mod mod : this.selectedCategory.equals(Category.ALL) ? Apple.CLIENT.getModsManager().getMods() : Apple.CLIENT.getModsManager().getMods(this.selectedCategory))
			{
				if (xOffset != 0 && xOffset % 3 == 0)
				{
					yOffset += 115;
					xOffset = 0;
				}
				
				String[] names = mod.getName().split(" ");
				
				if (!this.searching && this.isInsideBox(mouseX, mouseY, i - 200, j - 70, i + 200, j + 150) && this.isInsideBox(mouseX, mouseY, (i - 170) + xOffset, (j - 80) + yOffset, (i - 170) + xOffset + 100, (j - 80) + yOffset + 100))
				{
				    this.drawRect((i - 170) + xOffset, (j - 80) + yOffset, (i - 170) + xOffset + 100, (j - 80) + yOffset + 100, backgroundColour.getRGB());
				}
				
				else
				{
				    this.drawRect((i - 170) + xOffset, (j - 80) + yOffset, (i - 170) + xOffset + 100, (j - 80) + yOffset + 100, backgroundColourLightened.getRGB());
				}
				
				this.drawStringAlpha(names[0], (i - 120) - (this.getStringWidth(names[0], 12) / 2) + xOffset, (j - 60) + yOffset, 12, -1, (int) (this.index * 255));
				this.drawStringAlpha(names[1], (i - 120) - (this.getStringWidth(names[1], 12) / 2) + xOffset, (j - 40) + yOffset, 12, -1, (int) (this.index * 255));
				
				if (!this.searching && this.isInsideBox(mouseX, mouseY, i - 200, j - 70, i + 200, j + 150) && this.isInsideBox(mouseX, mouseY, (i - 170) + xOffset, (j - 80) + yOffset, (i - 170) + xOffset + 100, (j - 80) + yOffset + 100))
				{
				    if (mod.isEnabled())
	                {
				        this.drawRect((i - 170) + xOffset, (j - 5) + yOffset, (i - 170) + xOffset + 100, (j - 80) + yOffset + 100, new Color(0, 200, 75, (int) (this.index * 255)).darker().getRGB());
	                }
	                
	                else
	                {
	                    this.drawRect((i - 170) + xOffset, (j - 5) + yOffset, (i - 170) + xOffset + 100, (j - 80) + yOffset + 100, new Color(180, 0, 0, (int) (this.index * 255)).getRGB());
	                }
				}
				
				else
				{
				    if (mod.isEnabled())
                    {
				        this.drawRect((i - 170) + xOffset, (j - 5) + yOffset, (i - 170) + xOffset + 100, (j - 80) + yOffset + 100, new Color(0, 200, 75, (int) (this.index * 255)).getRGB());
	                }
				    
				    else
				    {
				        this.drawRect((i - 170) + xOffset, (j - 5) + yOffset, (i - 170) + xOffset + 100, (j - 80) + yOffset + 100, new Color(255, 25, 50, (int) (this.index * 255)).getRGB());
				    }
				}
				
				String text = mod.isEnabled() ? "ENABLED" : "DISABLED";
				this.drawStringAlpha(text, (i - 120) - (this.getStringWidth(text, 12) / 2) + xOffset, (j + 2) + yOffset, 12, new Color(254, 254, 254).getRGB(), (int) (this.index * 255));
				xOffset += 115;
			}
			
			j += 20;
            i -= 5;
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
		
		else
		{
		    this.drawStringAlpha(this.selectedMod.getName(), i - (this.getStringWidth(this.selectedMod.getName(), 16) / 2), j - 130, 16, -1, (int) (this.index * 255));
			this.drawStringAlpha(this.selectedMod.getDescription(), i - (this.getStringWidth(this.selectedMod.getDescription(), 8) / 2), j - 110, -1, (int) (this.index * 255));
			int offset = 0;
            j -= 15;
			
			for (Setting setting : this.selectedMod.getSettings())
			{
				if (setting.getTypeOfSetting().equals("Check Box"))
				{
					this.drawRect(i - 180, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName()) + 25, (j - 60) + offset, this.isInsideBox(mouseX, mouseY, i - 180, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName()) + 25, (j - 60) + offset) ? this.backgroundColour : this.backgroundColourLightened, (int) (this.index * 255));
					this.drawRect((i - 180) + this.getStringWidth(setting.getName()) + 9, (j - 75) + offset, (i - 180) + this.getStringWidth(setting.getName()) + 19, (j - 65) + offset, setting.getCheckBoxValue() ? new Color(0, 225, 100).getRGB() : this.isInsideBox(mouseX, mouseY, i - 180, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName()) + 25, (j - 60) + offset) ? this.backgroundColourLightened : this.backgroundColour, (int) (this.index * 255));
					this.drawStringAlpha(setting.getName(), i - 175, (j - 74) + offset, -1, (int) (this.index * 255));
					offset += 25;
				}
				
				if (setting.getTypeOfSetting().equals("Text Box"))
				{
					if (setting.isFocused())
					{
						if (this.timeUtil.hasTimePassed(500))
						{
							setting.setBoolean(!setting.getBoolean());
							this.timeUtil.reset();
						}
					}
					
					else
					{
						setting.setBoolean(false);
					}
					
					this.drawRect(i - 180, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ": " + setting.getTextBoxValue()) + 9, (j - 60) + offset, this.isInsideBox(mouseX, mouseY, i - 180, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ": " + setting.getTextBoxValue()) + 9, (j - 60) + offset) ? this.backgroundColour : this.backgroundColourLightened, (int) (this.index * 255));
					this.drawStringAlpha(setting.getName() + ": " + setting.getTextBoxValue() + (setting.getBoolean() ? "|" : ""), i - 175, (j - 74) + offset, -1, (int) (this.index * 255));
					offset += 25;
				}
				
				if (setting.getTypeOfSetting().equals("Mode"))
				{
					this.drawRect(i - 180, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ": " + setting.getModeValue()) + 9, (j - 60) + offset, this.isInsideBox(mouseX, mouseY, i - 180, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ": " + setting.getModeValue()) + 9, (j - 60) + offset) ? this.backgroundColour : this.backgroundColourLightened, (int) (this.index * 255));
					this.drawStringAlpha(setting.getName() + ": " + setting.getModeValue(), i - 175, (j - 74) + offset, -1, (int) (this.index * 255));
					offset += 25;
				}
				
				if (setting.getTypeOfSetting().equals("Color Box"))
				{
					this.drawRect(i - 180, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ":  " + setting.getColors()[0] + ", " + setting.getColors()[1] + ", " + setting.getColors()[2]) + 9, (j - 60) + offset, this.isInsideBox(mouseX, mouseY, i - 180, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ":  " + setting.getColors()[0] + ", " + setting.getColors()[1] + ", " + setting.getColors()[2]) + 9, (j - 60) + offset) ? this.backgroundColour : this.backgroundColourLightened, (int) (this.index * 255));
					this.drawStringAlpha(setting.getName() + ":  " + setting.getColors()[0] + ", " + setting.getColors()[1] + ", " + setting.getColors()[2], i - 175, (j - 74) + offset, -1, (int) (this.index * 255));
					this.drawRect(i - 180 + this.getStringWidth(setting.getName() + ": ") + 4, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ":  " + setting.getColors()[0] + ", " + setting.getColors()[1] + ", " + setting.getColors()[2]) + 9, (j - 60) + offset, new Color(setting.getColors()[0], setting.getColors()[1], setting.getColors()[2]).getRGB(), (int) (this.index * 255));
					this.drawStringAlpha(setting.getColors()[0] + ", " + setting.getColors()[1] + ", " + setting.getColors()[2], i - 175 + this.getStringWidth(setting.getName() + ":  "), (j - 74) + offset, new Color(255 - setting.getColors()[0], 255 - setting.getColors()[1], 255 - setting.getColors()[2]).getRGB(), (int) (this.index * 255));
					offset += 25;
				}
				
				if (setting.getTypeOfSetting().equals("Slider"))
				{
					this.drawRect(i - 180, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ": " + setting.getSliderValue()) + 9, (j - 60) + offset, this.isInsideBox(mouseX, mouseY, i - 180, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ": " + setting.getSliderValue()) + 9, (j - 60) + offset) ? this.backgroundColour: this.backgroundColourLightened, (int) (this.index * 255));
					this.drawStringAlpha(setting.getName() + ": " + setting.getSliderValue(), i - 175, (j - 74) + offset, -1, (int) (this.index * 255));
					offset += 25;
				}
			}
			
            j += 15;
		}
		
		if (this.searching)
		{
	        backgroundColourDarkened = new Color(temp1.getRed(), temp1.getGreen(), temp1.getBlue(), (int) (this.index * 255));
	        backgroundColourLightened = new Color(temp2.getRed(), temp2.getGreen(), temp2.getBlue(), (int) (this.index * 255));
	        backgroundColour = new Color(temp3.getRed(), temp3.getGreen(), temp3.getBlue(), (int) (this.index * 255));
	        
			if (this.timeUtil.hasTimePassed(500))
			{
				this.aBoolean = !this.aBoolean;
				this.timeUtil.reset();
			}
			
			int xOffset = 0, yOffset = this.scrollIndex2 - 1;
			this.drawRect(0, 0, this.width / this.zoomFactor / (0.75F + (this.index * 0.25F)), this.height / this.zoomFactor / (0.75F + (this.index * 0.25F)), new Color(0, 0, 0, 200).getRGB(), (int) (this.index * 200));
			
			if (this.isInsideBox(mouseX, mouseY, i - 100, 20, i + 100, 50))
			{
			    this.drawRect(i - 100, 20, i + 100, 50, backgroundColour.getRGB());
			}
			
			else
			{
			    this.drawRect(i - 100, 20, i + 100, 50, backgroundColourLightened.getRGB());
			}
			
			this.drawStringAlpha(this.searchingMessage + (this.aBoolean ? "|" : ""), i - 95, 27, 16, -1, (int) (this.index * 255));
			GlStateManager.color(1, 1, 1, this.index);
			GlStateManager.enableBlend();
			this.mc.getTextureManager().bindTexture(new ResourceLocation("icons/search.png"));
			this.drawScaledCustomSizeModalRect(i + 70, 20, 0, 0, 28, 28, 28, 28, 28, 28);
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			this.scissor(0, 69, this.width / this.zoomFactor, this.height / this.zoomFactor, 0.85F + (this.index * 0.15F));
            i += 5;
            
			for (Mod mod : this.searchingMessage.isEmpty() ? Apple.CLIENT.getModsManager().getMods() : Apple.CLIENT.getModsManager().getMods().stream().filter(mod -> mod.getName().toLowerCase().contains(this.searchingMessage.toLowerCase())).collect(Collectors.toCollection(ArrayList::new)))
			{
	            j = 160;
                
                if (xOffset != 0 && xOffset % 3 == 0)
                {
                    yOffset += 115;
                    xOffset = 0;
                }
                
                String[] names = mod.getName().split(" ");
                
                if (this.isInsideBox(mouseX, mouseY, (i - 170) + xOffset, (j - 80) + yOffset, (i - 170) + xOffset + 100, (j - 80) + yOffset + 100))
                {
                    this.drawRect((i - 170) + xOffset, (j - 80) + yOffset, (i - 170) + xOffset + 100, (j - 80) + yOffset + 100, backgroundColour.getRGB());
                }
                
                else
                {
                    this.drawRect((i - 170) + xOffset, (j - 80) + yOffset, (i - 170) + xOffset + 100, (j - 80) + yOffset + 100, backgroundColourLightened.getRGB());
                }
                
                this.drawStringAlpha(names[0], (i - 120) - (this.getStringWidth(names[0], 12) / 2) + xOffset, (j - 60) + yOffset, 12, -1, (int) (this.index * 255));
                this.drawStringAlpha(names[1], (i - 120) - (this.getStringWidth(names[1], 12) / 2) + xOffset, (j - 40) + yOffset, 12, -1, (int) (this.index * 255));
                
                if (this.isInsideBox(mouseX, mouseY, (i - 170) + xOffset, (j - 80) + yOffset, (i - 170) + xOffset + 100, (j - 80) + yOffset + 100))
                {
                    if (mod.isEnabled())
                    {
                        this.drawRect((i - 170) + xOffset, (j - 5) + yOffset, (i - 170) + xOffset + 100, (j - 80) + yOffset + 100, new Color(0, 200, 75, (int) (this.index * 255)).darker().getRGB());
                    }
                    
                    else
                    {
                        this.drawRect((i - 170) + xOffset, (j - 5) + yOffset, (i - 170) + xOffset + 100, (j - 80) + yOffset + 100, new Color(180, 0, 0, (int) (this.index * 255)).getRGB());
                    }
                }
                
                else
                {
                    if (mod.isEnabled())
                    {
                        this.drawRect((i - 170) + xOffset, (j - 5) + yOffset, (i - 170) + xOffset + 100, (j - 80) + yOffset + 100, new Color(0, 200, 75, (int) (this.index * 255)).getRGB());
                    }
                    
                    else
                    {
                        this.drawRect((i - 170) + xOffset, (j - 5) + yOffset, (i - 170) + xOffset + 100, (j - 80) + yOffset + 100, new Color(255, 25, 50, (int) (this.index * 255)).getRGB());
                    }
                }
                
                String text = mod.isEnabled() ? "ENABLED" : "DISABLED";
                this.drawStringAlpha(text, (i - 120) - (this.getStringWidth(text, 12) / 2) + xOffset, (j + 2) + yOffset, 12, new Color(254, 254, 254).getRGB(), (int) (this.index * 255));
                xOffset += 115;
			}
			
			i -= 5;
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
		
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
    }
	
	@Override
	public void handleInput() throws IOException
    {
		super.handleInput();
		int ii = Integer.compare(0, Mouse.getDWheel());
		
		if (this.searching)
		{
			if (ii != 0)
			{
				if (ii < 0)
				{
					this.scrollIndex2 += 50;
					
					if (this.scrollIndex2 > -10)
					{
						this.scrollIndex2 = -10;
					}
				}
				
				else
				{
					this.scrollIndex2 -= 50;
				}
			}
		}
		
		else
		{
			if (this.selectedMod == null)
			{
				if (ii != 0)
				{
					if (ii < 0)
					{
						this.scrollIndex += 50;
						
						if (this.scrollIndex > 10)
						{
							this.scrollIndex = 10;
						}
					}
					
					else
					{
						this.scrollIndex -= 50;
					}
				}
			}
			
			else
			{
				int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
		        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
				int i = this.width / 2;
				int j = this.height / 2;
				i /= this.zoomFactor;
				j /= this.zoomFactor;
				mouseX /= this.zoomFactor;
				mouseY /= this.zoomFactor;
	            j -= 15;
				
				if (ii != 0)
				{
					if (ii >= 0)
					{
						int offset = 0;
						
						for (Setting setting : this.selectedMod.getSettings())
						{
							if (setting.getTypeOfSetting().equals("Check Box"))
							{
								offset += 25;
							}
							
							if (setting.getTypeOfSetting().equals("Text Box"))
							{
								offset += 25;
							}
							
							if (setting.getTypeOfSetting().equals("Mode"))
							{
								offset += 25;
							}
							
							if (setting.getTypeOfSetting().equals("Color Box"))
							{
								if (this.isInsideBox(mouseX, mouseY, (i - 180) + (this.getStringWidth(setting.getName() + ": ") + 4), (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ":  " + setting.getColors()[0]) + 7, (j - 60) + offset))
								{
									if (setting.getColors()[0] > 0)
									{
										setting.setColors(new int[] {setting.getColors()[0] - 15, setting.getColors()[1], setting.getColors()[2]});
									}
								}
								
								if (this.isInsideBox(mouseX, mouseY, (i - 180) + (this.getStringWidth(setting.getName() + ":  " + setting.getColors()[0]) + 7), (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ":  " + setting.getColors()[0] + ", " + setting.getColors()[1]) + 8, (j - 60) + offset))
								{
									if (setting.getColors()[1] > 0)
									{
										setting.setColors(new int[] {setting.getColors()[0], setting.getColors()[1] - 15, setting.getColors()[2]});
									}
								}
								
								if (this.isInsideBox(mouseX, mouseY, (i - 180) + (this.getStringWidth(setting.getName() + ":  " + setting.getColors()[0] + ", " + setting.getColors()[1]) + 8), (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ":  " + setting.getColors()[0] + ", " + setting.getColors()[1] + ", " + setting.getColors()[2]) + 9, (j - 60) + offset))
								{
									if (setting.getColors()[2] > 0)
									{
										setting.setColors(new int[] {setting.getColors()[0], setting.getColors()[1], setting.getColors()[2] - 15});
									}
								}
								
								offset += 25;
							}
							
							if (setting.getTypeOfSetting().equals("Slider"))
							{
								if (this.isInsideBox(mouseX, mouseY, i - 180, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ": " + setting.getSliderValue()) + 9, (j - 60) + offset))
								{
									if (setting.getSliderValue() > setting.getMinSliderValue())
									{
										setting.setSliderValue(setting.getSliderValue() - setting.getIncrement());
									}
								}
								
								offset += 25;
							}
						}
					}
					
					else
					{
						int offset = 0;
						
						for (Setting setting : this.selectedMod.getSettings())
						{
							if (setting.getTypeOfSetting().equals("Check Box"))
							{
								offset += 25;
							}
							
							if (setting.getTypeOfSetting().equals("Text Box"))
							{
								offset += 25;
							}
							
							if (setting.getTypeOfSetting().equals("Mode"))
							{
								offset += 25;
							}
							
							if (setting.getTypeOfSetting().equals("Color Box"))
							{
								if (this.isInsideBox(mouseX, mouseY, (i - 180) + (this.getStringWidth(setting.getName() + ": ") + 4), (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ":  " + setting.getColors()[0]) + 7, (j - 60) + offset))
								{
									if (setting.getColors()[0] < 255)
									{
										setting.setColors(new int[] {setting.getColors()[0] + 15, setting.getColors()[1], setting.getColors()[2]});
									}
								}
								
								if (this.isInsideBox(mouseX, mouseY, (i - 180) + (this.getStringWidth(setting.getName() + ":  " + setting.getColors()[0]) + 7), (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ":  " + setting.getColors()[0] + ", " + setting.getColors()[1]) + 8, (j - 60) + offset))
								{
									if (setting.getColors()[1] < 255)
									{
										setting.setColors(new int[] {setting.getColors()[0], setting.getColors()[1] + 15, setting.getColors()[2]});
									}
								}
								
								if (this.isInsideBox(mouseX, mouseY, (i - 180) + (this.getStringWidth(setting.getName() + ":  " + setting.getColors()[0] + ", " + setting.getColors()[1]) + 8), (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ":  " + setting.getColors()[0] + ", " + setting.getColors()[1] + ", " + setting.getColors()[2]) + 9, (j - 60) + offset))
								{
									if (setting.getColors()[2] < 255)
									{
										setting.setColors(new int[] {setting.getColors()[0], setting.getColors()[1], setting.getColors()[2] + 15});
									}
								}
								
								offset += 25;
							}
							
							if (setting.getTypeOfSetting().equals("Slider"))
							{
								if (this.isInsideBox(mouseX, mouseY, i - 180, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ": " + setting.getSliderValue()) + 9, (j - 60) + offset))
								{
									if (setting.getSliderValue() < setting.getMaxSliderValue())
									{
										setting.setSliderValue(setting.getSliderValue() + setting.getIncrement());
									}
								}
								
								offset += 25;
							}
						}
					}
				}
				
				j += 15;
			}
		}
    }
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		int i = this.width / 2;
		int j = this.height / 2;
		i /= this.zoomFactor;
		j /= this.zoomFactor;
		mouseX /= this.zoomFactor;
		mouseY /= this.zoomFactor;
		
		if (this.searching)
		{
			int xOffset = 0, yOffset = this.scrollIndex2 - 1;
			
			for (Mod mod : this.searchingMessage.isEmpty() ? Apple.CLIENT.getModsManager().getMods() : Apple.CLIENT.getModsManager().getMods().stream().filter(mod -> mod.getName().toLowerCase().contains(this.searchingMessage.toLowerCase())).collect(Collectors.toCollection(ArrayList::new)))
			{
				if (xOffset != 0 && xOffset % 3 == 0)
				{
					yOffset += 115;
					xOffset = 0;
				}
                
				if (this.isInsideBox(mouseX, mouseY, 0, 69, this.width / this.zoomFactor, this.height / this.zoomFactor) && this.isInsideBox(mouseX, mouseY, (i - 170) + xOffset, (80) + yOffset, (i - 170) + xOffset + 100, (80) + yOffset + 100))
				{
					if (mouseButton == 0)
					{
						SoundUtil.playClickSound();
						mod.toggle();
					}
					
					if (mouseButton == 1)
					{
						SoundUtil.playClickSound();
						this.selectedMod = mod;
						this.searching = false;
						this.scrollIndex2 = -10;
					}
				}
				
				xOffset += 115;
			}
		}
		
		else
		{
			if (this.selectedMod == null)
			{
				int xOffset = 0, yOffset = this.scrollIndex, categoryOffset = 0;
                j -= 20;
                
				if (this.isInsideBox(mouseX, mouseY, i + 55, j - 100, i + 160, j - 80) && mouseButton == 0)
				{
					SoundUtil.playClickSound();
					this.searching = true;
				}
				
				for (Category category : Category.values())
				{
					float f = i - 170;
					
					if (this.isInsideBox(mouseX, mouseY, f + categoryOffset, j - 100, (f + this.getStringWidth(category.name(), 8)) + 10 + categoryOffset, j - 80) && mouseButton == 0)
					{
						SoundUtil.playClickSound();
						this.selectedCategory = category;
						this.scrollIndex = 10;
					}
					
					categoryOffset += this.getStringWidth(category.name(), 8) + 15;
				}
				
				for (Mod mod : this.selectedCategory.equals(Category.ALL) ? Apple.CLIENT.getModsManager().getMods() : Apple.CLIENT.getModsManager().getMods(this.selectedCategory))
				{
					if (xOffset != 0 && xOffset % 3 == 0)
					{
						yOffset += 115;
						xOffset = 0;
					}
					
					if (this.isInsideBox(mouseX, mouseY, i - 200, j - 70, i + 200, j + 150) && this.isInsideBox(mouseX, mouseY, (i - 170) + xOffset, (j - 80) + yOffset, (i - 170) + xOffset + 100, (j - 80) + yOffset + 100))
					{
						if (mouseButton == 0)
						{
							SoundUtil.playClickSound();
							mod.toggle();
						}
						
						if (mouseButton == 1)
						{
							SoundUtil.playClickSound();
							this.selectedMod = mod;
						}
					}
					
					xOffset += 115;
				}
				
	            j += 20;
			}
			
			else
			{
				int offset = 0;
	            j -= 15;
				
				for (Setting setting : this.selectedMod.getSettings())
				{
					if (setting.getTypeOfSetting().equals("Check Box"))
					{
						if (this.isInsideBox(mouseX, mouseY, i - 180, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName()) + 25, (j - 60) + offset) && mouseButton == 0)
						{
							SoundUtil.playClickSound();
							setting.setCheckBoxValue(!setting.getCheckBoxValue());
						}
						
						offset += 25;
					}
					
					if (setting.getTypeOfSetting().equals("Mode"))
					{
						if (this.isInsideBox(mouseX, mouseY, i - 180, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ": " + setting.getModeValue()) + 25, (j - 60) + offset))
						{
							int index = setting.getModes().indexOf(setting.getModeValue());
							SoundUtil.playClickSound();
							
							if (mouseButton == 0)
							{
								if (index < (setting.getModes().size() - 1))
								{
									index++;
								}
								
								else
								{
									index = 0;
								}
							}
							
							else
							{
								if (index > 0)
								{
									index--;
								}
								
								else
								{
									index = setting.getModes().size() - 1;
								}
							}
							
							setting.setModeValue(setting.getModes().get(index));
						}
						
						offset += 25;
					}
					
					if (setting.getTypeOfSetting().equals("Text Box"))
					{
						if (this.isInsideBox(mouseX, mouseY, i - 180, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ": " + setting.getTextBoxValue()) + 9, (j - 60) + offset) && mouseButton == 0)
						{
							SoundUtil.playClickSound();
							setting.setFocused(true);
						}
						
						else
						{
							setting.setFocused(false);
						}
						
						offset += 25;
					}
					
					if (setting.getTypeOfSetting().equals("Color Box"))
					{
						if (this.isInsideBox(mouseX, mouseY, i - 180, (j - 80) + offset, (i - 180) + this.getStringWidth(setting.getName() + ": " + setting.getColors()[0] + ", " + setting.getColors()[1] + ", " + setting.getColors()[2]) + 9, (j - 60) + offset) && mouseButton == 0)
						{
							SoundUtil.playClickSound();
						}
						
						offset += 25;
					}
					
					if (setting.getTypeOfSetting().equals("Slider"))
					{
						offset += 25;
					}
				}
				
				j += 15;
			}
		}
    }
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state)
    {
		super.mouseReleased(mouseX, mouseY, state);
    }
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.index = 0;
		this.randomModName = Apple.CLIENT.getModsManager().getMods().get(new Random().nextInt(Apple.CLIENT.getModsManager().getMods().size() - 1)).getName();
		Keyboard.enableRepeatEvents(true);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
		if (keyCode == 1)
        {
			if (this.selectedMod != null)
			{
				this.selectedMod = null;
			}
			
			else if (this.searching)
			{
				this.scrollIndex2 = -10;
				this.searching = false;
			}
			
			else
			{
				this.closingGui = true;
			}
        }
		
		else
		{
			if (GuiScreen.isCtrlKeyDown())
			{
				if (GuiScreen.isPlusKeyDown())
				{
					this.zoomFactor += 0.25F;
				}
				
				else if (GuiScreen.isMinusKeyDown())
				{
					this.zoomFactor -= 0.25F;
				}
				
				if (this.zoomFactor < 0.25F)
				{
					this.zoomFactor = 0.25F;
				}
			}
			
			if (this.searching)
			{
				if (keyCode == 14)
				{
					if (this.searchingMessage.length() > 0)
					{
						this.searchingMessage = this.searchingMessage.substring(0, this.searchingMessage.length() - 1);
					}
				}
				
				else if (GuiScreen.isKeyComboCtrlV(keyCode))
				{
					this.searchingMessage += GuiScreen.getClipboardString();
				}
				
				else
				{
					this.searchingMessage += this.getChar(typedChar);
				}
				
				this.scrollIndex2 = -10;
			}
			
			else
			{
				if (this.selectedMod != null)
				{
					int offset = 0;
					
					for (Setting setting : this.selectedMod.getSettings())
					{
						if (setting.getTypeOfSetting().equals("Check Box"))
						{
							offset += 25;
						}
						
						if (setting.getTypeOfSetting().equals("Slider"))
						{
							offset += 25;
						}
						
						if (setting.getTypeOfSetting().equals("Text Box"))
						{
							if (setting.isFocused())
							{
								if (keyCode == 14)
								{
									if (setting.getTextBoxValue().length() > 0)
									{
										setting.setTextBoxValue(setting.getTextBoxValue().substring(0, setting.getTextBoxValue().length() - 1));
									}
								}
								
								else if (GuiScreen.isKeyComboCtrlV(keyCode))
								{
									setting.setTextBoxValue(setting.getTextBoxValue() + GuiScreen.getClipboardString());
								}
								
								else
								{
									setting.setTextBoxValue(setting.getTextBoxValue() + this.getChar(typedChar));
								}
							}
							
							offset += 25;
						}
						
						if (setting.getTypeOfSetting().equals("Mode"))
						{
							offset += 25;
						}
						
						if (setting.getTypeOfSetting().equals("Color Box"))
						{
							offset += 25;
						}
					}
				}
			}
		}
    }
	
	public String getChar(char typedChar)
	{
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !@#$%^&*()-_=+[]\\{}|;':\"<>?,./~`";
		String charr = String.valueOf(typedChar);
		
		if (characters.contains(charr))
		{
			return charr;
		}
		
		else
		{
			return "";
		}
	}
	
	@Override
	public boolean doesGuiPauseGame()
    {
		return false;
    }
	
	public boolean isInsideBox(int mouseX, int mouseY, float x, float y, float width, float height)
	{
		return mouseX > x && mouseX < width && mouseY > y && mouseY < height;
	}
}

package appu26j.fontrenderer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import appu26j.interfaces.MinecraftInterface;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

public class CustomFontRenderer implements MinecraftInterface
{
    private static HashMap<String, Float> cachedStringWidths = new HashMap<>();
    private static UnicodeFont font, fontItalic, fontBold, fontBoldAndItalic;
    private static int previousScaleFactor = 0, size = 0, currentColor = -1;
    private static int[] colorCode = new int[32];
    private static String fontPath = "";
    
    public static void loadFont(String fontPath, int size)
    {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        previousScaleFactor = scaledResolution.getScaleFactor();
        CustomFontRenderer.fontPath = fontPath;
        CustomFontRenderer.size = size;
        
        try
        {
            font = new UnicodeFont(fontPath, size * scaledResolution.getScaleFactor(), false, false);
            font.addAsciiGlyphs();
            font.getEffects().add(new ColorEffect());
            font.loadGlyphs();
        }
        
        catch (Exception e)
        {
            ;
        }
        
        try
        {
            fontItalic = new UnicodeFont(fontPath, size * scaledResolution.getScaleFactor(), false, true);
            fontItalic.addAsciiGlyphs();
            fontItalic.getEffects().add(new ColorEffect());
            fontItalic.loadGlyphs();
        }
        
        catch (Exception e)
        {
            ;
        }
        
        try
        {
            fontBold = new UnicodeFont(fontPath, size * scaledResolution.getScaleFactor(), true, false);
            fontBold.addAsciiGlyphs();
            fontBold.getEffects().add(new ColorEffect());
            fontBold.loadGlyphs();
        }
        
        catch (Exception e)
        {
            ;
        }
        
        try
        {
            fontBoldAndItalic = new UnicodeFont(fontPath, size * scaledResolution.getScaleFactor(), true, true);
            fontBoldAndItalic.addAsciiGlyphs();
            fontBoldAndItalic.getEffects().add(new ColorEffect());
            fontBoldAndItalic.loadGlyphs();
        }
        
        catch (Exception e)
        {
            ;
        }
        
        for (int i = 0; i < 32; ++i)
        {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i >> 0 & 1) * 170 + j;

            if (i == 6)
            {
                k += 85;
            }

            if (mc.gameSettings.anaglyph)
            {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }

            if (i >= 16)
            {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }
    }
    
    public static float drawCenteredString(String text, float x, float y, int color)
    {
        return drawStringWithShadow(text, x - (getStringWidth(text) / 2), y, color);
    }
    
    public static float drawStringWithShadow(String text, float x, float y, int color)
    {
        if (text != null && !text.trim().isEmpty())
        {
            y -= 3;
            x -= 0.5F;
            int textureID = GlStateManager.getBoundTexture();
            ScaledResolution scaledResolution = new ScaledResolution(mc);
            
            if (previousScaleFactor != scaledResolution.getScaleFactor())
            {
                try
                {
                    font = new UnicodeFont(fontPath, size * scaledResolution.getScaleFactor(), false, false);
                    font.addAsciiGlyphs();
                    font.getEffects().add(new ColorEffect());
                    font.loadGlyphs();
                    previousScaleFactor = scaledResolution.getScaleFactor();
                }
                
                catch (Exception e)
                {
                    ;
                }
                
                try
                {
                    fontItalic = new UnicodeFont(fontPath, size * scaledResolution.getScaleFactor(), false, true);
                    fontItalic.addAsciiGlyphs();
                    fontItalic.getEffects().add(new ColorEffect());
                    fontItalic.loadGlyphs();
                }
                
                catch (Exception e)
                {
                    ;
                }
                
                try
                {
                    fontBold = new UnicodeFont(fontPath, size * scaledResolution.getScaleFactor(), true, false);
                    fontBold.addAsciiGlyphs();
                    fontBold.getEffects().add(new ColorEffect());
                    fontBold.loadGlyphs();
                }
                
                catch (Exception e)
                {
                    ;
                }
                
                try
                {
                    fontBoldAndItalic = new UnicodeFont(fontPath, size * scaledResolution.getScaleFactor(), true, true);
                    fontBoldAndItalic.addAsciiGlyphs();
                    fontBoldAndItalic.getEffects().add(new ColorEffect());
                    fontBoldAndItalic.loadGlyphs();
                }
                
                catch (Exception e)
                {
                    ;
                }
            }
            
            boolean bold = false, obfuscated = false, strikethrough = false, underline = false, italic = false;
            boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
            boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
            boolean texture = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
            String previousColorCode = "";
            
            if (!blend)
            {
                GL11.glEnable(GL11.GL_BLEND);
            }
            
            if (lighting)
            {
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            
            if (texture)
            {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }
            
            GlStateManager.color(1, 1, 1, 1);
            x *= previousScaleFactor;
            y *= previousScaleFactor;
            currentColor = color;
            float xPosition = x;
            GlStateManager.pushMatrix();
            GlStateManager.scale(1F / previousScaleFactor, 1F / previousScaleFactor, 1F / previousScaleFactor);
            ArrayList<String> unallowedLetters = new ArrayList<>();
            
            for (int i = 0; i < text.length(); i++)
            {
                char character = text.charAt(i);
                
                if (!isCharAllowed(character))
                {
                    unallowedLetters.add(previousColorCode + String.valueOf(character) + "\n" + xPosition + "," + y);
                    xPosition += mc.fontRendererObj.getStringWidthNoCustomFont(String.valueOf(character)) * previousScaleFactor;
                    continue;
                }
                
                if (character == '�' && (i + 1) < text.length())
                {
                    int l = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                    
                    if (l < 16)
                    {
                        if (l < 0 || l > 15)
                        {
                            l = 15;
                        }
                        
                        bold = false;
                        obfuscated = false;
                        strikethrough = false;
                        underline = false;
                        italic = false;
                        previousColorCode = String.valueOf(text.charAt(i + 1));
                        currentColor = colorCode[l];
                    }
                    
                    if (l == 17)
                    {
                        bold = true;
                    }
                    
                    if (l == 16)
                    {
                        obfuscated = true;
                    }
                    
                    if (l == 18)
                    {
                        strikethrough = true;
                    }
                    
                    if (l == 19)
                    {
                        underline = true;
                    }
                    
                    if (l == 20)
                    {
                        italic = true;
                    }
                    
                    continue;
                }
                
                if ((i + 2) < text.length() && text.substring(i, i + 2).equals("�r"))
                {
                    bold = false;
                    obfuscated = false;
                    strikethrough = false;
                    underline = false;
                    italic = false;
                    currentColor = color;
                }
                
                else if ((i + 1) < text.length() && (i - 1) > 0 && text.substring(i - 1, i + 1).equals("�r"))
                {
                    bold = false;
                    obfuscated = false;
                    strikethrough = false;
                    underline = false;
                    italic = false;
                    currentColor = color;
                }
                
                if (character == '\n')
                {
                    xPosition = x;
                    y += size * previousScaleFactor;
                }
                
                if (i > 0 && text.charAt(i - 1) == '�')
                {
                    continue;
                }
                
                if (obfuscated)
                {
                    float k = getStringWidth(String.valueOf(character));
                    char character2 = 0;
                    int integer = 0;
                    
                    while (integer++ < "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".length() * 4)
                    {
                        Random random = new Random();
                        int j = random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".length());
                        character2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".charAt(j);

                        if (k == getStringWidth(String.valueOf(character2)))
                        {
                            break;
                        }
                    }
                    
                    character = character2;
                }
                
                Color temp = new Color(currentColor, true);
                currentColor = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), new Color(color, true).getAlpha()).getRGB();
                int darkColor = (currentColor & 16579836) >> 2 | currentColor & -16777216;
                float offset = ((size * (float) previousScaleFactor) / 10);
                    
                if (bold && italic)
                {
                    fontBoldAndItalic.drawString(xPosition + offset, y + offset, String.valueOf(character), new org.newdawn.slick.Color(darkColor));
                }
                
                else if (bold)
                {
                    fontBold.drawString(xPosition + offset, y + offset, String.valueOf(character), new org.newdawn.slick.Color(darkColor));
                }
                
                else if (italic)
                {
                    fontItalic.drawString(xPosition + offset, y + offset, String.valueOf(character), new org.newdawn.slick.Color(darkColor));
                }
                
                else
                {
                    font.drawString(xPosition + offset, y + offset, String.valueOf(character), new org.newdawn.slick.Color(darkColor));
                }
                
                if (bold && italic)
                {
                    fontBoldAndItalic.drawString(xPosition, y, String.valueOf(character), new org.newdawn.slick.Color(currentColor));
                }
                
                else if (bold)
                {
                    fontBold.drawString(xPosition, y, String.valueOf(character), new org.newdawn.slick.Color(currentColor));
                }
                
                else if (italic)
                {
                    fontItalic.drawString(xPosition, y, String.valueOf(character), new org.newdawn.slick.Color(currentColor));
                }
                
                else
                {
                    font.drawString(xPosition, y, String.valueOf(character), new org.newdawn.slick.Color(currentColor));
                }
                
                if (strikethrough)
                {
                    float ytemp = y + (size * (float) previousScaleFactor) + (size / 2);
                    Gui.drawRect(xPosition, ytemp - 7, xPosition + (getStringWidth(String.valueOf(character)) * previousScaleFactor), ytemp - 5, -1);
                    GL11.glEnable(GL11.GL_BLEND);
                }
                
                if (underline)
                {
                    float ytemp = y + (size * (float) previousScaleFactor) + size + 4;
                    Gui.drawRect(xPosition, ytemp - 7, xPosition + (getStringWidth(String.valueOf(character)) * previousScaleFactor), ytemp - 5, -1);
                    GL11.glEnable(GL11.GL_BLEND);
                }
                
                xPosition += getStringWidth(String.valueOf(character)) * previousScaleFactor;
            }
            
            GlStateManager.popMatrix();
            
            if (texture)
            {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
            }
            
            if (lighting)
            {
                GL11.glEnable(GL11.GL_LIGHTING);
            }
            
            if (!blend)
            {
                GL11.glDisable(GL11.GL_BLEND);
            }

            GL11.glColor4f(1, 1, 1, 1);
            
            for (int i = 0; i < unallowedLetters.size(); i++)
            {
                try
                {
                    String temp = unallowedLetters.get(i);
                    String name = temp.split("\n")[0];
                    String positionTemp = temp.substring(temp.split("\n").length);
                    float xPos = Float.parseFloat(positionTemp.split(",")[0]);
                    float yPos = Float.parseFloat(positionTemp.split(",")[1]);
                    String doubleS = previousColorCode.isEmpty() ? "" : "�";
                    mc.fontRendererObj.drawString(doubleS + name, xPos / previousScaleFactor + 1, yPos / previousScaleFactor + 3, currentColor, false);
                }
                
                catch (Exception e)
                {
                    ;
                }
            }
            
            return xPosition / previousScaleFactor;
        }
        
        else
        {
            return 0;
        }
    }
    
    public static float drawString(String text, float x, float y, int color)
    {
        if (text != null && !text.trim().isEmpty())
        {
            y -= 3;
            x -= 0.5F;
            int textureID = GlStateManager.getBoundTexture();
            ScaledResolution scaledResolution = new ScaledResolution(mc);
            
            if (previousScaleFactor != scaledResolution.getScaleFactor())
            {
                try
                {
                    font = new UnicodeFont(fontPath, size * scaledResolution.getScaleFactor(), false, false);
                    font.addAsciiGlyphs();
                    font.getEffects().add(new ColorEffect());
                    font.loadGlyphs();
                    previousScaleFactor = scaledResolution.getScaleFactor();
                }
                
                catch (Exception e)
                {
                    ;
                }
                
                try
                {
                    fontItalic = new UnicodeFont(fontPath, size * scaledResolution.getScaleFactor(), false, true);
                    fontItalic.addAsciiGlyphs();
                    fontItalic.getEffects().add(new ColorEffect());
                    fontItalic.loadGlyphs();
                }
                
                catch (Exception e)
                {
                    ;
                }
                
                try
                {
                    fontBold = new UnicodeFont(fontPath, size * scaledResolution.getScaleFactor(), true, false);
                    fontBold.addAsciiGlyphs();
                    fontBold.getEffects().add(new ColorEffect());
                    fontBold.loadGlyphs();
                }
                
                catch (Exception e)
                {
                    ;
                }
                
                try
                {
                    fontBoldAndItalic = new UnicodeFont(fontPath, size * scaledResolution.getScaleFactor(), true, true);
                    fontBoldAndItalic.addAsciiGlyphs();
                    fontBoldAndItalic.getEffects().add(new ColorEffect());
                    fontBoldAndItalic.loadGlyphs();
                }
                
                catch (Exception e)
                {
                    ;
                }
            }
            
            boolean bold = false, obfuscated = false, strikethrough = false, underline = false, italic = false;
            boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
            boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
            boolean texture = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
            String previousColorCode = "";
            
            if (!blend)
            {
                GL11.glEnable(GL11.GL_BLEND);
            }
            
            if (lighting)
            {
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            
            if (texture)
            {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }
            
            GlStateManager.color(1, 1, 1, 1);
            x *= previousScaleFactor;
            y *= previousScaleFactor;
            currentColor = color;
            float xPosition = x;
            GlStateManager.pushMatrix();
            GlStateManager.scale(1F / previousScaleFactor, 1F / previousScaleFactor, 1F / previousScaleFactor);
            ArrayList<String> unallowedLetters = new ArrayList<>();
            
            for (int i = 0; i < text.length(); i++)
            {
                char character = text.charAt(i);
                
                if (!isCharAllowed(character))
                {
                    unallowedLetters.add(previousColorCode + String.valueOf(character) + "\n" + xPosition + "," + y);
                    xPosition += mc.fontRendererObj.getStringWidthNoCustomFont(String.valueOf(character)) * previousScaleFactor;
                    continue;
                }
                
                if (character == '�' && (i + 1) < text.length())
                {
                    int l = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                    
                    if (l < 16)
                    {
                        if (l < 0 || l > 15)
                        {
                            l = 15;
                        }
                        
                        bold = false;
                        obfuscated = false;
                        strikethrough = false;
                        underline = false;
                        italic = false;
                        previousColorCode = String.valueOf(text.charAt(i + 1));
                        currentColor = colorCode[l];
                    }
                    
                    if (l == 17)
                    {
                        bold = true;
                    }
                    
                    if (l == 16)
                    {
                        obfuscated = true;
                    }
                    
                    if (l == 18)
                    {
                        strikethrough = true;
                    }
                    
                    if (l == 19)
                    {
                        underline = true;
                    }
                    
                    if (l == 20)
                    {
                        italic = true;
                    }
                    
                    continue;
                }
                
                if ((i + 2) < text.length() && text.substring(i, i + 2).equals("�r"))
                {
                    bold = false;
                    obfuscated = false;
                    strikethrough = false;
                    underline = false;
                    italic = false;
                    currentColor = color;
                }
                
                else if ((i + 1) < text.length() && (i - 1) > 0 && text.substring(i - 1, i + 1).equals("�r"))
                {
                    bold = false;
                    obfuscated = false;
                    strikethrough = false;
                    underline = false;
                    italic = false;
                    currentColor = color;
                }
                
                if (character == '\n')
                {
                    xPosition = x;
                    y += size * previousScaleFactor;
                }
                
                if (i > 0 && text.charAt(i - 1) == '�')
                {
                    continue;
                }
                
                if (obfuscated)
                {
                    float k = getStringWidth(String.valueOf(character));
                    char character2 = 0;
                    int integer = 0;
                    
                    while (integer++ < "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".length() * 4)
                    {
                        Random random = new Random();
                        int j = random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".length());
                        character2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".charAt(j);

                        if (k == getStringWidth(String.valueOf(character2)))
                        {
                            break;
                        }
                    }
                    
                    character = character2;
                }
                
                Color temp = new Color(currentColor, true);
                currentColor = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), new Color(color, true).getAlpha()).getRGB();
                
                if (bold && italic)
                {
                    fontBoldAndItalic.drawString(xPosition, y, String.valueOf(character), new org.newdawn.slick.Color(currentColor));
                }
                
                else if (bold)
                {
                    fontBold.drawString(xPosition, y, String.valueOf(character), new org.newdawn.slick.Color(currentColor));
                }
                
                else if (italic)
                {
                    fontItalic.drawString(xPosition, y, String.valueOf(character), new org.newdawn.slick.Color(currentColor));
                }
                
                else
                {
                    font.drawString(xPosition, y, String.valueOf(character), new org.newdawn.slick.Color(currentColor));
                }
                
                if (strikethrough)
                {
                    float ytemp = y + (size * (float) previousScaleFactor) + (size / 2);
                    Gui.drawRect(xPosition, ytemp - 7, xPosition + (getStringWidth(String.valueOf(character)) * previousScaleFactor), ytemp - 5, -1);
                    GL11.glEnable(GL11.GL_BLEND);
                }
                
                if (underline)
                {
                    float ytemp = y + (size * (float) previousScaleFactor) + size + 4;
                    Gui.drawRect(xPosition, ytemp - 7, xPosition + (getStringWidth(String.valueOf(character)) * previousScaleFactor), ytemp - 5, -1);
                    GL11.glEnable(GL11.GL_BLEND);
                }
                
                xPosition += getStringWidth(String.valueOf(character)) * previousScaleFactor;
            }
            
            GlStateManager.popMatrix();
            
            if (texture)
            {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
            }
            
            if (lighting)
            {
                GL11.glEnable(GL11.GL_LIGHTING);
            }
            
            if (!blend)
            {
                GL11.glDisable(GL11.GL_BLEND);
            }

            GL11.glColor4f(1, 1, 1, 1);
            
            for (int i = 0; i < unallowedLetters.size(); i++)
            {
                try
                {
                    String temp = unallowedLetters.get(i);
                    String name = temp.split("\n")[0];
                    String positionTemp = temp.substring(temp.split("\n").length);
                    float xPos = Float.parseFloat(positionTemp.split(",")[0]);
                    float yPos = Float.parseFloat(positionTemp.split(",")[1]);
                    String doubleS = previousColorCode.isEmpty() ? "" : "�";
                    mc.fontRendererObj.drawString(doubleS + name, xPos / previousScaleFactor + 1, yPos / previousScaleFactor + 3, currentColor, false);
                }
                
                catch (Exception e)
                {
                    ;
                }
            }
            
            return xPosition / previousScaleFactor;
        }
        
        else
        {
            return 0;
        }
    }
    
    public static float getStringWidth(String text)
    {
        String temp = EnumChatFormatting.getTextWithoutFormattingCodes(text);
        
        if (cachedStringWidths.size() > 1)
        {
            cachedStringWidths.clear();
        }
        
        return cachedStringWidths.computeIfAbsent(temp, function ->
        {
            float width = 0;
            
            for (char character : temp.toCharArray())
            {
                if (!isCharAllowed(character))
                {
                    width += (mc.fontRendererObj.getStringWidthNoCustomFont(String.valueOf(character)) * previousScaleFactor) + 1;
                    continue;
                }
                
                width += font.getWidth(String.valueOf(character)) + 1;
            }
            
            return width / (float) previousScaleFactor;
        });
    }
    
    private static boolean isCharAllowed(char character)
    {
        String[] allowedLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|\\'\";:,.<>/?� ".split("");
        boolean allowed = false;
        
        for (String letter : allowedLetters)
        {
            if (letter.equals(String.valueOf(character)))
            {
                allowed = true;
                break;
            }
        }
        
        return allowed;
    }
}

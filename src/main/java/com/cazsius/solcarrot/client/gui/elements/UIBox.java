package com.cazsius.solcarrot.client.gui.elements;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class UIBox extends UIElement {
	public static UIBox horizontalLine(int minX, int maxX, int y, int color) {
		return new UIBox(new Rectangle(minX, y, maxX + 1 - minX, 1), color);
	}
	
	public static UIBox verticalLine(int x, int minY, int maxY, int color) {
		return new UIBox(new Rectangle(x, minY, 1, maxY + 1 - minY), color);
	}
	
	public int color;
	
	public UIBox(Rectangle frame, int color) {
		super(frame);
		
		this.color = color;
	}
	
	@Override
	protected void render() {
		super.render();
		
		float a = (color >> 24 & 0xFF) / 255f;
		float r = (color >> 16 & 0xFF) / 255f;
		float g = (color >> 8 & 0xFF) / 255f;
		float b = (color & 0xFF) / 255f;
		
		GlStateManager.enableBlend();
		GlStateManager.color(r, g, b, a);
		Gui.drawRect(frame.x, frame.y, frame.x + frame.width, frame.y + frame.height, color);
	}
}

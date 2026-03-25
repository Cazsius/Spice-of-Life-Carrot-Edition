package com.cazsius.solcarrot.client.gui.elements;

import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.awt.*;

public class UILabel extends UIElement {
	public String text = "";
	public TextAlignment alignment = TextAlignment.CENTER;
	public Color color = Color.BLACK;
	
	/** sets frame to text size */
	public UILabel(String text) {
		this(new Rectangle(font.width(text) - 1, 7), text);
	}
	
	public UILabel(Rectangle frame, String text) {
		super(frame);
		this.text = text;
	}
	
	public UILabel(Rectangle frame) {
		super(frame);
	}
	
	@Override
	protected void render(GuiGraphicsExtractor graphics) {
		super.render(graphics);
		
		int textWidth = font.width(text) - 1;
		int x = frame.x + (frame.width - textWidth) * alignment.ordinal / 2;
		int y = frame.y + (frame.height - 7) / 2;
//		if (color.getTransparency() == Color.TRANSLUCENT) {
//			RenderSystem.enableBlend(); TODO: Check if this is still needed
//		}
		graphics.text(font, text, x, y, color.getRGB(), false);
	}
	
	public enum TextAlignment {
		LEFT(0), CENTER(1), RIGHT(2);
		
		final int ordinal;
		
		TextAlignment(int ordinal) {
			this.ordinal = ordinal;
		}
	}
}

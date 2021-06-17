package com.cazsius.solcarrot.client.gui.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import java.awt.*;

public class UILabel extends UIElement {
	public String text = "";
	public TextAlignment alignment = TextAlignment.CENTER;
	public Color color = Color.BLACK;
	
	/** sets frame to text size */
	public UILabel(String text) {
		this(new Rectangle(fontRenderer.width(text) - 1, 7), text);
	}
	
	public UILabel(Rectangle frame, String text) {
		super(frame);
		this.text = text;
	}
	
	public UILabel(Rectangle frame) {
		super(frame);
	}
	
	@Override
	protected void render(MatrixStack matrices) {
		super.render(matrices);
		
		int textWidth = fontRenderer.width(text) - 1;
		int x = frame.x + (frame.width - textWidth) * alignment.ordinal / 2;
		int y = frame.y + (frame.height - 7) / 2;
		if (color.getTransparency() == Color.TRANSLUCENT) {
			RenderSystem.enableBlend();
		}
		fontRenderer.draw(matrices, text, x, y, color.getRGB());
	}
	
	enum TextAlignment {
		LEFT(0), CENTER(1), RIGHT(2);
		
		final int ordinal;
		
		TextAlignment(int ordinal) {
			this.ordinal = ordinal;
		}
	}
}

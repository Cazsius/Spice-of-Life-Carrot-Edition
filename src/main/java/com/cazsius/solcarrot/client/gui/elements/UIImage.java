package com.cazsius.solcarrot.client.gui.elements;

import net.minecraft.client.gui.GuiGraphics;

import java.awt.*;

public class UIImage extends UIElement {
	public ImageData data;
	public float alpha = 1;
	
	public UIImage(ImageData data) {
		this(new Rectangle(data.visualWidth, data.visualHeight), data);
	}
	
	public UIImage(Rectangle frame, ImageData data) {
		super(frame);
		
		this.data = data;
	}
	
	@Override
	protected void render(GuiGraphics graphics) {
		super.render(graphics);
		
		int imageWidth = data.partOfTexture.width;
		int imageHeight = data.partOfTexture.height;
		
		graphics.blit(
			data.textureLocation,
			frame.x + (int) Math.floor((frame.width - imageWidth) / 2d),
			frame.y + (int) Math.floor((frame.height - imageHeight) / 2d),
			0,
			data.partOfTexture.x, data.partOfTexture.y,
			data.partOfTexture.width, data.partOfTexture.height,
			256, 256
		);
	}
}

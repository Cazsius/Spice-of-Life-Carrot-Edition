package com.cazsius.solcarrot.client.gui.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraftforge.fml.client.gui.GuiUtils;

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
	protected void render(MatrixStack matrices) {
		super.render(matrices);
		
		int imageWidth = data.partOfTexture.width;
		int imageHeight = data.partOfTexture.height;
		
		RenderSystem.enableBlend();
		RenderSystem.color4f(1, 1, 1, alpha);
		mc.getTextureManager().bind(data.textureLocation);
		GuiUtils.drawTexturedModalRect(
			frame.x + (int) Math.floor((frame.width - imageWidth) / 2d),
			frame.y + (int) Math.floor((frame.height - imageHeight) / 2d),
			data.partOfTexture.x, data.partOfTexture.y,
			data.partOfTexture.width, data.partOfTexture.height,
			0
		);
	}
	
}

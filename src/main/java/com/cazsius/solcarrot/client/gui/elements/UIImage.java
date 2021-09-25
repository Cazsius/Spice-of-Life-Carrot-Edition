package com.cazsius.solcarrot.client.gui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;

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
	protected void render(PoseStack matrices) {
		super.render(matrices);
		
		int imageWidth = data.partOfTexture.width;
		int imageHeight = data.partOfTexture.height;
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, data.textureLocation);
		
		GuiComponent.blit(
			matrices,
			frame.x + (int) Math.floor((frame.width - imageWidth) / 2d),
			frame.y + (int) Math.floor((frame.height - imageHeight) / 2d),
			0,
			data.partOfTexture.x, data.partOfTexture.y,
			data.partOfTexture.width, data.partOfTexture.height,
			256, 256
		);
	}
	
}

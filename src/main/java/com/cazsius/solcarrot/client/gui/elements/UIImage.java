package com.cazsius.solcarrot.client.gui.elements;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.awt.*;

public class UIImage extends UIElement {
	public Image image;
	public float alpha = 1;
	
	public UIImage(Image image) {
		this(new Rectangle(image.partOfTexture.getSize()), image);
	}
	
	public UIImage(Rectangle frame, Image image) {
		super(frame);
		
		this.image = image;
	}
	
	@Override
	protected void render() {
		super.render();
		
		int imageWidth = image.partOfTexture.width;
		int imageHeight = image.partOfTexture.height;
		
		GlStateManager.enableBlend();
		GlStateManager.color(1, 1, 1, alpha);
		mc.getTextureManager().bindTexture(image.textureLocation);
		GuiUtils.drawTexturedModalRect(
			frame.x + (int) Math.floor((frame.width - imageWidth) / 2d),
			frame.y + (int) Math.floor((frame.height - imageHeight) / 2d),
			image.partOfTexture.x, image.partOfTexture.y,
			image.partOfTexture.width, image.partOfTexture.height,
			0
		);
	}
	
	public static class Image {
		public final ResourceLocation textureLocation;
		public final Rectangle partOfTexture;
		
		public Image(ResourceLocation textureLocation, Rectangle partOfTexture) {
			this.textureLocation = textureLocation;
			this.partOfTexture = partOfTexture;
		}
	}
}

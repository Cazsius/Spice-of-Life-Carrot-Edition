package com.cazsius.solcarrot.client.gui.elements;

import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public final class ImageData {
	public final ResourceLocation textureLocation;
	public final Rectangle partOfTexture;
	public int visualWidth;
	public int visualHeight;
	
	public ImageData(ResourceLocation textureLocation, Rectangle partOfTexture) {
		this(textureLocation, partOfTexture, partOfTexture.width, partOfTexture.height);
	}
	
	public ImageData(ResourceLocation textureLocation, Rectangle partOfTexture, int visualWidth, int visualHeight) {
		this.textureLocation = textureLocation;
		this.partOfTexture = partOfTexture;
		this.visualWidth = visualWidth;
		this.visualHeight = visualHeight;
	}
}

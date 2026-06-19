package com.cazsius.solcarrot.client.gui.elements;

import net.minecraft.resources.Identifier;

import java.awt.*;

public final class ImageData {
	public final Identifier textureLocation;
	public final Rectangle partOfTexture;
	public final int visualWidth;
	public final int visualHeight;

	public ImageData(Identifier textureLocation, Rectangle partOfTexture) {
		this(textureLocation, partOfTexture, partOfTexture.width, partOfTexture.height);
	}

	public ImageData(Identifier textureLocation, Rectangle partOfTexture, int visualWidth, int visualHeight) {
		this.textureLocation = textureLocation;
		this.partOfTexture = partOfTexture;
		this.visualWidth = visualWidth;
		this.visualHeight = visualHeight;
	}
}

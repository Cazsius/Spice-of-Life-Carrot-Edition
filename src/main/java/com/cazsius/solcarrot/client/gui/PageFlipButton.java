package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.SOLCarrot;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
final class PageFlipButton extends Button {
	private static final ResourceLocation texture = SOLCarrot.resourceLocation("textures/gui/food_book.png");
	public static final int width = 23;
	public static final int height = 13;
	
	private final Direction direction;
	private final Pageable pageable;
	
	PageFlipButton(int x, int y, Direction direction, Pageable pageable) {
		super(x, y, 23, 13, "", (button) -> ((PageFlipButton) button).changePage());
		
		this.direction = direction;
		this.pageable = pageable;
	}
	
	@Override
	public void renderButton(int mouseX, int mouseY, float partialTicks) {
		if (!visible) return;
		
		int textureX = 0;
		
		boolean isHovered = x <= mouseX && mouseX < x + width && y <= mouseY && mouseY < y + height;
		if (isHovered) {
			textureX += 23;
		}
		
		int textureY = direction == Direction.FORWARD ? 192 : 205;
		
		Minecraft.getInstance().getTextureManager().bindTexture(texture);
		blit(x, y, textureX, textureY, 23, 13);
	}
	
	public void updateState() {
		visible = pageable.isWithinRange(pageable.getCurrentPageNumber() + direction.distance);
	}
	
	private void changePage() {
		pageable.switchToPage(pageable.getCurrentPageNumber() + direction.distance);
	}
	
	enum Direction {
		FORWARD(1),
		BACKWARD(-1);
		
		final int distance;
		
		Direction(int distance) {
			this.distance = distance;
		}
	}
	
	interface Pageable {
		void switchToPage(int pageNumber);
		
		int getCurrentPageNumber();
		
		boolean isWithinRange(int pageNumber);
	}
}

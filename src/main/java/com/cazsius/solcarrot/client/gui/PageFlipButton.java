package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.SOLCarrot;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
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
		super(x, y, width, height, Component.empty(), (button) -> ((PageFlipButton) button).changePage());
		
		this.direction = direction;
		this.pageable = pageable;
	}
	
	@Override
	public void renderButton(PoseStack matrices, int mouseX, int mouseY, float partialTicks) {
		if (!visible) return;
		
		int textureX = 0;
		
		boolean isHovered = x <= mouseX && mouseX < x + width && y <= mouseY && mouseY < y + height;
		if (isHovered) {
			textureX += width;
		}
		
		int textureY = 192;
		if (direction == Direction.BACKWARD) {
			textureY += height;
		}
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, texture);
		blit(matrices, x, y, 0, textureX, textureY, width, height, 256, 256);
	}
	
	public void updateState() {
		visible = pageable.isWithinRange(pageable.getCurrentPageNumber() + direction.distance);
	}
	
	private void changePage() {
		pageable.switchToPage(pageable.getCurrentPageNumber() + direction.distance);
	}
	
	@Override
	public void playDownSound(SoundManager soundManager) {
		soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
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

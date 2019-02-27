package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.capability.FoodInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Optional;

import static com.cazsius.solcarrot.lib.Localization.localized;

@SideOnly(Side.CLIENT)
public final class GuiFoodBook extends GuiScreen {
	private static final ResourceLocation backgroundTexture = new ResourceLocation("textures/gui/book.png");
	private static final int textureWidth = 192;
	private static final int textureHeight = 192;
	
	private static final int foodsPerRow = 5;
	private static final int rowsPerPage = 5;
	private static final int foodsPerPage = foodsPerRow * rowsPerPage;
	
	private int leftEdge;
	private int topEdge;
	private int centerX;
	private int centerY;
	
	private NextPageButton nextPageButton;
	private NextPageButton prevPageButton;
	private GuiButton doneButton;
	
	private final FoodCapability foodCapability;
	private final List<FoodInstance> foodLog;
	
	private int pageCount;
	private int currentPage = 0;
	
	public GuiFoodBook(EntityPlayer player) {
		super();
		
		foodCapability = FoodCapability.get(player);
		foodLog = foodCapability.getHistory();
		pageCount = (foodLog.size() + foodsPerPage - 1) / foodsPerPage;
		System.out.println("page count: " + pageCount + ", foods per page:" + foodsPerPage);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		centerX = width / 2;
		centerY = height / 2;
		leftEdge = centerX - (textureWidth) / 2;
		topEdge = centerY - (textureHeight) / 2;
		
		buttonList.clear();
		
		int pageFlipButtonSpacing = 50;
		prevPageButton = addButton(new NextPageButton(1, centerX - pageFlipButtonSpacing / 2 - NextPageButton.width, topEdge + 150, false));
		nextPageButton = addButton(new NextPageButton(2, centerX + pageFlipButtonSpacing / 2, topEdge + 150, true));
		
		updateButtonVisibility();
	}
	
	private void updateButtonVisibility() {
		this.nextPageButton.visible = currentPage < pageCount - 1;
		this.prevPageButton.visible = currentPage > 0;
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(backgroundTexture);
		drawTexturedModalRect(leftEdge, topEdge, 0, 0, textureWidth, textureHeight);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		String title = localized("gui", "food_book.title");
		drawCenteredString(title, centerX, topEdge + 16, 0x000000);
		
		String header = localized("gui", "food_book.header", foodLog.size());
		drawCenteredString(header, centerX, topEdge + 30, 0x000000);
		
		drawCenteredString("" + (currentPage + 1), centerX, topEdge + 154, 0x000000);
		
		renderFoodOnPage(mouseX, mouseY);
	}
	
	private void drawCenteredString(String text, int x, int y, int color) {
		fontRenderer.drawString(text, x - fontRenderer.getStringWidth(text) / 2, y, color);
	}
	
	private void renderFoodOnPage(int mouseX, int mouseY) {
		int startIndex = currentPage * foodsPerPage;
		int endIndex = Math.min(foodLog.size(), startIndex + foodsPerPage); // well, 1 past the end
		
		int size = 16;
		int spacing = size + 4;
		int minX = centerX - spacing * foodsPerRow / 2;
		int minY = centerY - spacing * rowsPerPage / 2;
		
		Optional<ItemStack> hoveredFood = Optional.empty();
		for (int i = startIndex; i < endIndex; i++) {
			FoodInstance food = foodLog.get(i);
			int x = minX + spacing * (i % foodsPerRow);
			int y = minY + spacing * ((i / foodsPerRow) % rowsPerPage);
			
			ItemStack itemStack = food.getItemStack();
			itemRender.renderItemIntoGUI(itemStack, x, y);
			
			if (x <= mouseX && mouseX < x + size && y <= mouseY && mouseY < y + size) {
				hoveredFood = Optional.of(itemStack);
			}
		}
		
		hoveredFood.ifPresent(food -> renderToolTip(food, mouseX, mouseY));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (!button.enabled) return;
		
		if (button == doneButton) {
			mc.player.closeScreen();
		} else if (button == prevPageButton) {
			currentPage--;
			updateButtonVisibility();
		} else if (button == nextPageButton) {
			currentPage++;
			updateButtonVisibility();
		}
	}
	
	@SideOnly(Side.CLIENT)
	private static class NextPageButton extends GuiButton {
		private static final int width = 23;
		private static final int height = 13;
		
		private final boolean isForward;
		
		private NextPageButton(int buttonId, int x, int y, boolean isForward) {
			super(buttonId, x, y, 23, 13, "");
			this.isForward = isForward;
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (!visible) return;
			
			int textureX = 0;
			int textureY = 192;
			
			boolean isHovered = x <= mouseX && mouseX < x + width && y <= mouseY && mouseY < y + height;
			if (isHovered) {
				textureX += 23;
			}
			
			if (!isForward) {
				textureY += 13;
			}
			
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			mc.getTextureManager().bindTexture(GuiFoodBook.backgroundTexture);
			drawTexturedModalRect(x, y, textureX, textureY, 23, 13);
		}
	}
}

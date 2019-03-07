package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.capability.FoodInstance;
import com.cazsius.solcarrot.lib.FoodItemStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.stream.Collectors;

import static com.cazsius.solcarrot.lib.Localization.localized;

@SideOnly(Side.CLIENT)
public final class GuiFoodBook extends GuiScreen {
	private static final ResourceLocation backgroundTexture = new ResourceLocation("textures/gui/book.png");
	private static final int textureWidth = 192;
	private static final int textureHeight = 192;
	
	private static final int itemsPerRow = 5;
	private static final int rowsPerPage = 5;
	private static final int itemsPerPage = itemsPerRow * rowsPerPage;
	
	private int leftEdge;
	private int topEdge;
	private int centerX;
	private int centerY;
	
	private NextPageButton nextPageButton;
	private NextPageButton prevPageButton;
	
	private EntityPlayer player;
	
	private final List<Page> pages = new ArrayList<>();
	private int currentPageNumber = 0;
	
	public GuiFoodBook(EntityPlayer player) {
		this.player = player;
	}
	
	private List<ItemListPage> pages(String header, List<ItemStack> items) {
		List<ItemListPage> pages = new ArrayList<>();
		for (int startIndex = 0; startIndex < items.size(); startIndex += itemsPerPage) {
			int endIndex = Math.min(startIndex + itemsPerPage, items.size());
			pages.add(new ItemListPage(header, items.subList(startIndex, endIndex)));
		}
		return pages;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		initPages();
		
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
	
	private void initPages() {
		pages.clear();
		
		FoodCapability foodCapability = FoodCapability.get(player);
		// sort by name, using metadata as tiebreaker
		List<ItemStack> eatenFoods = foodCapability.getHistory().stream()
				.map(FoodInstance::getItemStack)
				// sort by name, using metadata as tiebreaker
				.sorted(Comparator.comparing(ItemStack::getMetadata))
				.sorted(Comparator.comparing(food -> I18n.format(food.getTranslationKey() + ".name")))
				.collect(Collectors.toList());
		String eatenFoodsHeader = localized("gui", "food_book.eaten_foods", eatenFoods.size());
		pages.addAll(pages(eatenFoodsHeader, eatenFoods));
		
		List<ItemStack> uneatenFoods = FoodItemStacks.getAllFoods().stream()
				.filter(food -> !foodCapability.hasEaten(food))
				.collect(Collectors.toList());
		String uneatenFoodsHeader = localized("gui", "food_book.uneaten_foods", uneatenFoods.size());
		pages.addAll(pages(uneatenFoodsHeader, uneatenFoods));
	}
	
	private void updateButtonVisibility() {
		nextPageButton.visible = currentPageNumber < pages.size() - 1;
		prevPageButton.visible = currentPageNumber > 0;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(backgroundTexture);
		drawTexturedModalRect(leftEdge, topEdge, 0, 0, textureWidth, textureHeight);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		// book title
		String title = localized("gui", "food_book.title");
		drawCenteredString(title, centerX, topEdge + 16, 0x000000);
		
		// page number
		drawCenteredString("" + (currentPageNumber + 1), centerX, topEdge + 154, 0x000000);
		
		// current page
		pages.get(currentPageNumber).render(mouseX, mouseY);
	}
	
	private void drawCenteredString(String text, int x, int y, int color) {
		fontRenderer.drawString(text, x - fontRenderer.getStringWidth(text) / 2, y, color);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (!button.enabled) return;
		
		if (button == prevPageButton) {
			currentPageNumber--;
			updateButtonVisibility();
		} else if (button == nextPageButton) {
			currentPageNumber++;
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
	
	private abstract class Page {
		private String header;
		
		private Page(String header) {
			this.header = header;
		}
		
		void render(int mouseX, int mouseY) {
			// draw title
			drawCenteredString(header, centerX, topEdge + 30, 0x000000);
		}
	}
	
	private class ItemListPage extends Page {
		private static final int itemSize = 16;
		private static final int itemSpacing = itemSize + 4;
		
		private List<ItemStack> items;
		
		private ItemListPage(String header, List<ItemStack> items) {
			super(header);
			this.items = items;
		}
		
		@Override
		void render(int mouseX, int mouseY) {
			super.render(mouseX, mouseY);
			
			int minX = centerX - itemSpacing * itemsPerRow / 2;
			int minY = centerY - itemSpacing * rowsPerPage / 2;
			
			Optional<ItemStack> hoveredItem = Optional.empty();
			for (int i = 0; i < items.size(); i++) {
				ItemStack itemStack = items.get(i);
				int x = minX + itemSpacing * (i % itemsPerRow);
				int y = minY + itemSpacing * ((i / itemsPerRow) % rowsPerPage);
				
				itemRender.renderItemIntoGUI(itemStack, x, y);
				
				if (x <= mouseX && mouseX < x + itemSize && y <= mouseY && mouseY < y + itemSize) {
					hoveredItem = Optional.of(itemStack);
				}
			}
			
			hoveredItem.ifPresent(item -> renderToolTip(item, mouseX, mouseY));
		}
	}
}

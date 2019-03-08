package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.capability.*;
import com.cazsius.solcarrot.lib.FoodItemStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.stream.Collectors;

import static com.cazsius.solcarrot.lib.Localization.localized;

@SideOnly(Side.CLIENT)
public final class GuiFoodBook extends GuiScreen {
	private static final ResourceLocation texture = SOLCarrot.resourceLocation("textures/gui/food_book.png");
	private static final int textureWidth = 186;
	private static final int textureHeight = 192;
	
	private static final int itemsPerRow = 5;
	private static final int rowsPerPage = 5;
	private static final int itemsPerPage = itemsPerRow * rowsPerPage;
	
	private int leftEdge;
	private int topEdge;
	private int centerX;
	private int centerY;
	
	private int mouseX;
	private int mouseY;
	
	private NextPageButton nextPageButton;
	private NextPageButton prevPageButton;
	
	private FoodCapability foodCapability;
	private EntityPlayer player;
	private List<ItemStack> eatenFoods;
	private List<ItemStack> uneatenFoods;
	
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
		
		foodCapability = FoodCapability.get(player);
		
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
		
		pages.add(new StatListPage());
		
		// sort by name, using metadata as tiebreaker
		eatenFoods = foodCapability.getEatenFoods().stream()
				.map(FoodInstance::getItemStack)
				// sort by name, using metadata as tiebreaker
				.sorted(Comparator.comparing(ItemStack::getMetadata))
				.sorted(Comparator.comparing(food -> I18n.format(food.getTranslationKey() + ".name")))
				.collect(Collectors.toList());
		String eatenFoodsHeader = localized("gui", "food_book.eaten_foods", eatenFoods.size());
		pages.addAll(pages(eatenFoodsHeader, eatenFoods));
		
		uneatenFoods = FoodItemStacks.getAllFoods().stream()
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
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		
		drawDefaultBackground();
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(leftEdge, topEdge, 0, 0, textureWidth, textureHeight);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		// book title
		String title = localized("gui", "food_book.title");
		drawCenteredString(title, centerX, topEdge + 16, 0x000000);
		
		// page number
		drawCenteredString("" + (currentPageNumber + 1), centerX, topEdge + 154, 0x000000);
		
		// current page
		pages.get(currentPageNumber).render();
	}
	
	private void drawCenteredString(String text, int x, int y, int color) {
		int width = fontRenderer.getStringWidth(text) - 1;
		fontRenderer.drawString(text, x - width / 2, y, color);
	}
	
	@Override
	protected void drawVerticalLine(int x, int startY, int endY, int color) {
		// the vanilla method increments the wrong number >_>
		super.drawVerticalLine(x, startY - 1, endY + 1, color);
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
	
	private boolean isMouseInRect(int x, int y, int width, int height) {
		return x <= mouseX && mouseX < x + width
				&& y <= mouseY && mouseY < y + height;
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
			mc.getTextureManager().bindTexture(texture);
			drawTexturedModalRect(x, y, textureX, textureY, 23, 13);
		}
	}
	
	private abstract class Page {
		private String header;
		
		private Page(String header) {
			this.header = header;
		}
		
		void render() {
			// draw title
			drawCenteredString(header, centerX, topEdge + 30, 0x000000);
		}
	}
	
	private class StatListPage extends Page {
		private static final int fullBlack = 0xFF_000000;
		private static final int lessBlack = 0x88_000000;
		private static final int leastBlack = 0x44_000000;
		
		private Optional<String> tooltip = Optional.empty();
		private ProgressInfo progressInfo;
		
		private StatListPage() {
			super(localized("gui", "food_book.stats"));
		}
		
		@Override
		void render() {
			super.render();
			tooltip = Optional.empty();
			progressInfo = foodCapability.getProgressInfo();
			
			renderProgressDiagram();
			
			String foodsTasted = localized("gui", "food_book.stats.foods_tasted",
					eatenFoods.size(),
					eatenFoods.size() + uneatenFoods.size()
			);
			drawCenteredString(foodsTasted, centerX, topEdge + 100, fullBlack);
			
			String heartsGained = localized("gui", "food_book.stats.hearts_gained",
					progressInfo.milestonesAchieved(),
					progressInfo.heartsPerMilestone * progressInfo.milestones.length
			);
			drawCenteredString(heartsGained, centerX, topEdge + 120, fullBlack);
			
			tooltip.ifPresent(text -> {
				GuiUtils.drawHoveringText(
						ItemStack.EMPTY,
						Collections.singletonList(text),
						mouseX, mouseY,
						width, height,
						-1,
						fontRenderer
				);
			});
		}
		
		private void drawHeart(int x, int y, boolean isOpaque) {
			GlStateManager.enableBlend();
			mc.getTextureManager().bindTexture(texture);
			GlStateManager.color(1.0F, 1.0F, 1.0F, isOpaque ? 1.0F : 0.5F);
			drawTexturedModalRect(x, y, 0, 224, 9, 9);
		}
		
		private void drawCarrot(int x, int y) {
			mc.getTextureManager().bindTexture(texture);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			drawTexturedModalRect(x, y, 0, 240, 16, 16);
		}
		
		private void drawMilestoneHearts(int x, int y, boolean isOpaque) {
			int heartsPerMilestone = progressInfo.heartsPerMilestone;
			
			int width;
			if (heartsPerMilestone <= 3) {
				int spacing = 8;
				width = 9 + spacing * (heartsPerMilestone - 1);
				for (int i = 0; i < heartsPerMilestone; i++) {
					drawHeart(x - width / 2 + spacing * i, y, isOpaque);
				}
			} else {
				String label = "" + heartsPerMilestone;
				int labelWidth = fontRenderer.getStringWidth(label);
				int spacing = 1;
				width = 9 + spacing + labelWidth;
				int left = x - width / 2;
				drawHeart(left + labelWidth + spacing, y, isOpaque);
				fontRenderer.drawString(label, left, y + 1, isOpaque ? fullBlack : lessBlack);
			}
			
			if (isMouseInRect(x - width / 2, y, width, 9)) {
				tooltip = Optional.of(localized("gui", "food_book.stats.tooltip.hearts_per_milestone"));
			}
		}
		
		private void drawCenteredLabel(String text, String tooltip, int x, int y, int color) {
			drawCenteredString(text, x, y, color);
			
			int width = fontRenderer.getStringWidth(text) - 1;
			if (isMouseInRect(x - width / 2, y, width, 8)) {
				this.tooltip = Optional.of(tooltip);
			}
		}
		
		private void renderProgressDiagram() {
			int lineY = topEdge + 72;
			int segmentLength = 48;
			int leftEdge = centerX - segmentLength * 3 / 4;
			int leftPoint = centerX - segmentLength / 2;
			int rightPoint = centerX + segmentLength / 2;
			int rightEdge = centerX + segmentLength * 3 / 4;
			int padding = 4;
			
			int milestonesAchieved = progressInfo.milestonesAchieved();
			int previousMilestone = milestonesAchieved > 0 ? progressInfo.milestones[milestonesAchieved - 1] : 0;
			int nextMilestone = progressInfo.nextMilestone();
			boolean hasReachedMax = progressInfo.hasReachedMax();
			boolean hasSurpassedMax = hasReachedMax && progressInfo.foodsEaten > previousMilestone;
			boolean isNextMilestoneMax = milestonesAchieved + 1 == progressInfo.milestones.length;
			int progress = segmentLength * (progressInfo.foodsEaten - previousMilestone) / (nextMilestone - previousMilestone);
			int progressX = leftPoint + (hasSurpassedMax ? segmentLength : progress);
			
			drawCarrot(leftEdge - padding - 16, lineY - 8);
			
			if (milestonesAchieved > 0) {
				drawHorizontalLine(leftEdge, leftPoint, lineY, fullBlack);
				drawMilestoneHearts(leftPoint, lineY - 23, true);
			}
			
			drawVerticalLine(leftPoint, lineY - 2, lineY - 1, fullBlack);
			drawCenteredLabel(
					"" + previousMilestone,
					localized("gui", "food_book.stats.tooltip.previous_milestone"),
					leftPoint, lineY - 10,
					fullBlack
			);
			
			drawHorizontalLine(leftPoint, progressX, lineY, fullBlack);
			
			if (!hasSurpassedMax) {
				// all the edge cases!
				drawHorizontalLine(progressX + 1, rightPoint, lineY, hasReachedMax ? leastBlack : lessBlack);
			}
			
			if (!hasReachedMax) {
				drawVerticalLine(rightPoint, lineY - 2, lineY - 1, lessBlack);
				GlStateManager.enableBlend();
				drawCenteredLabel(
						"" + nextMilestone,
						localized("gui", "food_book.stats.tooltip.next_milestone"),
						rightPoint, lineY - 10,
						lessBlack
				);
				drawMilestoneHearts(rightPoint, lineY - 23, false);
			}
			
			boolean isMaxInSight = !hasReachedMax && !isNextMilestoneMax;
			drawHorizontalLine(rightPoint + 1, rightEdge, lineY, isMaxInSight ? lessBlack : leastBlack);
			
			drawVerticalLine(progressX, lineY + 1, lineY + 5, fullBlack);
			
			GlStateManager.enableBlend();
			drawCenteredLabel(
					"" + progressInfo.foodsEaten,
					localized("gui", "food_book.stats.tooltip.foods_tasted"),
					progressX, lineY + 7,
					fullBlack
			);
			
			int totalFoods = eatenFoods.size() + uneatenFoods.size();
			if (!uneatenFoods.isEmpty()) {
				drawCenteredLabel(
						"" + totalFoods,
						localized("gui", "food_book.stats.tooltip.total_foods"),
						rightEdge + padding + 8, lineY - 4,
						leastBlack
				);
			}
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
		void render() {
			super.render();
			
			int minX = centerX - itemSpacing * itemsPerRow / 2;
			int minY = centerY - itemSpacing * rowsPerPage / 2;
			
			Optional<ItemStack> hoveredItem = Optional.empty();
			for (int i = 0; i < items.size(); i++) {
				ItemStack itemStack = items.get(i);
				int x = minX + itemSpacing * (i % itemsPerRow);
				int y = minY + itemSpacing * ((i / itemsPerRow) % rowsPerPage);
				
				itemRender.renderItemIntoGUI(itemStack, x, y);
				
				if (isMouseInRect(x, y, itemSize, itemSize)) {
					hoveredItem = Optional.of(itemStack);
				}
			}
			
			hoveredItem.ifPresent(item -> renderToolTip(item, mouseX, mouseY));
		}
	}
}

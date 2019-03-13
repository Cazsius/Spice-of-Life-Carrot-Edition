package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.capability.*;
import com.cazsius.solcarrot.client.gui.elements.*;
import com.cazsius.solcarrot.lib.FoodItemStacks;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static com.cazsius.solcarrot.lib.Localization.localized;

@SideOnly(Side.CLIENT)
public final class GuiFoodBook extends GuiScreen {
	private static final ResourceLocation texture = SOLCarrot.resourceLocation("textures/gui/food_book.png");
	private static final UIImage.Image bookImage = new UIImage.Image(texture, new Rectangle(0, 0, 186, 192));
	
	private final List<UIElement> elements = new ArrayList<>();
	private UIImage background;
	private UILabel pageNumberLabel;
	
	private PageFlipButton nextPageButton;
	private PageFlipButton prevPageButton;
	
	private EntityPlayer player;
	private FoodCapability foodCapability;
	
	private final List<Page> pages = new ArrayList<>();
	private int currentPageNumber = 0;
	
	public GuiFoodBook(EntityPlayer player) {
		this.player = player;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		foodCapability = FoodCapability.get(player);
		
		background = new UIImage(bookImage);
		background.setCenterX(width / 2);
		background.setCenterY(height / 2);
		
		elements.clear();
		
		// book title
		UILabel titleLabel = new UILabel(localized("gui", "food_book.title"));
		titleLabel.setCenterX(background.getCenterX());
		titleLabel.setMinY(background.getMinY() + 16);
		elements.add(titleLabel);
		
		// page number
		pageNumberLabel = new UILabel("1");
		pageNumberLabel.setCenterX(background.getCenterX());
		pageNumberLabel.setMinY(background.getMinY() + 154);
		elements.add(pageNumberLabel);
		
		initPages();
		
		buttonList.clear();
		
		int pageFlipButtonSpacing = 50;
		prevPageButton = addButton(new PageFlipButton(1, background.getCenterX() - pageFlipButtonSpacing / 2 - PageFlipButton.width, background.getMinY() + 150, PageFlipButton.Direction.BACKWARD));
		nextPageButton = addButton(new PageFlipButton(2, background.getCenterX() + pageFlipButtonSpacing / 2, background.getMinY() + 150, PageFlipButton.Direction.FORWARD));
		
		updateButtonVisibility();
	}
	
	private void initPages() {
		pages.clear();
		
		ProgressInfo progressInfo = foodCapability.getProgressInfo();
		pages.add(new StatListPage(background.frame, progressInfo));
		
		List<ItemStack> eatenFoods = foodCapability.getEatenFoods().stream()
			.map(FoodInstance::getItemStack)
			// sort by name, using metadata as tiebreaker
			.sorted(Comparator.comparing(ItemStack::getMetadata))
			.sorted(Comparator.comparing(food -> I18n.format(food.getTranslationKey() + ".name")))
			.collect(Collectors.toList());
		String eatenFoodsHeader = localized("gui", "food_book.eaten_foods", eatenFoods.size());
		pages.addAll(ItemListPage.pages(background.frame, eatenFoodsHeader, eatenFoods));
		
		if (progressInfo.showUneatenFoods) {
			List<ItemStack> uneatenFoods = FoodItemStacks.getAllFoods().stream()
				.filter(food -> !foodCapability.hasEaten(food))
				.collect(Collectors.toList());
			String uneatenFoodsHeader = localized("gui", "food_book.uneaten_foods", uneatenFoods.size());
			pages.addAll(ItemListPage.pages(background.frame, uneatenFoodsHeader, uneatenFoods));
		}
	}
	
	private void updateButtonVisibility() {
		nextPageButton.visible = currentPageNumber < pages.size() - 1;
		prevPageButton.visible = currentPageNumber > 0;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		UIElement.render(background, mouseX, mouseY);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		// current page
		UIElement.render(elements, mouseX, mouseY);
		UIElement.render(pages.get(currentPageNumber), mouseX, mouseY);
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
		
		pageNumberLabel.text = "" + (currentPageNumber + 1);
	}
}

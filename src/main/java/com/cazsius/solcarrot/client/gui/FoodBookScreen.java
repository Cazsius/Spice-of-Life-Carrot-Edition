package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.client.gui.elements.*;
import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.cazsius.solcarrot.lib.Localization.localized;

@OnlyIn(Dist.CLIENT)
public final class FoodBookScreen extends Screen implements PageFlipButton.Pageable {
	private static final ResourceLocation texture = SOLCarrot.resourceLocation("textures/gui/food_book.png");
	private static final UIImage.Image bookImage = new UIImage.Image(texture, new Rectangle(0, 0, 186, 192));
	static final UIImage.Image carrotImage = new UIImage.Image(texture, new Rectangle(0, 240, 16, 16));
	static final UIImage.Image spiderEyeImage = new UIImage.Image(texture, new Rectangle(16, 240, 16, 16));
	static final UIImage.Image heartImage = new UIImage.Image(texture, new Rectangle(0, 224, 15, 15));
	static final UIImage.Image drumstickImage = new UIImage.Image(texture, new Rectangle(16, 224, 15, 15));
	static final UIImage.Image blacklistImage = new UIImage.Image(texture, new Rectangle(32, 224, 15, 15));
	static final UIImage.Image whitelistImage = new UIImage.Image(texture, new Rectangle(48, 224, 15, 15));
	
	static final Color fullBlack = Color.BLACK;
	static final Color lessBlack = new Color(0, 0, 0, 128);
	static final Color leastBlack = new Color(0, 0, 0, 64);
	
	private final List<UIElement> elements = new ArrayList<>();
	private UIImage background;
	private UILabel pageNumberLabel;
	
	private PageFlipButton nextPageButton;
	private PageFlipButton prevPageButton;
	
	private PlayerEntity player;
	private FoodData foodData;
	
	private final List<Page> pages = new ArrayList<>();
	private int currentPageNumber = 0;
	
	public static void open(PlayerEntity player) {
		Minecraft.getInstance().displayGuiScreen(new FoodBookScreen(player));
	}
	
	public FoodBookScreen(PlayerEntity player) {
		super(new StringTextComponent(""));
		this.player = player;
	}
	
	@Override
	public void init() {
		super.init();
		
		foodData = new FoodData(FoodList.get(player));
		
		background = new UIImage(bookImage);
		background.setCenterX(width / 2);
		background.setCenterY(height / 2);
		
		elements.clear();
		
		// page number
		pageNumberLabel = new UILabel("1");
		pageNumberLabel.setCenterX(background.getCenterX());
		pageNumberLabel.setMinY(background.getMinY() + 156);
		elements.add(pageNumberLabel);
		
		initPages();
		
		int pageFlipButtonSpacing = 50;
		prevPageButton = addButton(new PageFlipButton(
			background.getCenterX() - pageFlipButtonSpacing / 2 - PageFlipButton.width,
			background.getMinY() + 152,
			PageFlipButton.Direction.BACKWARD,
			this
		));
		nextPageButton = addButton(new PageFlipButton(
			background.getCenterX() + pageFlipButtonSpacing / 2,
			background.getMinY() + 152,
			PageFlipButton.Direction.FORWARD,
			this
		));
		
		updateButtonVisibility();
	}
	
	private void initPages() {
		pages.clear();
		
		pages.add(new StatListPage(foodData, background.frame));
		
		pages.add(new ConfigInfoPage(foodData, background.frame));
		
		addPages("eaten_foods", foodData.eatenFoods);
		
		if (SOLCarrotConfig.shouldShowUneatenFoods()) {
			addPages("uneaten_foods", foodData.uneatenFoods);
		}
	}
	
	private void addPages(String headerLocalizationPath, List<Item> items) {
		String header = localized("gui", "food_book." + headerLocalizationPath, items.size());
		List<ItemStack> stacks = items.stream().map(ItemStack::new).collect(Collectors.toList());
		pages.addAll(ItemListPage.pages(background.frame, header, stacks));
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderBackground();
		
		UIElement.render(background, mouseX, mouseY);
		
		super.render(mouseX, mouseY, partialTicks);
		
		// current page
		UIElement.render(elements, mouseX, mouseY);
		UIElement.render(pages.get(currentPageNumber), mouseX, mouseY);
	}
	
	@Override
	public void switchToPage(int pageNumber) {
		if (!isWithinRange(pageNumber)) return;
		
		currentPageNumber = pageNumber;
		updateButtonVisibility();
		
		pageNumberLabel.text = "" + (currentPageNumber + 1);
		
		prevPageButton.updateState();
		nextPageButton.updateState();
	}
	
	@Override
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}
	
	@Override
	public boolean isWithinRange(int pageNumber) {
		return pageNumber > 0 && pageNumber < pages.size();
	}
	
	private void updateButtonVisibility() {
		prevPageButton.updateState();
		nextPageButton.updateState();
	}
}

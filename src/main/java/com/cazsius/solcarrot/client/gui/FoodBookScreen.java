package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.client.gui.elements.*;
import com.cazsius.solcarrot.tracking.FoodList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
	private static final ImageData bookImage = new ImageData(texture,
		new Rectangle(0, 0, 186, 192)
	);
	static final ImageData carrotImage = new ImageData(texture,
		new Rectangle(0, 240, 16, 16),
		14, 14
	);
	static final ImageData spiderEyeImage = new ImageData(texture,
		new Rectangle(16, 240, 16, 16),
		8, 8
	);
	static final ImageData heartImage = new ImageData(texture,
		new Rectangle(0, 224, 15, 15),
		9, 9
	);
	static final ImageData drumstickImage = new ImageData(texture,
		new Rectangle(16, 224, 15, 15),
		9, 9
	);
	static final ImageData blacklistImage = new ImageData(texture,
		new Rectangle(32, 224, 15, 15),
		10, 12
	);
	static final ImageData whitelistImage = new ImageData(texture,
		new Rectangle(48, 224, 15, 15),
		10, 12
	);
	
	static final Color fullBlack = Color.BLACK;
	static final Color lessBlack = new Color(0, 0, 0, 128);
	static final Color leastBlack = new Color(0, 0, 0, 64);
	
	private final List<UIElement> elements = new ArrayList<>();
	private UIImage background;
	private UILabel pageNumberLabel;
	
	private PageFlipButton nextPageButton;
	private PageFlipButton prevPageButton;
	
	private Player player;
	private FoodData foodData;
	
	private final List<Page> pages = new ArrayList<>();
	private int currentPageNumber = 0;
	
	public static void open(Player player) {
		Minecraft.getInstance().setScreen(new FoodBookScreen(player));
	}
	
	public FoodBookScreen(Player player) {
		super(TextComponent.EMPTY);
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
		prevPageButton = addRenderableWidget(new PageFlipButton(
			background.getCenterX() - pageFlipButtonSpacing / 2 - PageFlipButton.width,
			background.getMinY() + 152,
			PageFlipButton.Direction.BACKWARD,
			this
		));
		nextPageButton = addRenderableWidget(new PageFlipButton(
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
	public void render(PoseStack matrices, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrices);
		
		UIElement.render(matrices, background, mouseX, mouseY);
		
		super.render(matrices, mouseX, mouseY, partialTicks);
		
		if (!pages.isEmpty()) { // might not be loaded yet; race condition
			// current page
			UIElement.render(matrices, elements, mouseX, mouseY);
			UIElement.render(matrices, pages.get(currentPageNumber), mouseX, mouseY);
		}
	}
	
	@Override
	public void switchToPage(int pageNumber) {
		if (!isWithinRange(pageNumber)) return;
		
		currentPageNumber = pageNumber;
		updateButtonVisibility();
		
		pageNumberLabel.text = "" + (currentPageNumber + 1);
	}
	
	@Override
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}
	
	@Override
	public boolean isWithinRange(int pageNumber) {
		return pageNumber >= 0 && pageNumber < pages.size();
	}
	
	private void updateButtonVisibility() {
		prevPageButton.updateState();
		nextPageButton.updateState();
	}
}

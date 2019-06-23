package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.client.gui.elements.*;
import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.cazsius.solcarrot.lib.Localization.localized;

@SideOnly(Side.CLIENT)
public final class GuiFoodBook extends GuiScreen {
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
	
	private EntityPlayer player;
	private FoodData foodData;
	
	private final List<Page> pages = new ArrayList<>();
	private int currentPageNumber = 0;
	
	public GuiFoodBook(EntityPlayer player) {
		this.player = player;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
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
		
		buttonList.clear();
		
		int pageFlipButtonSpacing = 50;
		prevPageButton = addButton(new PageFlipButton(1, background.getCenterX() - pageFlipButtonSpacing / 2 - PageFlipButton.width, background.getMinY() + 152, PageFlipButton.Direction.BACKWARD));
		nextPageButton = addButton(new PageFlipButton(2, background.getCenterX() + pageFlipButtonSpacing / 2, background.getMinY() + 152, PageFlipButton.Direction.FORWARD));
		
		updateButtonVisibility();
	}
	
	private void initPages() {
		pages.clear();
		
		pages.add(new StatListPage(foodData, background.frame));
		
		pages.add(new ConfigInfoPage(foodData, background.frame));
		
		addPages("eaten_foods", foodData.eatenFoods);
		
		if (foodData.progressInfo.configInfo.shouldShowUneatenFoods) {
			addPages("uneaten_foods", foodData.uneatenFoods);
		}
	}
	
	private void addPages(String headerLocalizationPath, List<ItemStack> items) {
		String header = localized("gui", "food_book." + headerLocalizationPath, items.size());
		pages.addAll(ItemListPage.pages(background.frame, header, items));
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

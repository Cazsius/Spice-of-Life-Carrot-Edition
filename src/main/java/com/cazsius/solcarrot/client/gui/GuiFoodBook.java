package com.cazsius.solcarrot.client.gui;

import org.lwjgl.input.Keyboard;

import com.cazsius.solcarrot.item.ItemFoodBook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiFoodBook extends GuiScreen {

	private static final ResourceLocation FOOD_BOOK_GUI_TEXTURES = new ResourceLocation("textures/gui/book.png");
	
	private GuiFoodBook.NextPageButton buttonNextPage;
	private GuiFoodBook.NextPageButton buttonPreviousPage;
	
    private int bookTotalPages = 1;
    private int currPage;

	public GuiFoodBook(ItemFoodBook food_book) {
		super();
	}

	public void initGui() {
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);

		int i = (this.width - 192) / 2;
		this.buttonNextPage = (GuiFoodBook.NextPageButton) this
				.addButton(new GuiFoodBook.NextPageButton(1, i + 120, 156, true));
		this.buttonPreviousPage = (GuiFoodBook.NextPageButton) this
				.addButton(new GuiFoodBook.NextPageButton(2, i + 38, 156, false));
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(FOOD_BOOK_GUI_TEXTURES);
		int i = (this.width - 192) / 2;
		this.drawTexturedModalRect(i, 2, 0, 0, 192, 192);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@SideOnly(Side.CLIENT)
	static class NextPageButton extends GuiButton {
		private final boolean isForward;

		public NextPageButton(int buttonId, int x, int y, boolean isForwardIn) {
			super(buttonId, x, y, 23, 13, "");
			this.isForward = isForwardIn;
		}

		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
						&& mouseY < this.y + this.height;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(GuiFoodBook.FOOD_BOOK_GUI_TEXTURES);
				int i = 0;
				int j = 192;

				if (flag) {
					i += 23;
				}

				if (!this.isForward) {
					j += 13;
				}

				this.drawTexturedModalRect(this.x, this.y, i, j, 23, 13);
			}
		}
	}

}

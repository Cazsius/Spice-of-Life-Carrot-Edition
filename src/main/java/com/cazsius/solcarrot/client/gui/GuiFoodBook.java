package com.cazsius.solcarrot.client.gui;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.capability.FoodInstance;
import com.cazsius.solcarrot.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class GuiFoodBook extends GuiScreen {

    private GuiFoodBook.NextPageButton buttonNextPage;
    private GuiFoodBook.NextPageButton buttonPreviousPage;
    private GuiButton buttonDone;

    private final FoodCapability foodCapability = new FoodCapability();
    private final EntityPlayer usingPlayer;

    private int bookTotalPages = 2;
    private int currPage;
    private int totalFoods;
    private NBTTagList bookPages;
    private int foodPosX = (width - 150) / 2;
    private int foodPosY = (height - 170) / 2;

    private static final ResourceLocation FOOD_BOOK_GUI_TEXTURES = new ResourceLocation("textures/gui/book.png");

    public GuiFoodBook(EntityPlayer player) {
        super();
        usingPlayer = player;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        int i = (this.width - 192) / 2;
        this.buttonDone = this.addButton(new GuiButton(0, this.width / 2 - 100, 196, 200, 20, I18n.format("gui.done")));
        this.buttonNextPage = this.addButton(new GuiFoodBook.NextPageButton(1, i + 120, 156, true));
        this.buttonPreviousPage = this.addButton(new GuiFoodBook.NextPageButton(2, i + 38, 156, false));
        this.updateButtons();
    }

    public void renderFoodOnPage(EntityPlayer player, int posX, int posY) {
        Set setFoodList =  player.getCapability(this.foodCapability.FOOD_CAPABILITY, null).foodList;
        List<FoodInstance> foodList = new ArrayList<>(setFoodList);
        int foodPerRow = 0;
        for(int i = 0; i < foodList.size(); i++) {
            totalFoods++;
            foodPerRow++;
            posX = posX + 16;
            ItemStack foodStack = new ItemStack(foodList.get(i).item());
            this.itemRender.renderItemIntoGUI(foodStack, posX, posY);
            if(foodPerRow >= 7) {
                foodPerRow = 0;
                posY = posY + 20;
                posX = foodPosX;
            }
        }
    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(FOOD_BOOK_GUI_TEXTURES);
        int i = (this.width - 192) / 2;
        int j = 2;
        this.drawTexturedModalRect(i, 2, 0, 0, 192, 192);

        this.foodPosX = (width - 150) / 2;
        this.foodPosY = (height - 170) / 2;
        this.renderFoodOnPage(this.usingPlayer, foodPosX, foodPosY);

        Minecraft.getMinecraft().fontRenderer.drawString("My Food Log", 180, 25, 000000, false);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }



    private void updateButtons()
    {
        this.buttonNextPage.visible = (this.currPage < this.bookTotalPages - 1);
        this.buttonPreviousPage.visible = this.currPage > 0;
        this.buttonDone.visible = true;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        if (button.enabled) {
            //buttonDone
            if(button.id == 0) {
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            //buttonNextPage
            else if (button.id == 1) {
                if (this.currPage < this.bookTotalPages - 1) {
                    ++this.currPage;
                } else if (totalFoods >= 35) {
                    this.addNewPage();
                }
            }
            //butonPreviousPage
            else if (button.id == 2) {
                if (this.currPage > 0) {
                    --this.currPage;
                }
            }
        }
    }

    //Add new Page if X amount of Foods are Displayed
    private void addNewPage() {
        if (this.bookPages != null && this.totalFoods >= 35) {
            this.bookPages.appendTag(new NBTTagString(""));
            ++this.bookTotalPages;
        }
    }

    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton
    {
        private final boolean isForward;

        public NextPageButton(int buttonId, int x, int y, boolean isForwardIn)
        {
            super(buttonId, x, y, 23, 13, "");
            this.isForward = isForwardIn;
        }

        /**
         * Draws this button to the screen.
         */
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
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

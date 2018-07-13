package com.cazsius.solcarrot.item;

import com.cazsius.solcarrot.client.gui.GuiFoodBook;
import com.cazsius.solcarrot.common.CommonProxy;
import com.cazsius.solcarrot.lib.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemFoodBook extends Item {

    public ItemFoodBook() {
        super();
        this.setCreativeTab(CreativeTabs.MISC);
        this.setRegistryName("food_book");
        this.setUnlocalizedName(Constants.MOD_ID + "." + "food_book".replace("_", "."));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiFoodBook(playerIn));
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}

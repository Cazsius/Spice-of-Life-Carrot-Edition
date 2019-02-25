package com.cazsius.solcarrot.item;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.lib.Constants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ItemFoodBook extends Item {
    
    public static final int GUI_ID = 1;
    
    public ItemFoodBook() {
        super();
        this.setCreativeTab(CreativeTabs.MISC);
        this.setRegistryName("food_book");
        this.setUnlocalizedName(Constants.MOD_ID + "." + "food_book".replace("_", "."));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.openGui(SOLCarrot.instance, GUI_ID, world, (int) player.posX, (int) player.posY, (int) player.posZ);
        
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}

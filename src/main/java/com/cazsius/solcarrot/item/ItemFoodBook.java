package com.cazsius.solcarrot.item;

import com.cazsius.solcarrot.SOLCarrot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemFoodBook extends Item {

	public ItemFoodBook() {
		super();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		// OPEN GUI Here
		playerIn.openGui(SOLCarrot.instance, 1, worldIn, 0, 0, 0);
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

}

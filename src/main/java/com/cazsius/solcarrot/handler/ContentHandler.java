package com.cazsius.solcarrot.handler;

import com.cazsius.solcarrot.item.ItemFoodBook;
import com.cazsius.solcarrot.lib.ModUtils;

import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ContentHandler {

	// Items
	public static Item itemFoodBook;

	public static void initItems() {
		// itemLadderBuilder = ModUtils.registerItem(new ItemLadderBuilder(),
		// "ladder_builder");
		itemFoodBook = ModUtils.registerItem(new ItemFoodBook(), "food_book");

	}

	@SideOnly(Side.CLIENT)
	public static void onClientPreInit() {
		ModUtils.registerItemInvModel(itemFoodBook);
	}
}

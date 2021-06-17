package com.cazsius.solcarrot.item;

import com.cazsius.solcarrot.client.gui.FoodBookScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public final class FoodBookItem extends Item {
	public FoodBookItem() {
		super(new Properties().tab(ItemGroup.TAB_MISC));
	}
	
	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if (player.isLocalPlayer()) {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FoodBookScreen.open(player));
		}
		
		return new ActionResult<>(ActionResultType.SUCCESS, player.getItemInHand(hand));
	}
}

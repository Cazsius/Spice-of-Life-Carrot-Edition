package com.cazsius.solcarrot.item;

import com.cazsius.solcarrot.client.gui.FoodBookScreen;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;

public final class FoodBookItem extends Item {
	public FoodBookItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (player.isLocalPlayer() && FMLEnvironment.getDist().isClient()) {
			FoodBookScreen.open(player);
		}

		return super.use(level, player, hand);
	}
}

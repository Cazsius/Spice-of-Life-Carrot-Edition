package com.cazsius.solcarrot.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class DeathHandler {
	@SubscribeEvent
	public static void onClone(PlayerEvent.Clone event) {
		EntityPlayer player = event.getEntityPlayer();
		FoodCapability cap = player.getCapability(FoodCapability.FOOD_CAPABILITY, null);
		FoodCapability orig = event.getOriginal().getCapability(FoodCapability.FOOD_CAPABILITY, null);
		cap.deserializeNBT(orig.serializeNBT());
	}
}

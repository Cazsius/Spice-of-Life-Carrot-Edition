package com.cazsius.solcarrot.capability;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class DeathHandler {
	@SubscribeEvent
	public static void onClone(PlayerEvent.Clone event) {
		FoodCapability newInstance = FoodCapability.get(event.getEntityPlayer());
		FoodCapability original = FoodCapability.get(event.getOriginal());
		newInstance.deserializeNBT(original.serializeNBT());
	}
}

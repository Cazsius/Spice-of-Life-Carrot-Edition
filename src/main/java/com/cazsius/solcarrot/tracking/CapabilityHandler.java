package com.cazsius.solcarrot.tracking;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.api.SOLCarrotAPI;
import com.cazsius.solcarrot.communication.FoodListMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = SOLCarrot.MOD_ID)
public final class CapabilityHandler {
	private static final ResourceLocation FOOD = SOLCarrot.resourceLocation("food");
	
	@EventBusSubscriber(modid = SOLCarrot.MOD_ID)
	private static final class Setup {
		@SubscribeEvent
		public static void registerCapabilities(RegisterCapabilitiesEvent event) {
			event.registerEntity(SOLCarrotAPI.foodCapability, EntityType.PLAYER, (player, ctx) -> player.getData(SOLCarrot.FOOD_ATTACHMENT.get()));
		}
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		// server needs to send any loaded data to the client
		syncFoodList(event.getEntity());
	}
	
	@SubscribeEvent
	public static void onPlayerDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
		syncFoodList(event.getEntity());
	}
	
	@SubscribeEvent
	public static void onClone(PlayerEvent.Clone event) {
		if (event.isWasDeath() && SOLCarrotConfig.shouldResetOnDeath()) return;
		
		var originalPlayer = event.getOriginal();
		var original = FoodList.get(originalPlayer);
		event.getEntity().setData(SOLCarrot.FOOD_ATTACHMENT, original);
	}
	
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		syncFoodList(event.getEntity());
	}
	
	public static void syncFoodList(Player player) {
		if (player instanceof ServerPlayer target) {
			target.connection.send(
				new FoodListMessage(FoodList.get(player), player.registryAccess())
			);
		}

		MaxHealthHandler.updateFoodHPModifier(player);
	}
}

package com.cazsius.solcarrot.tracking;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.cazsius.solcarrot.lib.Localization.localizedComponent;
import static com.cazsius.solcarrot.lib.Localization.localizedQuantityComponent;

@Mod.EventBusSubscriber(modid = SOLCarrot.MOD_ID)
public final class FoodTracker {
	@SubscribeEvent
	public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
		if (!(event.getEntity() instanceof Player player)) return;
		
		if (player.level().isClientSide) return;
		var world = (ServerLevel) player.level();
		
		var serverPlayer = (ServerPlayer) player;
		boolean isInSurvival = serverPlayer.gameMode.getGameModeForPlayer() == GameType.SURVIVAL;
		if (SOLCarrotConfig.limitProgressionToSurvival() && !isInSurvival) return;
		
		var usedItem = event.getItem().getItem();
		if (!usedItem.isEdible()) return;
		
		FoodList foodList = FoodList.get(player);
		boolean hasTriedNewFood = foodList.addFood(usedItem);
		
		// check this before syncing, because the sync entails an hp update
		boolean newMilestoneReached = MaxHealthHandler.updateFoodHPModifier(player);
		
		CapabilityHandler.syncFoodList(player);
		var progressInfo = foodList.getProgressInfo();
		
		if (newMilestoneReached) {
			if (SOLCarrotConfig.shouldPlayMilestoneSounds()) {
				// passing the player makes it not play for some reason
				world.playSound(
					null,
					player.blockPosition(),
					SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS,
					1.0F, 1.0F
				);
			}
			
			if (SOLCarrotConfig.shouldSpawnMilestoneParticles()) {
				spawnParticles(world, player, ParticleTypes.HEART, 12);
				
				if (progressInfo.hasReachedMax()) {
					spawnParticles(world, player, ParticleTypes.HAPPY_VILLAGER, 16);
				}
			}
			
			var heartsDescription = localizedQuantityComponent("message", "hearts", SOLCarrotConfig.getHeartsPerMilestone());
			
			if (SOLCarrotConfig.shouldShowProgressAboveHotbar()) {
				String messageKey = progressInfo.hasReachedMax() ? "finished.hotbar" : "milestone_achieved";
				player.displayClientMessage(localizedComponent("message", messageKey, heartsDescription), true);
			} else {
				showChatMessage(player, ChatFormatting.DARK_AQUA, localizedComponent("message", "milestone_achieved", heartsDescription));
				if (progressInfo.hasReachedMax()) {
					showChatMessage(player, ChatFormatting.GOLD, localizedComponent("message", "finished.chat"));
				}
			}
		} else if (hasTriedNewFood) {
			if (SOLCarrotConfig.shouldSpawnIntermediateParticles()) {
				spawnParticles(world, player, ParticleTypes.END_ROD, 12);
			}
		}
	}
	
	private static void spawnParticles(ServerLevel world, Player player, ParticleOptions type, int count) {
		// this overload sends a packet to the client
		world.sendParticles(
			type,
			player.getX(), player.getY() + player.getEyeHeight(), player.getZ(),
			count,
			0.5F, 0.5F, 0.5F,
			0.0F
		);
	}
	
	private static void showChatMessage(Player player, ChatFormatting color, Component message) {
		var component = localizedComponent("message", "chat_wrapper", message)
			.withStyle(color);
		player.displayClientMessage(component, false);
	}
	
	private FoodTracker() {}
}

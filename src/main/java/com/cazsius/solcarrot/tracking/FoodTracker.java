package com.cazsius.solcarrot.tracking;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.*;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.cazsius.solcarrot.lib.Localization.localizedComponent;
import static com.cazsius.solcarrot.lib.Localization.localizedQuantityComponent;

@Mod.EventBusSubscriber(modid = SOLCarrot.MOD_ID)
public final class FoodTracker {
	@SubscribeEvent
	public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
		if (!(event.getEntity() instanceof PlayerEntity)) return;
		PlayerEntity player = (PlayerEntity) event.getEntity();
		
		if (player.level.isClientSide) return;
		ServerWorld world = (ServerWorld) player.level;
		
		ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
		boolean isInSurvival = serverPlayer.gameMode.getGameModeForPlayer() == GameType.SURVIVAL;
		if (SOLCarrotConfig.limitProgressionToSurvival() && !isInSurvival) return;
		
		Item usedItem = event.getItem().getItem();
		if (!usedItem.isEdible()) return;
		
		FoodList foodList = FoodList.get(player);
		boolean hasTriedNewFood = foodList.addFood(usedItem);
		
		// check this before syncing, because the sync entails an hp update
		boolean newMilestoneReached = MaxHealthHandler.updateFoodHPModifier(player);
		
		CapabilityHandler.syncFoodList(player);
		ProgressInfo progressInfo = foodList.getProgressInfo();
		
		if (newMilestoneReached) {
			if (SOLCarrotConfig.shouldPlayMilestoneSounds()) {
				// passing the player makes it not play for some reason
				world.playSound(
					null,
					player.blockPosition(),
					SoundEvents.PLAYER_LEVELUP, SoundCategory.PLAYERS,
					1.0F, 1.0F
				);
			}
			
			if (SOLCarrotConfig.shouldSpawnMilestoneParticles()) {
				spawnParticles(world, player, ParticleTypes.HEART, 12);
				
				if (progressInfo.hasReachedMax()) {
					spawnParticles(world, player, ParticleTypes.HAPPY_VILLAGER, 16);
				}
			}
			
			ITextComponent heartsDescription = localizedQuantityComponent("message", "hearts", SOLCarrotConfig.getHeartsPerMilestone());
			
			if (SOLCarrotConfig.shouldShowProgressAboveHotbar()) {
				String messageKey = progressInfo.hasReachedMax() ? "finished.hotbar" : "milestone_achieved";
				player.displayClientMessage(localizedComponent("message", messageKey, heartsDescription), true);
			} else {
				showChatMessage(player, TextFormatting.DARK_AQUA, localizedComponent("message", "milestone_achieved", heartsDescription));
				if (progressInfo.hasReachedMax()) {
					showChatMessage(player, TextFormatting.GOLD, localizedComponent("message", "finished.chat"));
				}
			}
		} else if (hasTriedNewFood) {
			if (SOLCarrotConfig.shouldSpawnIntermediateParticles()) {
				spawnParticles(world, player, ParticleTypes.END_ROD, 12);
			}
		}
	}
	
	private static void spawnParticles(ServerWorld world, PlayerEntity player, IParticleData type, int count) {
		// this overload sends a packet to the client
		world.sendParticles(
			type,
			player.getX(), player.getY() + player.getEyeHeight(), player.getZ(),
			count,
			0.5F, 0.5F, 0.5F,
			0.0F
		);
	}
	
	private static void showChatMessage(PlayerEntity player, TextFormatting color, ITextComponent message) {
		ITextComponent component = localizedComponent("message", "chat_wrapper", message)
			.withStyle(style -> style.applyFormat(color));
		player.displayClientMessage(component, false);
	}
	
	private FoodTracker() {}
}

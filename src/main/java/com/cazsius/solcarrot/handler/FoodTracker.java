package com.cazsius.solcarrot.handler;

import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.lib.ProgressInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.food.FoodEvent;

import java.util.Arrays;
import java.util.Optional;

import static com.cazsius.solcarrot.lib.Localization.localized;
import static com.cazsius.solcarrot.lib.Localization.localizedQuantity;

@Mod.EventBusSubscriber
public class FoodTracker {
	
	@SubscribeEvent
	public static void onFoodEaten(FoodEvent.FoodEaten event) {
		EntityPlayer player = event.player;
		
		FoodCapability foodCapability = FoodCapability.get(player);
		foodCapability.addFood(event.food.getItem(), event.food.getMetadata());
		
		float storedHP = player.getMaxHealth();
		
		MaxHealthHandler.updateFoodHPModifier(player);
		if (storedHP < player.getMaxHealth()) {
			player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
			player.world.spawnParticle(
					EnumParticleTypes.VILLAGER_HAPPY,
					player.posX, player.posY + 2, player.posZ,
					player.motionX, player.motionY, player.motionZ
			);
			
			if (player.world.isRemote) {
				ProgressInfo progressInfo = ProgressInfo.getProgressInfo(player);
				
				String heartsDescription = localizedQuantity("message", "milestone_achieved.hearts", SOLCarrotConfig.heartsPerMilestone);
				
				String milestoneAchievedMessage = localized("message", "milestone_achieved", heartsDescription);
				
				String nextMilestoneMessage;
				if (progressInfo.hasReachedMax()) {
					nextMilestoneMessage = localized("message", "desire.finished");
				} else {
					nextMilestoneMessage = localized("message", "desire.continues", progressInfo.foodsUntilNextMilestone(), heartsDescription);
				}
				
				showMessage(player, milestoneAchievedMessage, nextMilestoneMessage);
			}
		}
	}
	
	private static void showMessage(EntityPlayer player, String... message) {
		boolean showAboveHotbar = SOLCarrotConfig.shouldShowProgressAboveHotbar;
		String separator = showAboveHotbar ? " " : "\n"; // above-hotbar mode is single-line only :(
		Optional<String> combinedMessage = Arrays.stream(message)
				.reduce((acc, next) -> acc + separator + next);
		assert combinedMessage.isPresent(); // at least one message to send
		
		String prefix = showAboveHotbar ? "" : localized("message", "prefix") + " ";
		ITextComponent component = new TextComponentString(prefix + combinedMessage.get());
		component.setStyle(new Style().setColor(TextFormatting.DARK_AQUA));
		player.sendStatusMessage(component, showAboveHotbar);
	}
}

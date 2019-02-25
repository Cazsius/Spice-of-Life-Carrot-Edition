package com.cazsius.solcarrot.handler;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.command.Command;
import com.cazsius.solcarrot.lib.ProgressInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.food.FoodEvent;

import static com.cazsius.solcarrot.lib.Localization.localized;
import static com.cazsius.solcarrot.lib.Localization.localizedQuantity;

@Mod.EventBusSubscriber
public class HandlerFoodTracker {
	
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
				
				String heartsDescription = localizedQuantity("message", "milestone_achieved.hearts", HandlerConfiguration.getHeartsPerMilestone());
				
				String milestoneAchievedMessage = localized("message", "milestone_achieved", heartsDescription);
				
				String nextMilestoneMessage;
				if (progressInfo.hasReachedMax()) {
					nextMilestoneMessage = localized("message", "desire.finished");
				} else {
					nextMilestoneMessage = localized("message", "desire.continues", progressInfo.foodsUntilNextMilestone(), heartsDescription);
				}
				
				Command.showMessage(player, milestoneAchievedMessage + "\n" + nextMilestoneMessage);
			}
		}
	}
}

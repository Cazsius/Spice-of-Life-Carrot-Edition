package com.cazsius.solcarrot.handler;

import com.cazsius.solcarrot.capability.FoodCapability;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.food.FoodEvent;

public class HandlerFoodTracker {

	@SubscribeEvent
	public void onFoodEaten(FoodEvent.FoodEaten event) {
		FoodCapability food = event.player.getCapability(FoodCapability.FOOD_CAPABILITY, null);
		food.addFood(event.food.getItem());

		float storedHP = event.player.getMaxHealth();

		MaxHealthHandler.updateFoodHPModifier(event.player);
		if (storedHP < event.player.getMaxHealth()) {
			event.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
			event.player.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, event.player.posX, event.player.posY + 2,
					event.player.posZ, event.player.motionX, event.player.motionY, event.player.motionZ, new int[0]);
			if (event.player.world.isRemote) {
				TextComponentTranslation milestone = new TextComponentTranslation("You've reached a new milestone! "
						+ HandlerConfiguration.getHeartsPerMilestone() + " hearts have been added!");
				event.player.sendMessage(milestone);
			}

		}
	}

}
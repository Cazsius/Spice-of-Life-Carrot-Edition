package com.cazsius.solcarrot.handler;

import com.cazsius.solcarrot.capability.FoodCapability;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.food.FoodEvent;

@SuppressWarnings("deprecation")
public class HandlerFoodTracker {

	@SubscribeEvent
	public void onFoodEaten(FoodEvent.FoodEaten event) 
	{
		FoodCapability food = event.player.getCapability(FoodCapability.FOOD_CAPABILITY, null);
		food.addFood(event.food.getItem(), event.food.getMetadata());

		float storedHP = event.player.getMaxHealth();

		MaxHealthHandler.updateFoodHPModifier(event.player);
		if (storedHP < event.player.getMaxHealth()) {
			event.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
			event.player.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, event.player.posX, event.player.posY + 2,
					event.player.posZ, event.player.motionX, event.player.motionY, event.player.motionZ, new int[0]);
			if (event.player.world.isRemote) {
				int hpm = HandlerConfiguration.getHeartsPerMilestone();
				TextComponentTranslation milestoneMessage = new TextComponentTranslation("solcarrot.message.foodeaten",
						(hpm == 1 ? I18n.translateToLocal("solcarrot.message.foodeaten.singleheart")
								: hpm + " " + I18n.translateToLocal("solcarrot.message.foodeaten.multipleheart")));

				event.player.sendMessage(milestoneMessage);

				int foodsEaten = food.getCount();
				int milestone = 0;
				int[] milestoneArray = HandlerConfiguration.getMilestoneArray();
				while (milestone < milestoneArray.length && foodsEaten + 1 > milestoneArray[milestone]) {
					milestone++;
				}

				if (milestone == milestoneArray.length) {
					milestoneMessage = new TextComponentTranslation("solcarrot.message.desire.lost");
				} else {
					milestoneMessage = new TextComponentTranslation("solcarrot.message.desire",
							(milestoneArray[milestone] - milestoneArray[milestone - 1]),
							(hpm == 1 ? I18n.translateToLocal("solcarrot.message.foodeaten.singleheart")
									: hpm + " " + I18n.translateToLocal("solcarrot.message.foodeaten.multipleheart")));
				}
				event.player.sendMessage(milestoneMessage);
			}

		}
	}

}
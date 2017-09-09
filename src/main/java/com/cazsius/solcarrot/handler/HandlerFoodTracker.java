package com.cazsius.solcarrot.handler;

import com.cazsius.solcarrot.capability.FoodCapability;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.food.FoodEvent;

public class HandlerFoodTracker {

	@SubscribeEvent
	public void onFoodEaten(FoodEvent.FoodEaten event) {
		FoodCapability food = event.player.getCapability(FoodCapability.FOOD_CAPABILITY, null);
		food.addFood(event.food.getItem());
		MaxHealthHandler.updateFoodHPModifier(event.player);
	}

}
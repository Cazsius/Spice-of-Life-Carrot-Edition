package com.cazsius.solcarrot.handler;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.lib.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HandlerCapability {

	public static final ResourceLocation FOOD = new ResourceLocation(Constants.MOD_ID, "food");

	@SubscribeEvent
	public void attachPlayerCapability(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayer) {
			event.addCapability(FOOD, new FoodCapability());
		}
	}
}

package com.cazsius.solcarrot.handler;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.lib.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class HandlerCapability {

	public static final ResourceLocation FOOD = new ResourceLocation(Constants.MOD_ID, "food");

	@SubscribeEvent
	public void attachPlayerCapability(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayer) {
			event.addCapability(FOOD, new FoodCapability());
		}
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event)
	{
		// server needs to send any loaded data to the client
		syncFoodList(event.player);
	}
	
	public void syncFoodList(EntityPlayer player)
	{
		FoodCapability food = player.getCapability(FoodCapability.FOOD_CAPABILITY, null);
		PacketHandler.INSTANCE.sendTo(new MessageFoodList(food), (EntityPlayerMP) player);
	}
}

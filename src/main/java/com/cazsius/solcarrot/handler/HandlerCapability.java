package com.cazsius.solcarrot.handler;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.capability.FoodCapability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class HandlerCapability {

	private static final ResourceLocation FOOD = SOLCarrot.resourceLocation("food");

	@SubscribeEvent
	public static void attachPlayerCapability(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayer) {
			event.addCapability(FOOD, new FoodCapability());
		}
	}

	@SubscribeEvent
	public static void onPlayerLogin(EntityJoinWorldEvent event) {
		// server needs to send any loaded data to the client
		if (event.getEntity() instanceof EntityPlayer && !event.getWorld().isRemote)
			syncFoodList((EntityPlayer) event.getEntity());
	}

	public static void syncFoodList(EntityPlayer player) {
		FoodCapability food = FoodCapability.get(player);
		PacketHandler.INSTANCE.sendTo(new MessageFoodList(food), (EntityPlayerMP) player);
	}
}

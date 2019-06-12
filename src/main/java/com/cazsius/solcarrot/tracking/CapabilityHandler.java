package com.cazsius.solcarrot.tracking;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.communication.MessageFoodList;
import com.cazsius.solcarrot.communication.PacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = SOLCarrot.MOD_ID)
public class CapabilityHandler {
	private static final ResourceLocation FOOD = SOLCarrot.resourceLocation("food");
	
	@SubscribeEvent
	public static void attachPlayerCapability(AttachCapabilitiesEvent<Entity> event) {
		if (!(event.getObject() instanceof EntityPlayer)) return;
		
		event.addCapability(FOOD, new FoodCapability());
	}
	
	@SubscribeEvent
	public static void onPlayerLogin(EntityJoinWorldEvent event) {
		if (event.getWorld().isRemote) return;
		if (!(event.getEntity() instanceof EntityPlayer)) return;
		
		// server needs to send any loaded data to the client
		syncFoodList((EntityPlayer) event.getEntity());
	}
	
	@SubscribeEvent
	public static void onClone(PlayerEvent.Clone event) {
		FoodCapability newInstance = FoodCapability.get(event.getEntityPlayer());
		FoodCapability original = FoodCapability.get(event.getOriginal());
		newInstance.deserializeNBT(original.serializeNBT());
	}
	
	public static void syncFoodList(EntityPlayer player) {
		FoodCapability foodCapability = FoodCapability.get(player);
		PacketHandler.INSTANCE.sendTo(new MessageFoodList(foodCapability), (EntityPlayerMP) player);
		MaxHealthHandler.updateFoodHPModifier(player);
	}
}

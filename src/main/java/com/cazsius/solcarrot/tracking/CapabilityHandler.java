package com.cazsius.solcarrot.tracking;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.api.FoodCapability;
import com.cazsius.solcarrot.communication.MessageFoodList;
import com.cazsius.solcarrot.communication.PacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = SOLCarrot.MOD_ID)
public final class CapabilityHandler {
	private static final ResourceLocation FOOD = SOLCarrot.resourceLocation("food");
	
	@SubscribeEvent
	public static void preInit(SOLCarrot.PreInitializationEvent e) {
		CapabilityManager.INSTANCE.register(FoodCapability.class, new FoodList.Storage(), FoodList::new);
	}
	
	@SubscribeEvent
	public static void attachPlayerCapability(AttachCapabilitiesEvent<Entity> event) {
		if (!(event.getObject() instanceof EntityPlayer)) return;
		
		event.addCapability(FOOD, new FoodList());
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
		if (event.isWasDeath() && SOLCarrotConfig.shouldResetOnDeath) return;
		
		FoodList original = FoodList.get(event.getOriginal());
		FoodList newInstance = FoodList.get(event.getEntityPlayer());
		newInstance.deserializeNBT(original.serializeNBT());
		syncFoodList(event.getEntityPlayer());
	}
	
	public static void syncFoodList(EntityPlayer player) {
		FoodList foodList = FoodList.get(player);
		PacketHandler.channel.sendTo(new MessageFoodList(foodList), (EntityPlayerMP) player);
		MaxHealthHandler.updateFoodHPModifier(player);
	}
	
	private CapabilityHandler() {}
}

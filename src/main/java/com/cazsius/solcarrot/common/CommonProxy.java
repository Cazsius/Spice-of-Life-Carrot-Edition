package com.cazsius.solcarrot.common;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.capability.FoodStorage;
import com.cazsius.solcarrot.handler.HandlerCapability;
import com.cazsius.solcarrot.handler.HandlerFoodTracker;
import com.cazsius.solcarrot.handler.MaxHealthHandler;
import com.cazsius.solcarrot.handler.PacketHandler;

import com.cazsius.solcarrot.item.ItemFoodBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.registries.GameData;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		PacketHandler.registerMessages("solcarrot");
	}

	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new HandlerFoodTracker());
		MinecraftForge.EVENT_BUS.register(new HandlerCapability());
		MinecraftForge.EVENT_BUS.register(MaxHealthHandler.class);
		CapabilityManager.INSTANCE.register(FoodCapability.class, new FoodStorage(), FoodCapability.class);
		NetworkRegistry.INSTANCE.registerGuiHandler(SOLCarrot.instance, new GuiHandler());
	}

	public EntityPlayer getSidedPlayer(MessageContext messageContext) {
		return messageContext.getServerHandler().player;
	}
}
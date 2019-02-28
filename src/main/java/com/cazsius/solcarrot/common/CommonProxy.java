package com.cazsius.solcarrot.common;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.capability.FoodStorage;
import com.cazsius.solcarrot.handler.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent e) {
		PacketHandler.registerMessages(SOLCarrot.MOD_ID);
	}
	
	public void init(FMLInitializationEvent event) {
		CapabilityManager.INSTANCE.register(FoodCapability.class, new FoodStorage(), FoodCapability::new);
		NetworkRegistry.INSTANCE.registerGuiHandler(SOLCarrot.instance, new GuiHandler());
	}
	
	public EntityPlayer getSidedPlayer(MessageContext messageContext) {
		return messageContext.getServerHandler().player;
	}
}
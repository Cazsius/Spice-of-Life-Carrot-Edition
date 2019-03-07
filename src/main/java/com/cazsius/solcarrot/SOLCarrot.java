package com.cazsius.solcarrot;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.capability.FoodStorage;
import com.cazsius.solcarrot.command.CommandFoodList;
import com.cazsius.solcarrot.common.GuiHandler;
import com.cazsius.solcarrot.handler.PacketHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = SOLCarrot.MOD_ID, version = "__VERSION_FROM_GRADLE__", dependencies = "required-after:applecore")
public class SOLCarrot {
	
	public static final String MOD_ID = "solcarrot";
	
	@Mod.Instance(MOD_ID)
	public static SOLCarrot instance;
	
	public static ResourceLocation resourceLocation(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
	
	public static String namespaced(String path) {
		return MOD_ID + "." + path;
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		PacketHandler.registerMessages(MOD_ID);
		CapabilityManager.INSTANCE.register(FoodCapability.class, new FoodStorage(), FoodCapability::new);
		NetworkRegistry.INSTANCE.registerGuiHandler(SOLCarrot.instance, new GuiHandler());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		MinecraftForge.EVENT_BUS.post(new InitializationEvent());
	}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandFoodList());
	}
	
	public static class InitializationEvent extends Event {}
}

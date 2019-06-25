package com.cazsius.solcarrot;

import com.cazsius.solcarrot.command.CommandFoodList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
	modid = SOLCarrot.MOD_ID,
	certificateFingerprint = "__FINGERPRINT_FROM_GRADLE__",
	version = "__VERSION_FROM_GRADLE__",
	dependencies = "required-after:applecore"
)
public final class SOLCarrot {
	public static final String MOD_ID = "solcarrot";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	@Mod.Instance(MOD_ID)
	public static SOLCarrot instance;
	
	public static ResourceLocation resourceLocation(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
	
	public static String namespaced(String path) {
		return MOD_ID + "." + path;
	}
	
	@EventHandler
	public static void onFingerprintViolation(FMLFingerprintViolationEvent event) {
		// This complains if jar not signed, even if certificateFingerprint is blank
		LOGGER.warn("Invalid Fingerprint!");
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.post(new PreInitializationEvent());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		MinecraftForge.EVENT_BUS.post(new InitializationEvent());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		MinecraftForge.EVENT_BUS.post(new PostInitializationEvent());
	}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandFoodList());
	}
	
	public static final class PreInitializationEvent extends Event {}
	
	public static final class InitializationEvent extends Event {}
	
	public static final class PostInitializationEvent extends Event {}
}

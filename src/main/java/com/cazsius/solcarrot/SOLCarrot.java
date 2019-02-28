package com.cazsius.solcarrot;

import com.cazsius.solcarrot.command.CommandFoodList;
import com.cazsius.solcarrot.common.CommonProxy;
import com.cazsius.solcarrot.lib.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

@Mod(modid = SOLCarrot.MOD_ID, version = "__VERSION_FROM_GRADLE__", dependencies = "required-after:applecore")
public class SOLCarrot {
	
	public static final String MOD_ID = "solcarrot";
	
	@Mod.Instance(MOD_ID)
	public static SOLCarrot instance;
	
	@SidedProxy(clientSide = Constants.CLIENT_PROXY_CLASS, serverSide = Constants.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static ResourceLocation resourceLocation(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
	
	public static String namespaced(String path) {
		return MOD_ID + "." + path;
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent e) {
		proxy.init(e);
	}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandFoodList());
	}
}

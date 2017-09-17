package com.cazsius.solcarrot;

import com.cazsius.solcarrot.command.CommandClearFoodArray;
import com.cazsius.solcarrot.command.CommandSizeFoodArray;
import com.cazsius.solcarrot.common.CommonProxy;
import com.cazsius.solcarrot.handler.HandlerConfiguration;
import com.cazsius.solcarrot.lib.Constants;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION_NUMBER, dependencies = "required-after:applecore")
public class SOLCarrot {

	@Instance(Constants.MOD_ID)
	public static SOLCarrot instance;

	@SidedProxy(clientSide = Constants.CLIENT_PROXY_CLASS, serverSide = Constants.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		HandlerConfiguration.initConfig(e.getSuggestedConfigurationFile());
		proxy.preInit(e);
	}

	@EventHandler
	public void Init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandClearFoodArray());
		event.registerServerCommand(new CommandSizeFoodArray());
	}
}

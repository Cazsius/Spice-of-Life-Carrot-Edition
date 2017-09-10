package com.cazsius.solcarrot.common;

import com.cazsius.solcarrot.handler.PacketHandler;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		PacketHandler.registerMessages("solcarrot");
	}

}
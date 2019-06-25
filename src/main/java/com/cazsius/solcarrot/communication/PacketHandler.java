package com.cazsius.solcarrot.communication;

import com.cazsius.solcarrot.SOLCarrot;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = SOLCarrot.MOD_ID)
public final class PacketHandler {
	public static final SimpleNetworkWrapper channel = NetworkRegistry.INSTANCE.newSimpleChannel(SOLCarrot.MOD_ID);
	
	private static int packetID = 0;
	
	@SubscribeEvent
	public static void preInit(SOLCarrot.PreInitializationEvent e) {
		channel.registerMessage(MessageFoodList.Handler.class, MessageFoodList.class, nextID(), Side.CLIENT);
	}
	
	private static int nextID() {
		return packetID++;
	}
	
	private PacketHandler() {}
}

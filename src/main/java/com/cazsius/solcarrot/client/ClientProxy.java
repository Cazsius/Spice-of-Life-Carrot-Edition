package com.cazsius.solcarrot.client;

import com.cazsius.solcarrot.command.CommandSizeFoodArray;
import com.cazsius.solcarrot.common.CommonProxy;
import com.cazsius.solcarrot.handler.HandlerTooltip;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		MinecraftForge.EVENT_BUS.register(new HandlerTooltip());
        ClientCommandHandler.instance.registerCommand(new CommandSizeFoodArray());
	}

	@Override
	public EntityPlayer getSidedPlayer(MessageContext messageContext) {
		return Minecraft.getMinecraft().player;
	}
}
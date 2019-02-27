package com.cazsius.solcarrot.client;

import com.cazsius.solcarrot.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public EntityPlayer getSidedPlayer(MessageContext messageContext) {
		return Minecraft.getMinecraft().player;
	}
}
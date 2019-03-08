package com.cazsius.solcarrot.handler;

import com.cazsius.solcarrot.capability.FoodCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageFoodList extends NBTMessage {
	private NBTTagCompound capabilityNBT;
	
	public MessageFoodList(FoodCapability foodCapability) {
		this.capabilityNBT = foodCapability.serializeFullNBT();
	}
	
	/** this is used when receiving, followed by a call to deserializeNBT */
	public MessageFoodList() {}
	
	@Override
	public NBTBase serializeNBT() {
		return capabilityNBT;
	}
	
	@Override
	public void deserializeNBT(NBTBase tag) {
		capabilityNBT = (NBTTagCompound) tag;
	}
	
	// message is only ever sent from server to client, so everything inside can use client-only methods just fine.
	public static class Handler implements IMessageHandler<MessageFoodList, IMessage> {
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(MessageFoodList message, MessageContext ctx) {
			// Always use a construct like this to actually handle your message.
			// This ensures that your 'handle' code is run on the main Minecraft
			// thread.
			// 'onMessage' itself is called on the networking thread so it is
			// not safe to do a lot of things here.
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		private void handle(MessageFoodList message, MessageContext ctx) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			FoodCapability.get(player).deserializeFullNBT(message.capabilityNBT);
		}
	}
}

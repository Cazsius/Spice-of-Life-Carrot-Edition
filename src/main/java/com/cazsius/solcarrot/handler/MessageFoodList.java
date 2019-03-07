package com.cazsius.solcarrot.handler;

import com.cazsius.solcarrot.capability.FoodCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageFoodList extends NBTMessage {
	private FoodCapability foodCapability;
	
	public MessageFoodList(FoodCapability foodCapability) {
		this.foodCapability = foodCapability;
	}
	
	/** forge needs this */
	public MessageFoodList() {
		this.foodCapability = new FoodCapability();
	}
	
	@Override
	public NBTBase serializeNBT() {
		return foodCapability.serializeNBT();
	}
	
	@Override
	public void deserializeNBT(NBTBase tag) {
		foodCapability.deserializeNBT(tag);
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
			FoodCapability.get(player).copyFoods(message.foodCapability);
		}
	}
}

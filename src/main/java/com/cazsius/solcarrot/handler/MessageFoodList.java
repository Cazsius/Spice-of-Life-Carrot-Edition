package com.cazsius.solcarrot.handler;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.capability.FoodInstance;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageFoodList implements IMessage {
	private FoodCapability foodCapability;
	
	public MessageFoodList(FoodCapability foodCapability) {
		this.foodCapability = foodCapability;
	}
	
	/** forge needs this */
	public MessageFoodList() {
		this.foodCapability = new FoodCapability();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer pb = new PacketBuffer(buf);
		while (pb.isReadable()) {
			//                                      item ID        metadata
			foodCapability.addFood(Item.getItemById(pb.readInt()), pb.readInt());
		}
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer pb = new PacketBuffer(buf);
		for (FoodInstance food : foodCapability.getHistory()) {
			pb.writeInt(Item.getIdFromItem(food.item));
			pb.writeInt(food.metadata);
		}
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

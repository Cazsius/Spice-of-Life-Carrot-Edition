package com.cazsius.solcarrot.communication;

import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public final class FoodListMessage {
	private CompoundTag capabilityNBT;
	
	public FoodListMessage(FoodList foodList) {
		this.capabilityNBT = foodList.serializeNBT();
	}
	
	public FoodListMessage(FriendlyByteBuf buffer) {
		this.capabilityNBT = buffer.readNbt();
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeNbt(capabilityNBT);
	}
	
	public void handle(Supplier<NetworkEvent.Context> context) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Handler.handle(this, context));
	}
	
	private static class Handler {
		static void handle(FoodListMessage message, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> {
				Player player = Minecraft.getInstance().player;
				assert player != null;
				FoodList.get(player).deserializeNBT(message.capabilityNBT);
			});
			context.get().setPacketHandled(true);
		}
	}
}

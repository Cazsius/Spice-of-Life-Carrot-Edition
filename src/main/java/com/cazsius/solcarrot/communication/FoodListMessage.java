package com.cazsius.solcarrot.communication;

import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public final class FoodListMessage {
	private CompoundNBT capabilityNBT;
	
	public FoodListMessage(FoodList foodList) {
		this.capabilityNBT = foodList.serializeNBT();
	}
	
	public FoodListMessage(PacketBuffer buffer) {
		this.capabilityNBT = buffer.readCompoundTag();
	}
	
	public void write(PacketBuffer buffer) {
		buffer.writeCompoundTag(capabilityNBT);
	}
	
	public void handle(Supplier<NetworkEvent.Context> context) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Handler.handle(this, context));
	}
	
	private static class Handler {
		static void handle(FoodListMessage message, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> {
				PlayerEntity player = Minecraft.getInstance().player;
				assert player != null;
				FoodList.get(player).deserializeNBT(message.capabilityNBT);
			});
			context.get().setPacketHandled(true);
		}
	}
}

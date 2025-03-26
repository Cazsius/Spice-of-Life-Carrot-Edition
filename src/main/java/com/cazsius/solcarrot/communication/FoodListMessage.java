package com.cazsius.solcarrot.communication;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record FoodListMessage(CompoundTag capabilityNBT) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, FoodListMessage> CODEC = StreamCodec.composite(
			ByteBufCodecs.COMPOUND_TAG,
			FoodListMessage::capabilityNBT,
			FoodListMessage::new);
	public static final Type<FoodListMessage> ID = new Type<>(SOLCarrot.resourceLocation("food_list_message"));


	public FoodListMessage(FoodList foodList, HolderLookup.Provider provider) {
		this(foodList.serializeNBT(provider));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}

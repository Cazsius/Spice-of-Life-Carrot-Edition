package com.cazsius.solcarrot.communication;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;

import java.util.List;

public record FoodListMessage(List<Holder<Item>> foodList) implements CustomPacketPayload {
	public static final StreamCodec<RegistryFriendlyByteBuf, FoodListMessage> CODEC = StreamCodec.composite(
			Item.STREAM_CODEC.apply(ByteBufCodecs.list()),
			FoodListMessage::foodList,
			FoodListMessage::new);
	public static final Type<FoodListMessage> ID = new Type<>(SOLCarrot.resourceLocation("food_list_message"));


	public FoodListMessage(FoodList foodList, HolderLookup.Provider provider) {
		this(foodList.getEatenFoods());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}

package com.cazsius.solcarrot.communication.handler;

import com.cazsius.solcarrot.client.FoodItems;
import com.cazsius.solcarrot.communication.ConstructFoodsMessage;
import com.cazsius.solcarrot.communication.FoodListMessage;
import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
	private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

	public static ClientPayloadHandler getInstance() {
		return INSTANCE;
	}

	public void handleFoodList(final FoodListMessage message, final IPayloadContext context) {
		context.enqueueWork(() -> {
					Player player = context.player();
					if (player != null) {
						FoodList foodList = FoodList.get(player);
						foodList.readFoods(message.foodList());
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("solcarrot.networking.food_list_message.failed", e.getMessage()));
					return null;
				});
	}

	public void handleConstructFoods(final ConstructFoodsMessage message, final IPayloadContext context) {
		context.enqueueWork(FoodItems::setUp)
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("solcarrot.networking.construct_foods_message.failed", e.getMessage()));
					return null;
				});
	}
}

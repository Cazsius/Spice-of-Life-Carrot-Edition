package com.cazsius.solcarrot;

import com.cazsius.solcarrot.communication.ConstructFoodsMessage;
import com.cazsius.solcarrot.communication.FoodListMessage;
import com.cazsius.solcarrot.communication.handler.ClientPayloadHandler;
import com.cazsius.solcarrot.item.SOLCarrotItems;
import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

@Mod(SOLCarrot.MOD_ID)
@EventBusSubscriber(modid = SOLCarrot.MOD_ID)
public final class SOLCarrot {
	public static final String MOD_ID = "solcarrot";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MOD_ID);
	public static final Supplier<AttachmentType<FoodList>> FOOD_ATTACHMENT = ATTACHMENT_TYPES.register("food", () ->
			AttachmentType.builder(() -> new FoodList()).serialize(FoodList.CODEC).build());


	public static ResourceLocation resourceLocation(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	@SubscribeEvent
	public static void setUp(RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(MOD_ID);

		registrar.playToClient(FoodListMessage.ID, FoodListMessage.CODEC, ClientPayloadHandler.getInstance()::handleFoodList);
		registrar.playToClient(ConstructFoodsMessage.ID, ConstructFoodsMessage.CODEC, ClientPayloadHandler.getInstance()::handleConstructFoods);
	}
	
	public SOLCarrot(IEventBus eventBus, ModContainer container, Dist dist) {
		ATTACHMENT_TYPES.register(eventBus);
		SOLCarrotConfig.setUp(container, dist);
		SOLCarrotItems.setUp(eventBus);
	}
}

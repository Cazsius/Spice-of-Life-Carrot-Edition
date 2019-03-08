package com.cazsius.solcarrot.handler;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.capability.ProgressInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.*;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.food.FoodEvent;

import java.util.Arrays;
import java.util.Optional;

import static com.cazsius.solcarrot.lib.Localization.localizedComponent;
import static com.cazsius.solcarrot.lib.Localization.localizedQuantityComponent;

@Mod.EventBusSubscriber(modid = SOLCarrot.MOD_ID)
public class FoodTracker {
	
	@SubscribeEvent
	public static void onFoodEaten(FoodEvent.FoodEaten event) {
		if (event.player.world.isRemote) return;
		WorldServer world = (WorldServer) event.player.world;
		
		EntityPlayer player = event.player;
		
		FoodCapability foodCapability = FoodCapability.get(player);
		foodCapability.addFood(event.food);
		CapabilityHandler.syncFoodList(player);
		
		boolean newMilestoneReached = MaxHealthHandler.updateFoodHPModifier(player);
		if (newMilestoneReached) {
			// passing the player makes it not play for some reason
			world.playSound(null,
					player.posX, player.posY, player.posZ,
					SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS,
					1.0F, 1.0F
			);
			
			// this overload sends a packet to the client
			world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,
					player.posX, player.posY + player.getEyeHeight(), player.posZ,
					16,
					0.5F, 0.5F, 0.5F,
					0.0F
			);
			
			ProgressInfo progressInfo = foodCapability.getProgressInfo();
			
			ITextComponent heartsDescription = localizedQuantityComponent("message", "milestone_achieved.hearts", SOLCarrotConfig.heartsPerMilestone);
			ITextComponent milestoneAchievedMessage = localizedComponent("message", "milestone_achieved", heartsDescription);
			ITextComponent nextMilestoneMessage;
			if (progressInfo.hasReachedMax()) {
				nextMilestoneMessage = localizedComponent("message", "desire.finished");
			} else {
				nextMilestoneMessage = localizedComponent("message", "desire.continues", progressInfo.foodsUntilNextMilestone(), heartsDescription);
			}
			
			showMessage(player, milestoneAchievedMessage, nextMilestoneMessage);
		}
	}
	
	private static void showMessage(EntityPlayer player, ITextComponent... message) {
		boolean showAboveHotbar = SOLCarrotConfig.shouldShowProgressAboveHotbar;
		String separator = showAboveHotbar ? " " : "\n"; // above-hotbar mode is single-line only :(
		Optional<ITextComponent> combinedMessage = Arrays.stream(message)
				.reduce((acc, next) -> acc.appendText(separator).appendSibling(next));
		assert combinedMessage.isPresent(); // at least one message to send
		
		ITextComponent prefix = showAboveHotbar
				? new TextComponentString("")
				: localizedComponent("message", "prefix").appendText(" ");
		ITextComponent component = prefix.appendSibling(combinedMessage.get());
		component.setStyle(new Style().setColor(TextFormatting.DARK_AQUA));
		player.sendStatusMessage(component, showAboveHotbar);
	}
}

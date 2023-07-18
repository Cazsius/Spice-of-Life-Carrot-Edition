package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.lib.Localization;
import com.cazsius.solcarrot.tracking.CapabilityHandler;
import com.cazsius.solcarrot.tracking.FoodList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

@Mod.EventBusSubscriber(modid = SOLCarrot.MOD_ID)
public final class FoodListCommand {
	private static final String name = "foodlist";
	
	@SubscribeEvent
	public static void register(RegisterCommandsEvent event) {
		event.getDispatcher().register(
			literal(name)
				.then(withPlayerArgumentOrSender(literal("size"), FoodListCommand::showFoodListSize))
				.then(withPlayerArgumentOrSender(literal("sync"), FoodListCommand::syncFoodList))
				.then(withPlayerArgumentOrSender(literal("clear"), FoodListCommand::clearFoodList))
		);
	}
	
	@FunctionalInterface
	private interface CommandWithPlayer {
		int run(CommandContext<CommandSourceStack> context, Player target) throws CommandSyntaxException;
	}
	
	static ArgumentBuilder<CommandSourceStack, ?> withPlayerArgumentOrSender(ArgumentBuilder<CommandSourceStack, ?> base, CommandWithPlayer command) {
		String target = "target";
		return base
			.executes((context) -> command.run(context, context.getSource().getPlayerOrException()))
			.then(argument(target, EntityArgument.player())
				.executes((context) -> command.run(context, EntityArgument.getPlayer(context, target)))
			);
	}
	
	static int showFoodListSize(CommandContext<CommandSourceStack> context, Player target) {
		var progressInfo = FoodList.get(target).getProgressInfo();
		
		var progressDesc = localizedQuantityComponent("size.desc.foods_eaten", progressInfo.foodsEaten);
		sendFeedback(context.getSource(), progressDesc);
		
		var milestoneDesc = progressInfo.hasReachedMax()
			? localizedComponent("size.desc.milestone.max")
			: localizedComponent("size.desc.milestone.more", progressInfo.foodsUntilNextMilestone());
		sendFeedback(context.getSource(), milestoneDesc);
		
		return Command.SINGLE_SUCCESS;
	}
	
	static int syncFoodList(CommandContext<CommandSourceStack> context, Player target) {
		CapabilityHandler.syncFoodList(target);
		
		sendFeedback(context.getSource(), localizedComponent("sync.success"));
		return Command.SINGLE_SUCCESS;
	}
	
	static int clearFoodList(CommandContext<CommandSourceStack> context, Player target) {
		boolean isOp = context.getSource().hasPermission(2);
		boolean isTargetingSelf = isTargetingSelf(context, target);
		if (!isOp && !isTargetingSelf)
			throw new CommandRuntimeException(localizedComponent("no_permissions"));
		
		FoodList.get(target).clearFood();
		CapabilityHandler.syncFoodList(target);
		
		var feedback = localizedComponent("clear.success");
		sendFeedback(context.getSource(), feedback);
		if (!isTargetingSelf) {
			target.displayClientMessage(applyFeedbackStyle(feedback), true);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	static void sendFeedback(CommandSourceStack source, MutableComponent message) {
		source.sendSuccess(() -> applyFeedbackStyle(message), true);
	}
	
	private static MutableComponent applyFeedbackStyle(MutableComponent text) {
		return text.withStyle(ChatFormatting.DARK_AQUA);
	}
	
	static boolean isTargetingSelf(CommandContext<CommandSourceStack> context, Player target) {
		return target.is(Objects.requireNonNull(context.getSource().getEntity()));
	}
	
	static MutableComponent localizedComponent(String path, Object... args) {
		return Localization.localizedComponent("command", localizationPath(path), args);
	}
	
	static MutableComponent localizedQuantityComponent(String path, int number) {
		return Localization.localizedQuantityComponent("command", localizationPath(path), number);
	}
	
	static String localizationPath(String path) {
		return FoodListCommand.name + "." + path;
	}
}

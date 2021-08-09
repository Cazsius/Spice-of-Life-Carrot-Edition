package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.lib.Localization;
import com.cazsius.solcarrot.tracking.*;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.*;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

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
		int run(CommandContext<CommandSource> context, PlayerEntity target) throws CommandSyntaxException;
	}
	
	static ArgumentBuilder<CommandSource, ?> withPlayerArgumentOrSender(ArgumentBuilder<CommandSource, ?> base, CommandWithPlayer command) {
		String target = "target";
		return base
			.executes((context) -> command.run(context, context.getSource().getPlayerOrException()))
			.then(argument(target, EntityArgument.player())
				.executes((context) -> command.run(context, EntityArgument.getPlayer(context, target)))
			);
	}
	
	static int showFoodListSize(CommandContext<CommandSource> context, PlayerEntity target) {
		ProgressInfo progressInfo = FoodList.get(target).getProgressInfo();
		
		IFormattableTextComponent progressDesc = localizedQuantityComponent("size.desc.foods_eaten", progressInfo.foodsEaten);
		sendFeedback(context.getSource(), progressDesc);
		
		IFormattableTextComponent milestoneDesc = progressInfo.hasReachedMax()
			? localizedComponent("size.desc.milestone.max")
			: localizedComponent("size.desc.milestone.more", progressInfo.foodsUntilNextMilestone());
		sendFeedback(context.getSource(), milestoneDesc);
		
		return Command.SINGLE_SUCCESS;
	}
	
	static int syncFoodList(CommandContext<CommandSource> context, PlayerEntity target) {
		CapabilityHandler.syncFoodList(target);
		
		sendFeedback(context.getSource(), localizedComponent("sync.success"));
		return Command.SINGLE_SUCCESS;
	}
	
	static int clearFoodList(CommandContext<CommandSource> context, PlayerEntity target) {
		boolean isOp = context.getSource().hasPermission(2);
		boolean isTargetingSelf = isTargetingSelf(context, target);
		if (!isOp && !isTargetingSelf)
			throw new CommandException(localizedComponent("no_permissions"));
		
		FoodList.get(target).clearFood();
		CapabilityHandler.syncFoodList(target);
		
		IFormattableTextComponent feedback = localizedComponent("clear.success");
		sendFeedback(context.getSource(), feedback);
		if (!isTargetingSelf) {
			target.displayClientMessage(applyFeedbackStyle(feedback), true);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	static void sendFeedback(CommandSource source, IFormattableTextComponent message) {
		source.sendSuccess(applyFeedbackStyle(message), true);
	}
	
	private static IFormattableTextComponent applyFeedbackStyle(IFormattableTextComponent text) {
		return text.withStyle(style -> style.applyFormat(TextFormatting.DARK_AQUA));
	}
	
	static boolean isTargetingSelf(CommandContext<CommandSource> context, PlayerEntity target) {
		return target.is(Objects.requireNonNull(context.getSource().getEntity()));
	}
	
	static IFormattableTextComponent localizedComponent(String path, Object... args) {
		return Localization.localizedComponent("command", localizationPath(path), args);
	}
	
	static IFormattableTextComponent localizedQuantityComponent(String path, int number) {
		return Localization.localizedQuantityComponent("command", localizationPath(path), number);
	}
	
	static String localizationPath(String path) {
		return FoodListCommand.name + "." + path;
	}
}

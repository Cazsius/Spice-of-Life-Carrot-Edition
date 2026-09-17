package com.cazsius.solcarrot.datagen;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.item.SOLCarrotItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;

@EventBusSubscriber
public class SOLDatagen {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		event.createReloadableRegistryObjects(
				new RegistrySetBuilder()
						.add(RecipeProvider.asBootstrap(SOLRecipeProvider::new)),
				Set.of(SOLCarrot.MOD_ID));
	}

	public static class SOLRecipeProvider extends RecipeProvider {

		public SOLRecipeProvider(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
			super(recipeOutput, advancementOutput);
		}


		@Override
		protected void buildRecipes() {
			shapeless(RecipeCategory.MISC, SOLCarrotItems.FOOD_BOOK.get())
					.requires(Items.BOOK)
					.requires(Items.CARROT)
					.unlockedBy("has_book", has(Items.BOOK))
					.unlockedBy("has_carrot", has(Items.CARROT))
					.save(output);
		}
	}
}

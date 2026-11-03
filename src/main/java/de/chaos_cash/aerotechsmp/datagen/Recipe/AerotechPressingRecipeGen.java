package de.chaos_cash.aerotechsmp.datagen.Recipe;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;


@SuppressWarnings("unused")
public final class AerotechPressingRecipeGen extends PressingRecipeGen {

	public AerotechPressingRecipeGen(PackOutput output, CompletableFuture<Provider> registries) {
	      super(output, registries, AerotechSMP.MOD_ID);
        createRecipes();
    }


    void createRecipes() {
        for (var rarity : Rarity.values()) {
              create(
                  () -> ModItems.COMPONENT_BOARDS.get(rarity),
                  builder ->
                      builder.output(0.5f, ModItems.COMPONENT_BASES.get(rarity));
              );
        }
    }
}

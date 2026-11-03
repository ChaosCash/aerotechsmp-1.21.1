package de.chaos_cash.aerotechsmp.datagen.Recipe;

import java.util.concurrent.CompletableFuture;

import com.simibubi.create.api.data.recipe.PressingRecipeGen;
import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.item.ModItems;
import de.chaos_cash.aerotechsmp.util.Rarity;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;


public final class AerotechPressingRecipeGen extends PressingRecipeGen {

	public AerotechPressingRecipeGen(PackOutput output, CompletableFuture<Provider> registries) {
	      super(output, registries, AerotechSMP.MOD_ID);
        createRecipes();
    }


    void createRecipes() {
        for (var rarity : Rarity.values()) {
              create(
                  () -> ModItems.CIRCUIT_BASES.get(rarity),
                  builder ->
                      builder.output(0.5f, ModItems.CIRCUIT_BOARDS.get(rarity))
              );
        }
    }
}

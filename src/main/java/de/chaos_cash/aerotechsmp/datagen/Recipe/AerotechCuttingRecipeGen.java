package de.chaos_cash.aerotechsmp.datagen.Recipe;

import com.simibubi.create.api.data.recipe.CuttingRecipeGen;
import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.item.ModItems;
import de.chaos_cash.aerotechsmp.util.Rarity;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public final class AerotechCuttingRecipeGen extends CuttingRecipeGen {
    public AerotechCuttingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, AerotechSMP.MOD_ID);
        createRecipes();
    }


    void createRecipes() {
        for (var rarity : Rarity.values()) {
            if (rarity != Rarity.Common) {
                create(
                    () -> ModItems.CIRCUIT_BOARDS.get(rarity),
                    builder ->
                        builder.duration(60)
                            .output(ModItems.CIRCUIT_BOARDS.get(rarity.tryDecrease()).toStack(2))
                            .output(0.5f, ModItems.CIRCUIT_BOARDS.get(rarity.tryDecrease()))
                );
            }
        }
    }
}

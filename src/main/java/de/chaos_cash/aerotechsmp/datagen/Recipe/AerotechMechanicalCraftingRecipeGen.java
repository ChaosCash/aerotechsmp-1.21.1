package de.chaos_cash.aerotechsmp.datagen.Recipe;

import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public final class AerotechMechanicalCraftingRecipeGen extends MechanicalCraftingRecipeGen {

    public AerotechMechanicalCraftingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Create.ID);
        createRecipes();
    }


    void createRecipes() {
    }
}

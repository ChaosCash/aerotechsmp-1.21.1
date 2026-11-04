package de.chaos_cash.aerotechsmp.datagen.Recipe;

import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeGen;
import de.chaos_cash.aerotechsmp.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import rearth.oritech.init.ItemContent;

import java.util.concurrent.CompletableFuture;

public final class AerotechMechanicalCraftingRecipeGen extends MechanicalCraftingRecipeGen {

    public AerotechMechanicalCraftingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Create.ID);
        createRecipes();
    }


    void createRecipes() {
        create(ModItems.CLAIM_CAPSULE::get)
                .recipe(b -> b
                        .key('S', ModItems.CLAIM_SHARD)
                        .key('C', ItemContent.DUBIOS_CONTAINER)
                        .patternLine(" S ")
                        .patternLine("SCS")
                        .patternLine(" S ")
                        .disallowMirrored());


        create(ModItems.FORCE_LOAD_CAPSULE::get)
                .recipe(b -> b
                        .key('S', ModItems.FORCE_LOAD_SHARD)
                        .key('C', ItemContent.DUBIOS_CONTAINER)
                        .patternLine(" S ")
                        .patternLine("SCS")
                        .patternLine(" S ")
                        .disallowMirrored());
    }
}

package de.chaos_cash.aerotechsmp.datagen.Recipe;

import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeGen;
import de.chaos_cash.aerotechsmp.item.ModItems;
import de.chaos_cash.aerotechsmp.util.Rarity;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import rearth.oritech.init.ItemContent;

import java.util.concurrent.CompletableFuture;

public final class AerotechMechanicalCraftingRecipeGen extends MechanicalCraftingRecipeGen {

    public AerotechMechanicalCraftingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Create.ID);
        createRecipes();
    }


    void createRecipes() {
        for (var rarity : Rarity.values()) {
            var spatialComponent = ModItems.SPATIAL_COMPONENTS.get(rarity);
            var kineticComponent = ModItems.KINETIC_COMPONENTS.get(rarity);
            Item sturdySheet = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "sturdy_sheet"));
            TagKey<Item> copperPlateTag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "plates/copper"));
            Item precisionMechanism = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "precision_mechanism"));


            create(kineticComponent::get) // kinetic component
                    .recipe(b ->
                            b.key('C', ModItems.CIRCUIT_BOARDS.get(rarity))
                                    .key('M', precisionMechanism)
                                    .key('P', copperPlateTag)
                                    .patternLine("  P  ")
                                    .patternLine(" PMP ")
                                    .patternLine("PMCMP")
                                    .patternLine(" PMP ")
                                    .patternLine("  P  ")
                                    .disallowMirrored());


            create(spatialComponent::get) // spatial component
                    .recipe(b ->
                            b.key('C', ModItems.CIRCUIT_BOARDS.get(rarity))
                            .key('P', ItemContent.ENDERIC_COMPOUND)
                            .key('S', sturdySheet)
                            .patternLine("  S  ")
                            .patternLine(" SPS ")
                            .patternLine("SPCPS")
                            .patternLine(" SPS ")
                            .patternLine("  S  ")
                            .disallowMirrored());
        }
    }
}

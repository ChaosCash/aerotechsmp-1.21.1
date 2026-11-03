package de.chaos_cash.aerotechsmp.datagen.Recipe;

import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.concurrent.CompletableFuture;

public final class AerotechCompactingRecipeGen extends CompactingRecipeGen {

    public AerotechCompactingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Create.ID);
        createRecipes();
    }


    void createRecipes() {
        TagKey<Fluid> moltenSteelTag = TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath("c", "molten_steel"));
        TagKey<Item> copperPlateTag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "plates/copper"));
        Item sturdySheet = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "sturdy_sheet"));

        for (var color : ArmorBlocks.Color.values()) {
            var armorBlock = ArmorBlocks.getBlock(ArmorBlocks.Tier.Light, color);
            create(
                    ArmorBlocks.getName(ArmorBlocks.Tier.Light, color, ArmorBlocks.BlockType.Block),
                    builder -> builder
                            .require(moltenSteelTag, 500)
                            .require(sturdySheet)
                            .require(sturdySheet)
                            .require(copperPlateTag)
                            .require(copperPlateTag)
                            .output(armorBlock)
            );
        }
    }
}

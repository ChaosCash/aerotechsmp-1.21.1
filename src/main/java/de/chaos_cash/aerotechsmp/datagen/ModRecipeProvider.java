package de.chaos_cash.aerotechsmp.datagen;


import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import de.chaos_cash.aerotechsmp.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        int i = 0;
        for (ArmorBlocks.Color color : ArmorBlocks.Color.values()) {
            SingleItemRecipeBuilder.stonecutting(
                    Ingredient.of(ModTags.Items.ARMOR_BLOCK_BASIC),
                    RecipeCategory.BUILDING_BLOCKS,
                    ModBlocks.ARMOR_BLOCKS.get(ArmorBlocks.Tier.Basic).get(color),
                    1
            ).unlockedBy("has_basic_armor_block", has(ModTags.Items.ARMOR_BLOCK_BASIC))
            .save(recipeOutput);

            SingleItemRecipeBuilder.stonecutting(
                    Ingredient.of(ModTags.Items.ARMOR_BLOCK_HARDENED),
                    RecipeCategory.BUILDING_BLOCKS,
                    ModBlocks.ARMOR_BLOCKS.get(ArmorBlocks.Tier.Hardened).get(color),
                    1
            ).unlockedBy("has_hardened_armor_block", has(ModTags.Items.ARMOR_BLOCK_HARDENED))
            .save(recipeOutput);

            SingleItemRecipeBuilder.stonecutting(
                    Ingredient.of(ModTags.Items.ARMOR_BLOCK_COMPRESSED),
                    RecipeCategory.BUILDING_BLOCKS,
                    ModBlocks.ARMOR_BLOCKS.get(ArmorBlocks.Tier.Compressed).get(color),
                    1
            ).unlockedBy("has_compressed_armor_block", has(ModTags.Items.ARMOR_BLOCK_COMPRESSED))
            .save(recipeOutput);

            i++;
        }
    }
}

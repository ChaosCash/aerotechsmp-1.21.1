package de.chaos_cash.aerotechsmp.datagen;


import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import de.chaos_cash.aerotechsmp.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        addArmorRecipes(recipeOutput);
    }


    private void addArmorRecipes(@NotNull RecipeOutput recipeOutput) {
        for (ArmorBlocks.Color color : ArmorBlocks.Color.values()) {
            Block basicArmorBlock = ArmorBlocks.getBlock(ArmorBlocks.Tier.Basic, color).get();
            Block hardenedArmorBlock = ArmorBlocks.getBlock(ArmorBlocks.Tier.Hardened, color).get();
            Block compressedArmorBlock = ArmorBlocks.getBlock(ArmorBlocks.Tier.Compressed, color).get();

            StairBlock basicArmorStair = ArmorBlocks.getStair(ArmorBlocks.Tier.Basic, color).get();
            StairBlock hardenedArmorStair = ArmorBlocks.getStair(ArmorBlocks.Tier.Hardened, color).get();
            StairBlock compressedArmorStair = ArmorBlocks.getStair(ArmorBlocks.Tier.Compressed, color).get();

            SlabBlock basicArmorSlab = ArmorBlocks.getSlab(ArmorBlocks.Tier.Basic, color).get();
            SlabBlock hardenedArmorSlab = ArmorBlocks.getSlab(ArmorBlocks.Tier.Hardened, color).get();
            SlabBlock compressedArmorSlab = ArmorBlocks.getSlab(ArmorBlocks.Tier.Compressed, color).get();


            // Block to Block Recipes
            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.ARMOR_BLOCK_BASIC),
                RecipeCategory.BUILDING_BLOCKS,
                basicArmorBlock,
                1
            )
            .unlockedBy("has_basic_armor_block", has(ModTags.Items.ARMOR_BLOCK_BASIC))
            .save(recipeOutput);

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.ARMOR_BLOCK_HARDENED),
                RecipeCategory.BUILDING_BLOCKS,
                hardenedArmorBlock,
                1
            )
            .unlockedBy("has_hardened_armor_block", has(ModTags.Items.ARMOR_BLOCK_HARDENED))
            .save(recipeOutput);

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.ARMOR_BLOCK_COMPRESSED),
                RecipeCategory.BUILDING_BLOCKS,
                compressedArmorBlock,
                1
            )
            .unlockedBy("has_compressed_armor_block", has(ModTags.Items.ARMOR_BLOCK_COMPRESSED))
            .save(recipeOutput);



            // Block to Stair Recipes
            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.ARMOR_BLOCK_BASIC),
                RecipeCategory.BUILDING_BLOCKS,
                basicArmorStair,
                1
            )
            .unlockedBy("has_basic_armor_block", has(ModTags.Items.ARMOR_BLOCK_BASIC))
            .save(recipeOutput);

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.ARMOR_BLOCK_HARDENED),
                RecipeCategory.BUILDING_BLOCKS,
                hardenedArmorStair,
                1
            )
            .unlockedBy("has_hardened_armor_block", has(ModTags.Items.ARMOR_BLOCK_HARDENED))
            .save(recipeOutput);

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.ARMOR_BLOCK_COMPRESSED),
                RecipeCategory.BUILDING_BLOCKS,
                compressedArmorStair,
                1
            )
            .unlockedBy("has_compressed_armor_block", has(ModTags.Items.ARMOR_BLOCK_COMPRESSED))
            .save(recipeOutput);



            // Block to Slab Recipes
            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.ARMOR_BLOCK_BASIC),
                RecipeCategory.BUILDING_BLOCKS,
                basicArmorSlab,
                2
            )
            .unlockedBy("has_basic_armor_block", has(ModTags.Items.ARMOR_BLOCK_BASIC))
            .save(recipeOutput);

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.ARMOR_BLOCK_HARDENED),
                RecipeCategory.BUILDING_BLOCKS,
                hardenedArmorSlab,
                2
            )
            .unlockedBy("has_hardened_armor_block", has(ModTags.Items.ARMOR_BLOCK_HARDENED))
            .save(recipeOutput);

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.ARMOR_BLOCK_COMPRESSED),
                RecipeCategory.BUILDING_BLOCKS,
                compressedArmorSlab,
                2
            )
            .unlockedBy("has_compressed_armor_block", has(ModTags.Items.ARMOR_BLOCK_COMPRESSED))
            .save(recipeOutput);



            // Stair to Stair Recipes
            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.ARMOR_STAIR_BASIC),
                RecipeCategory.BUILDING_BLOCKS,
                basicArmorStair,
                1
            )
            .unlockedBy("has_basic_armor_stair", has(ModTags.Items.ARMOR_STAIR_BASIC))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID,ArmorBlocks.getName(ArmorBlocks.Tier.Basic, color, ArmorBlocks.BlockType.Stairs) + "_from_stairs"));

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.ARMOR_STAIR_HARDENED),
                RecipeCategory.BUILDING_BLOCKS,
                hardenedArmorStair,
                1
            )
            .unlockedBy("has_hardened_armor_stair", has(ModTags.Items.ARMOR_STAIR_HARDENED))
                    .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID,ArmorBlocks.getName(ArmorBlocks.Tier.Hardened, color, ArmorBlocks.BlockType.Stairs) + "_from_stairs"));

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.ARMOR_STAIR_COMPRESSED),
                RecipeCategory.BUILDING_BLOCKS,
                compressedArmorStair,
                1
            )
            .unlockedBy("has_compressed_armor_stair", has(ModTags.Items.ARMOR_STAIR_COMPRESSED))
                    .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID,ArmorBlocks.getName(ArmorBlocks.Tier.Compressed, color, ArmorBlocks.BlockType.Stairs) + "_from_stairs"));



            // Slab to Slab Recipes
            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.ARMOR_SLAB_BASIC),
                RecipeCategory.BUILDING_BLOCKS,
                basicArmorSlab,
                1
            )
            .unlockedBy("has_basic_armor_slab", has(ModTags.Items.ARMOR_SLAB_BASIC))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID,ArmorBlocks.getName(ArmorBlocks.Tier.Basic, color, ArmorBlocks.BlockType.Slab) + "_from_slab"));

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.ARMOR_SLAB_HARDENED),
                RecipeCategory.BUILDING_BLOCKS,
                hardenedArmorSlab,
                1
            )
            .unlockedBy("has_hardened_armor_slab", has(ModTags.Items.ARMOR_SLAB_HARDENED))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID,ArmorBlocks.getName(ArmorBlocks.Tier.Hardened, color, ArmorBlocks.BlockType.Slab) + "_from_slab"));

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.ARMOR_SLAB_COMPRESSED),
                RecipeCategory.BUILDING_BLOCKS,
                compressedArmorSlab,
                1
            )
            .unlockedBy("has_compressed_armor_slab", has(ModTags.Items.ARMOR_SLAB_COMPRESSED))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID,ArmorBlocks.getName(ArmorBlocks.Tier.Compressed, color, ArmorBlocks.BlockType.Slab) + "_from_slab"));
        }
    }
}

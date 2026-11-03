package de.chaos_cash.aerotechsmp.datagen.Recipe;


import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import de.chaos_cash.aerotechsmp.item.ModItems;
import de.chaos_cash.aerotechsmp.util.ModTags;
import de.chaos_cash.aerotechsmp.util.Rarity;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;
import rearth.oritech.api.recipe.AssemblerRecipeBuilder;
import rearth.oritech.api.recipe.AtomicForgeRecipeBuilder;
import rearth.oritech.api.recipe.ParticleCollisionRecipeBuilder;
import rearth.oritech.init.ItemContent;

import java.util.concurrent.CompletableFuture;


public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        addArmorRecipes(recipeOutput);
        addComponentRecipes(recipeOutput);
    }



    private void addArmorRecipes(@NotNull RecipeOutput recipeOutput) {
        for (ArmorBlocks.Color color : ArmorBlocks.Color.values()) {
            Block lightArmorBlock = ArmorBlocks.getBlock(ArmorBlocks.Tier.Light, color).get();
            Block hardenedArmorBlock = ArmorBlocks.getBlock(ArmorBlocks.Tier.Hardened, color).get();
            Block compressedArmorBlock = ArmorBlocks.getBlock(ArmorBlocks.Tier.Compressed, color).get();

            StairBlock lightArmorStair = ArmorBlocks.getStair(ArmorBlocks.Tier.Light, color).get();
            StairBlock hardenedArmorStair = ArmorBlocks.getStair(ArmorBlocks.Tier.Hardened, color).get();
            StairBlock compressedArmorStair = ArmorBlocks.getStair(ArmorBlocks.Tier.Compressed, color).get();

            SlabBlock lightArmorSlab = ArmorBlocks.getSlab(ArmorBlocks.Tier.Light, color).get();
            SlabBlock hardenedArmorSlab = ArmorBlocks.getSlab(ArmorBlocks.Tier.Hardened, color).get();
            SlabBlock compressedArmorSlab = ArmorBlocks.getSlab(ArmorBlocks.Tier.Compressed, color).get();



            // Hardened collision to Compressed
            ParticleCollisionRecipeBuilder.build()
                    .time(250)
                    .result(compressedArmorBlock.asItem())
                    .input(hardenedArmorBlock)
                    .input(ItemContent.DURATIUM_DUST)
                    .export(recipeOutput, ArmorBlocks.getBlock(ArmorBlocks.Tier.Compressed, color).getId().getPath());

            AtomicForgeRecipeBuilder.build().time(100)
                    .result(hardenedArmorBlock.asItem())
                    .input(lightArmorBlock)
                    .input(Items.OBSIDIAN)
                    .input(ItemContent.STEEL_DUST)
                    .export(recipeOutput, ArmorBlocks.getBlock(ArmorBlocks.Tier.Light, color).getId().getPath());






            // Block to Block Recipes
            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.Armor.Block.LIGHT),
                RecipeCategory.BUILDING_BLOCKS,
                lightArmorBlock,
                1
            )
            .unlockedBy("has_light_armor_block", has(ModTags.Items.Armor.Slab.LIGHT))
            .save(recipeOutput);

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.Armor.Block.HARDENED),
                RecipeCategory.BUILDING_BLOCKS,
                hardenedArmorBlock,
                1
            )
            .unlockedBy("has_hardened_armor_block", has(ModTags.Items.Armor.Block.HARDENED))
            .save(recipeOutput);

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.Armor.Block.COMPRESSED),
                RecipeCategory.BUILDING_BLOCKS,
                compressedArmorBlock,
                1
            )
            .unlockedBy("has_compressed_armor_block", has(ModTags.Items.Armor.Block.COMPRESSED))
            .save(recipeOutput);



            // Block to Stair Recipes
            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.Armor.Block.LIGHT),
                RecipeCategory.BUILDING_BLOCKS,
                lightArmorStair,
                1
            )
            .unlockedBy("has_light_armor_block", has(ModTags.Items.Armor.Block.LIGHT))
            .save(recipeOutput);

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.Armor.Block.HARDENED),
                RecipeCategory.BUILDING_BLOCKS,
                hardenedArmorStair,
                1
            )
            .unlockedBy("has_hardened_armor_block", has(ModTags.Items.Armor.Block.HARDENED))
            .save(recipeOutput);

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.Armor.Block.COMPRESSED),
                RecipeCategory.BUILDING_BLOCKS,
                compressedArmorStair,
                1
            )
            .unlockedBy("has_compressed_armor_block", has(ModTags.Items.Armor.Block.COMPRESSED))
            .save(recipeOutput);



            // Block to Slab Recipes
            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.Armor.Block.LIGHT),
                RecipeCategory.BUILDING_BLOCKS,
                lightArmorSlab,
                2
            )
            .unlockedBy("has_light_armor_block", has(ModTags.Items.Armor.Block.LIGHT))
            .save(recipeOutput);

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.Armor.Block.HARDENED),
                RecipeCategory.BUILDING_BLOCKS,
                hardenedArmorSlab,
                2
            )
            .unlockedBy("has_hardened_armor_block", has(ModTags.Items.Armor.Block.HARDENED))
            .save(recipeOutput);

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.Armor.Block.COMPRESSED),
                RecipeCategory.BUILDING_BLOCKS,
                compressedArmorSlab,
                2
            )
            .unlockedBy("has_compressed_armor_block", has(ModTags.Items.Armor.Block.COMPRESSED))
            .save(recipeOutput);



            // Stair to Stair Recipes
            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.Armor.Stair.LIGHT),
                RecipeCategory.BUILDING_BLOCKS,
                lightArmorStair,
                1
            )
            .unlockedBy("has_light_armor_stair", has(ModTags.Items.Armor.Stair.LIGHT))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID,ArmorBlocks.getName(ArmorBlocks.Tier.Light, color, ArmorBlocks.BlockType.Stairs) + "_from_stairs"));

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.Armor.Stair.HARDENED),
                RecipeCategory.BUILDING_BLOCKS,
                hardenedArmorStair,
                1
            )
            .unlockedBy("has_hardened_armor_stair", has(ModTags.Items.Armor.Stair.HARDENED))
                    .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID,ArmorBlocks.getName(ArmorBlocks.Tier.Hardened, color, ArmorBlocks.BlockType.Stairs) + "_from_stairs"));

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.Armor.Stair.COMPRESSED),
                RecipeCategory.BUILDING_BLOCKS,
                compressedArmorStair,
                1
            )
            .unlockedBy("has_compressed_armor_stair", has(ModTags.Items.Armor.Stair.COMPRESSED))
                    .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID,ArmorBlocks.getName(ArmorBlocks.Tier.Compressed, color, ArmorBlocks.BlockType.Stairs) + "_from_stairs"));



            // Slab to Slab Recipes
            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.Armor.Slab.LIGHT),
                RecipeCategory.BUILDING_BLOCKS,
                lightArmorSlab,
                1
            )
            .unlockedBy("has_light_armor_slab", has(ModTags.Items.Armor.Slab.LIGHT))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID,ArmorBlocks.getName(ArmorBlocks.Tier.Light, color, ArmorBlocks.BlockType.Slab) + "_from_slab"));

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.Armor.Slab.HARDENED),
                RecipeCategory.BUILDING_BLOCKS,
                hardenedArmorSlab,
                1
            )
            .unlockedBy("has_hardened_armor_slab", has(ModTags.Items.Armor.Slab.HARDENED))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID,ArmorBlocks.getName(ArmorBlocks.Tier.Hardened, color, ArmorBlocks.BlockType.Slab) + "_from_slab"));

            SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(ModTags.Items.Armor.Slab.COMPRESSED),
                RecipeCategory.BUILDING_BLOCKS,
                compressedArmorSlab,
                1
            )
            .unlockedBy("has_compressed_armor_slab", has(ModTags.Items.Armor.Slab.COMPRESSED))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID,ArmorBlocks.getName(ArmorBlocks.Tier.Compressed, color, ArmorBlocks.BlockType.Slab) + "_from_slab"));
        }
    }


    private void addComponentRecipes(@NotNull RecipeOutput recipeOutput) {
        for (var rarity : Rarity.values()) {
                if (rarity == Rarity.Common) {
                    var circuitBase = ModItems.CIRCUIT_BASES.get(rarity);
                    ParticleCollisionRecipeBuilder.build().time(5000)
                            .result(circuitBase.get())
                            .input(Items.NETHER_STAR)
                            .input(Items.NETHER_STAR)
                            .export(recipeOutput, circuitBase.getId().getPath());
                }
                else {
                    var circuitBoard = ModItems.CIRCUIT_BOARDS.get(rarity.tryDecrease());
                    var circuitBase = ModItems.CIRCUIT_BASES.get(rarity);

                    ParticleCollisionRecipeBuilder.build().time((int) (500 * Math.pow(2, rarity.toInt())))
                            .result(circuitBase.get())
                            .input(circuitBoard)
                            .input(circuitBoard)
                            .export(recipeOutput, circuitBase.getId().getPath());

                    AssemblerRecipeBuilder.build() // assembler combination
                            .input(circuitBoard)
                            .input(circuitBoard)
                            .input(circuitBoard)
                            .input(circuitBoard)
                            .result(circuitBase.get())
                            .export(recipeOutput, circuitBase.getId().getPath());
                }
        }
    }
}

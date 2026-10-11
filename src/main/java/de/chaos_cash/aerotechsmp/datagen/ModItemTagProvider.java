package de.chaos_cash.aerotechsmp.datagen;

import de.chaos_cash.aerotechsmp.AerotechSMP;

import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlock;
import de.chaos_cash.aerotechsmp.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture,
                              CompletableFuture<TagLookup<Block>> lookupCompletableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, completableFuture, lookupCompletableFuture, AerotechSMP.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        for (ArmorBlock.Color color : ArmorBlock.Color.values())
        {
            tag(ModTags.Items.ARMOR_BASIC)
                    .add(ModBlocks.ARMOR_BLOCKS.get(ArmorBlock.Tier.Basic).get(color).asItem());

            tag(ModTags.Items.ARMOR_HARDENED)
                    .add(ModBlocks.ARMOR_BLOCKS.get(ArmorBlock.Tier.Hardened).get(color).asItem());

            tag(ModTags.Items.ARMOR_COMPRESSED)
                    .add(ModBlocks.ARMOR_BLOCKS.get(ArmorBlock.Tier.Compressed).get(color).asItem());
        }
    }
}

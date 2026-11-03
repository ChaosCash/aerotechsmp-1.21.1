package de.chaos_cash.aerotechsmp.datagen;

import de.chaos_cash.aerotechsmp.AerotechSMP;

import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
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
        for (ArmorBlocks.Color color : ArmorBlocks.Color.values())
        {
            tag(ModTags.Items.Armor.Block.LIGHT)
                    .add(ArmorBlocks.getBlock(ArmorBlocks.Tier.Light, color).asItem());

            tag(ModTags.Items.Armor.Block.HARDENED)
                    .add(ArmorBlocks.getBlock(ArmorBlocks.Tier.Hardened, color).asItem());

            tag(ModTags.Items.Armor.Block.COMPRESSED)
                    .add(ArmorBlocks.getBlock(ArmorBlocks.Tier.Compressed, color).asItem());


            tag(ModTags.Items.Armor.Stair.LIGHT)
                    .add(ArmorBlocks.getStair(ArmorBlocks.Tier.Light, color).asItem());

            tag(ModTags.Items.Armor.Stair.HARDENED)
                    .add(ArmorBlocks.getStair(ArmorBlocks.Tier.Hardened, color).asItem());

            tag(ModTags.Items.Armor.Stair.COMPRESSED)
                    .add(ArmorBlocks.getStair(ArmorBlocks.Tier.Compressed, color).asItem());


            tag(ModTags.Items.Armor.Slab.LIGHT)
                    .add(ArmorBlocks.getSlab(ArmorBlocks.Tier.Light, color).asItem());

            tag(ModTags.Items.Armor.Slab.HARDENED)
                    .add(ArmorBlocks.getSlab(ArmorBlocks.Tier.Hardened, color).asItem());

            tag(ModTags.Items.Armor.Slab.COMPRESSED)
                    .add(ArmorBlocks.getSlab(ArmorBlocks.Tier.Compressed, color).asItem());
        }
    }
}

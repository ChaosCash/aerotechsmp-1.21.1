package de.chaos_cash.aerotechsmp.datagen;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlock;
import de.chaos_cash.aerotechsmp.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, AerotechSMP.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        for (ArmorBlock.Color color : ArmorBlock.Color.values())
        {
            for (ArmorBlock.Tier tier : ArmorBlock.Tier.values())
            {
                tag(BlockTags.MINEABLE_WITH_PICKAXE)
                        .add(ModBlocks.ARMOR_BLOCKS.get(tier).get(color).get());
            }
        }


        for (ArmorBlock.Color color : ArmorBlock.Color.values())
        {
            tag(BlockTags.NEEDS_IRON_TOOL)
                    .add(ModBlocks.ARMOR_BLOCKS.get(ArmorBlock.Tier.Basic).get(color).get())
                    .add(ModBlocks.ARMOR_BLOCKS.get(ArmorBlock.Tier.Hardened).get(color).get());
        }

        for (ArmorBlock.Color color : ArmorBlock.Color.values())
        {
            tag(BlockTags.NEEDS_DIAMOND_TOOL)
                    .add(ModBlocks.ARMOR_BLOCKS.get(ArmorBlock.Tier.Compressed).get(color).get());
        }

    }
}

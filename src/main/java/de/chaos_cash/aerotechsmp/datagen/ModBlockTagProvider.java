package de.chaos_cash.aerotechsmp.datagen;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlock;
import de.chaos_cash.aerotechsmp.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, AerotechSMP.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        for (ArmorBlock.Color color : ArmorBlock.Color.values())
        {
            ArmorBlock basicArmor = ModBlocks.ARMOR_BLOCKS.get(ArmorBlock.Tier.Basic).get(color).get();
            ArmorBlock hardenedArmor = ModBlocks.ARMOR_BLOCKS.get(ArmorBlock.Tier.Hardened).get(color).get();
            ArmorBlock compressedArmor = ModBlocks.ARMOR_BLOCKS.get(ArmorBlock.Tier.Compressed).get(color).get();

            tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(basicArmor)
                    .add(hardenedArmor)
                    .add(compressedArmor);

            tag(BlockTags.NEEDS_IRON_TOOL)
                    .add(basicArmor)
                    .add(hardenedArmor);

            tag(BlockTags.NEEDS_DIAMOND_TOOL)
                    .add(compressedArmor);

            tag(ModTags.Blocks.HEAVY)
                    .add(hardenedArmor);
            tag(ModTags.Blocks.SUPER_HEAVY)
                    .add(compressedArmor);
        }

    }
}

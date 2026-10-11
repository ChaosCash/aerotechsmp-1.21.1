package de.chaos_cash.aerotechsmp.datagen;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import de.chaos_cash.aerotechsmp.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
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
        for (ArmorBlocks.Color color : ArmorBlocks.Color.values())
        {
            Block basicArmor = ModBlocks.ARMOR_BLOCKS.get(ArmorBlocks.Tier.Basic).get(color).get();
            Block hardenedArmor = ModBlocks.ARMOR_BLOCKS.get(ArmorBlocks.Tier.Hardened).get(color).get();
            Block compressedArmor = ModBlocks.ARMOR_BLOCKS.get(ArmorBlocks.Tier.Compressed).get(color).get();
            Block basicArmorStair = ModBlocks.ARMOR_STAIRS.get(ArmorBlocks.Tier.Basic).get(color).get();
            Block hardenedArmorStair = ModBlocks.ARMOR_STAIRS.get(ArmorBlocks.Tier.Hardened).get(color).get();
            Block compressedArmorStair = ModBlocks.ARMOR_STAIRS.get(ArmorBlocks.Tier.Compressed).get(color).get();


            tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(basicArmor)
                    .add(hardenedArmor)
                    .add(compressedArmor)
                    .add(basicArmorStair)
                    .add(hardenedArmorStair)
                    .add(compressedArmorStair);

            tag(BlockTags.NEEDS_IRON_TOOL)
                    .add(basicArmor)
                    .add(hardenedArmor)
                    .add(basicArmorStair)
                    .add(hardenedArmorStair);

            tag(BlockTags.NEEDS_DIAMOND_TOOL)
                    .add(compressedArmor)
                    .add(compressedArmorStair);

            tag(ModTags.Blocks.HEAVY)
                    .add(hardenedArmor)
                    .add(hardenedArmorStair);
            tag(ModTags.Blocks.SUPER_HEAVY)
                    .add(compressedArmor)
                    .add(compressedArmorStair);
        }

    }
}

package de.chaos_cash.aerotechsmp.datagen;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import de.chaos_cash.aerotechsmp.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
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
            Block basicArmorBlock = ArmorBlocks.getBlock(ArmorBlocks.Tier.Light, color).get();
            Block hardenedArmorBlock = ArmorBlocks.getBlock(ArmorBlocks.Tier.Hardened, color).get();
            Block compressedArmorBlock = ArmorBlocks.getBlock(ArmorBlocks.Tier.Compressed, color).get();

            StairBlock basicArmorStair = ArmorBlocks.getStair(ArmorBlocks.Tier.Light, color).get();
            StairBlock hardenedArmorStair = ArmorBlocks.getStair(ArmorBlocks.Tier.Hardened, color).get();
            StairBlock compressedArmorStair = ArmorBlocks.getStair(ArmorBlocks.Tier.Compressed, color).get();

            SlabBlock basicArmorSlab = ArmorBlocks.getSlab(ArmorBlocks.Tier.Light, color).get();
            SlabBlock hardenedArmorSlab = ArmorBlocks.getSlab(ArmorBlocks.Tier.Hardened, color).get();
            SlabBlock compressedArmorSlab = ArmorBlocks.getSlab(ArmorBlocks.Tier.Compressed, color).get();


            tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(basicArmorBlock)
                    .add(hardenedArmorBlock)
                    .add(compressedArmorBlock)

                    .add(basicArmorStair)
                    .add(hardenedArmorStair)
                    .add(compressedArmorStair)

                    .add(basicArmorSlab)
                    .add(hardenedArmorSlab)
                    .add(compressedArmorSlab);


            tag(BlockTags.NEEDS_IRON_TOOL)
                    .add(basicArmorBlock)
                    .add(hardenedArmorBlock)

                    .add(basicArmorStair)
                    .add(hardenedArmorStair)

                    .add(basicArmorSlab)
                    .add(hardenedArmorSlab);


            tag(BlockTags.NEEDS_DIAMOND_TOOL)
                    .add(compressedArmorBlock)
                    .add(compressedArmorStair)
                    .add(compressedArmorSlab);


            tag(ModTags.Blocks.LIGHT)
                    .add(basicArmorSlab);

            tag(ModTags.Blocks.HEAVY)
                    .add(hardenedArmorBlock)
                    .add(hardenedArmorStair)
                    .add(compressedArmorSlab);

            tag(ModTags.Blocks.SUPER_HEAVY)
                    .add(compressedArmorBlock)
                    .add(compressedArmorStair);

            tag(ModTags.Blocks.HALF_VOLUME)
                    .add(basicArmorSlab)
                    .add(hardenedArmorSlab)
                    .add(compressedArmorSlab);
        }

    }
}

package de.chaos_cash.aerotechsmp.datagen;


import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Supplier;


public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, AerotechSMP.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.TEAM_CORE);

        for (ArmorBlocks.Color color : ArmorBlocks.Color.values())
        {
            for (ArmorBlocks.Tier tier : ArmorBlocks.Tier.values())
            {
                var armorBlock = ArmorBlocks.getBlock(tier, color);
                blockWithItem(armorBlock);

                var armorStair = ArmorBlocks.getStair(tier, color);
                stairsBlock(armorStair.get(), blockTexture(armorBlock.get()));
                blockItem(armorStair);

                var armorSlab = ArmorBlocks.getSlab(tier, color);
                slabBlock(armorSlab.get(), blockTexture(armorBlock.get()), blockTexture(armorBlock.get()));
                blockItem(armorSlab);
            }
        }
    }


    private <T extends Block> void blockWithItem(DeferredBlock<T> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }


    private void blockItem(Supplier<? extends Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile(AerotechSMP.MOD_ID + ":block/" +
                BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath()));
    }
}

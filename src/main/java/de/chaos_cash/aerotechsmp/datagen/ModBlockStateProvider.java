package de.chaos_cash.aerotechsmp.datagen;


import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;


public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, AerotechSMP.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (ArmorBlock.Color color : ArmorBlock.Color.values())
        {
            for (ArmorBlock.Tier tier : ArmorBlock.Tier.values())
            {
                blockWithItem(ModBlocks.ARMOR_BLOCKS.get(tier).get(color));
            }
        }
    }


    private <T extends Block> void blockWithItem(DeferredBlock<T> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}

package de.chaos_cash.aerotechsmp.datagen;

import de.chaos_cash.aerotechsmp.block.ModBlocks;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider pRegistries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

    @Override
    protected void generate() {
        for (ArmorBlocks.Color color : ArmorBlocks.Color.values())
        {
            for (ArmorBlocks.Tier tier : ArmorBlocks.Tier.values())
            {
                dropSelf(ArmorBlocks.getBlock(tier, color).get());
                dropSelf(ArmorBlocks.getStair(tier, color).get());

                var armorSlab = ArmorBlocks.getSlab(tier, color).get();
                this.add(
                        armorSlab,
                        block -> createSlabItemTable(armorSlab)
                );
            }
        }

        dropSelf(ModBlocks.TEAM_CORE_BLOCK.get());
    }


    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(e -> (Block) e.value()).toList();
    }
}

package de.chaos_cash.aerotechsmp.block;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import de.chaos_cash.aerotechsmp.block.custom.TeamCoreBlock;
import de.chaos_cash.aerotechsmp.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AerotechSMP.MOD_ID);

    public static final DeferredBlock<TeamCoreBlock> TEAM_CORE = registerBlock("team_core", () -> new TeamCoreBlock(BlockBehaviour.Properties.of()));

    public static final Map<ArmorBlocks.Tier, Map<ArmorBlocks.Color, DeferredBlock<Block>>> ARMOR_BLOCKS = ArmorBlocks.registerBlocks();
    public static final Map<ArmorBlocks.Tier, Map<ArmorBlocks.Color, DeferredBlock<StairBlock>>> ARMOR_STAIRS = ArmorBlocks.registerStairs();
    public static final Map<ArmorBlocks.Tier, Map<ArmorBlocks.Color, DeferredBlock<SlabBlock>>> ARMOR_SLABS = ArmorBlocks.registerSlabs();



    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }


    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block)
    {
        var registeredBlock = BLOCKS.register(name, block);
        registerBlockItem(name, registeredBlock);

        return registeredBlock;
    }


    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block)
    {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
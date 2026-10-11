package de.chaos_cash.aerotechsmp.block.custom;

import de.chaos_cash.aerotechsmp.block.ModBlocks;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;


import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;


public class ArmorBlocks {

    public static DeferredBlock<Block> getBlock(Tier tier, Color color) {return ModBlocks.ARMOR_BLOCKS.get(tier).get(color);}
    public static DeferredBlock<StairBlock> getStair(Tier tier, Color color) {return ModBlocks.ARMOR_STAIRS.get(tier).get(color);}
    public static DeferredBlock<SlabBlock> getSlab(Tier tier, Color color) {return ModBlocks.ARMOR_SLABS.get(tier).get(color);}

    public static String getDisplayName(Tier tier, Color color, BlockType type) {
        String displayName = tier.toString() + " ";

        displayName += switch (color) {
            case Color.Default -> "";
            case Color.Type10 -> "Type 10 ";
            case Color.IVBO -> "4BO ";
            default -> color.name() + " ";
        };

        displayName += "Armor";

        displayName += switch (type){
            case Block -> " Block";
            case Stairs -> " Stairs";
            case Slab -> " Slab";
        };

        return displayName;
    }


    private static BlockBehaviour.Properties getProperties(Tier tier, BlockType type) {
        int strength = switch (tier){
            case Basic -> 24;
            case Hardened -> 28;
            case Compressed -> 32;
        };

        strength += switch (type){
            case Slab -> -4;
            case Stairs -> -2;
            default -> 0;
        };

        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
                .requiresCorrectToolForDrops()
                .strength(strength);

        properties = switch (tier) {
            case Basic -> properties.destroyTime(2.5f).sound(SoundType.COPPER);
            case Hardened -> properties.destroyTime(3f).sound(SoundType.NETHERITE_BLOCK);
            case Compressed -> properties.destroyTime(3.5f).sound(SoundType.NETHERITE_BLOCK);
        };

        return properties;
    }


    public static String getName(Tier tier, Color color, BlockType type) {
        String name = tier.name().toLowerCase();

        name += switch (color) {
            case Color.Default -> "";
            case Color.Type10 -> "_type_10";
            default -> "_" + color.name().toLowerCase();
        };

        name += "_armor";

        name += switch (type)
        {
            case BlockType.Block -> "_block";
            case BlockType.Slab -> "_slab";
            case BlockType.Stairs -> "_stairs";
        };

        return name;
    }


    static <T extends Block> Map<Tier, Map<Color, DeferredBlock<T>>> registerBlocksOfType(BlockType type, BiFunction<Tier, Color, T> newBlockFunction){
        Map<Tier, Map<Color, DeferredBlock<T>>> blocks = new HashMap<>();
        for (Tier tier : Tier.values()) {
            blocks.put(tier, new HashMap<>());
            for (Color color : Color.values()) {
                blocks.get(tier).put(
                    color,
                    ModBlocks.registerBlock(getName(tier, color, type), () -> newBlockFunction.apply(tier, color))
                );
            }
        }

        return blocks;
    }


    public static Map<Tier, Map<Color, DeferredBlock<Block>>> registerBlocks() {
        return registerBlocksOfType(BlockType.Block, (tier, color) -> new Block(getProperties(tier, BlockType.Block)));
    }

    public static Map<Tier, Map<Color, DeferredBlock<StairBlock>>> registerStairs() {
        return registerBlocksOfType(BlockType.Stairs, (tier, color) -> new StairBlock(getBlock(tier, color).get().defaultBlockState() ,getProperties(tier, BlockType.Stairs)));
    }

    public static Map<Tier, Map<Color, DeferredBlock<SlabBlock>>> registerSlabs() {
        return registerBlocksOfType(BlockType.Slab, (tier, color) -> new SlabBlock(getProperties(tier, BlockType.Slab)));
    }




    public enum BlockType
    {
        Block,
        Stairs,
        Slab,
    }

    public enum Tier
    {
        Basic,
        Hardened,
        Compressed
    }

    public enum Color
    {
        Default,
        Charcoal,
        Hide,
        Grizzly,
        Gink,
        Ley,
        Rota,
        Olive,
        Cactus,
        Pine,
        IVBO,
        Scale,
        Gorge,
        Ardenne,
        Parade,
        Type10,
        Algae,
        Azure,
        Horizon,
        Cherenkov,
        Kampfgrau,
        Gravel,
        Slate,
        Panzergrau,
        Sensha,
        Kat,
        Garupan,
        Gelb,
        Patton,
        Camel,
        Desert,
        Dust,
        Snow,
        Blush,
        Coral,
    }
}

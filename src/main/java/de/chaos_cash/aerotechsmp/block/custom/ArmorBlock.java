package de.chaos_cash.aerotechsmp.block.custom;

import de.chaos_cash.aerotechsmp.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zwachinabottle.cannoncompressedarmor.ModMaterials;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ArmorBlock extends Block {


    private ArmorBlock(Tier tier) {
        super(getProperties(tier));
    }

    private static Properties getProperties(Tier tier) {
        Properties properties = Properties.of().requiresCorrectToolForDrops();

        properties = switch (tier) {
            case Tier.Basic -> properties.strength(24).destroyTime(2.5f).sound(SoundType.COPPER);
            case Tier.Hardened -> properties.strength(28).destroyTime(3f).sound(SoundType.NETHERITE_BLOCK);
            case Tier.Compressed -> properties.strength(32).destroyTime(3.5f).sound(SoundType.NETHERITE_BLOCK);
        };

        return properties;
    }


    private static String getName(Tier tier, Color color)
    {
        String name = tier.name().toLowerCase();

        name += switch (color) {
            case Color.Default -> "";
            case Color.Type10 -> "_type_10";
            default -> "_" + color.name().toLowerCase();
        };

        name += "_armor_block";

        return name;
    }


    public static Map<Tier, Map<Color, DeferredBlock<ArmorBlock>>> register()
    {
        Map<Tier, Map<Color, DeferredBlock<ArmorBlock>>> blocks = new HashMap<>();
        for (Tier tier : Tier.values()) {
            blocks.put(tier, new HashMap<>());
            for (Color color : Color.values()) {
                blocks.get(tier).put(
                    color,
                    ModBlocks.registerBlock(getName(tier, color), () -> new ArmorBlock(tier))
                );
            }
        }

        return blocks;
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
        IVBO,
        Algae,
        Ardenne,
        Azure,
        Blush,
        Cactus,
        Camel,
        Charcoal,
        Cherenkov,
        Coral,
        Desert,
        Dust,
        Garupan,
        Gelb,
        Gink,
        Gorge,
        Gravel,
        Grizzly,
        Hide,
        Horizon,
        Kampfgrau,
        Kat,
        Ley,
        Olive,
        Panzergrau,
        Parade,
        Patton,
        Pine,
        Rota,
        Scale,
        Sensha,
        Slate,
        Snow,
        Type10
    }
}

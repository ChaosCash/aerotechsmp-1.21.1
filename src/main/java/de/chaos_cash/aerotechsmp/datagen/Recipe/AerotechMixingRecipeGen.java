package de.chaos_cash.aerotechsmp.datagen.Recipe;

import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.ItemLike;
import rearth.oritech.init.FluidContent;
import rearth.oritech.init.ItemContent;

import java.util.concurrent.CompletableFuture;

public final class AerotechMixingRecipeGen extends MixingRecipeGen {

    public AerotechMixingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Create.ID);
        createRecipes();
    }


    void createRecipes() {
        addSpawnEggRecipe(EntityType.ZOMBIE, Items.ROTTEN_FLESH);
        addSpawnEggRecipe(EntityType.SKELETON, Items.BONE);
        addSpawnEggRecipe(EntityType.CREEPER, Items.GUNPOWDER);
        addSpawnEggRecipe(EntityType.SPIDER, Items.STRING);
        addSpawnEggRecipe(EntityType.BLAZE, Items.BLAZE_ROD);
        addSpawnEggRecipe(EntityType.PIGLIN, Items.GOLD_INGOT);
        addSpawnEggRecipe(EntityType.ENDERMAN, Items.ENDER_PEARL);
        addSpawnEggRecipe(EntityType.SHULKER, Items.SHULKER_SHELL);
        addSpawnEggRecipe(EntityType.BREEZE, Items.BREEZE_ROD);
        addSpawnEggRecipe(EntityType.GUARDIAN, Items.PRISMARINE_SHARD);
        addSpawnEggRecipe(EntityType.SLIME, Items.SLIME_BALL);
        addSpawnEggRecipe(EntityType.PHANTOM, Items.PHANTOM_MEMBRANE);
        addSpawnEggRecipe(EntityType.GHAST, Items.GHAST_TEAR);
        addSpawnEggRecipe(EntityType.MAGMA_CUBE, Items.MAGMA_CREAM);
        addSpawnEggRecipe(EntityType.WITHER_SKELETON, Items.COAL);
        addSpawnEggRecipe(EntityType.WITCH, Items.REDSTONE);
        addSpawnEggRecipe(EntityType.VINDICATOR, Items.EMERALD);

        addSpawnEggRecipe(EntityType.PIG, Items.PORKCHOP);
        addSpawnEggRecipe(EntityType.COW, Items.LEATHER);
        addSpawnEggRecipe(EntityType.SHEEP, Items.WHITE_WOOL);
        addSpawnEggRecipe(EntityType.CHICKEN, Items.FEATHER);
        addSpawnEggRecipe(EntityType.RABBIT, Items.RABBIT_HIDE);
        addSpawnEggRecipe(EntityType.ARMADILLO, Items.ARMADILLO_SCUTE);
        addSpawnEggRecipe(EntityType.BEE, Items.HONEY_BOTTLE);
        addSpawnEggRecipe(EntityType.COD, Items.COD);
        addSpawnEggRecipe(EntityType.SALMON, Items.SALMON);
        addSpawnEggRecipe(EntityType.TROPICAL_FISH, Items.TROPICAL_FISH);
        addSpawnEggRecipe(EntityType.SQUID, Items.INK_SAC);
        addSpawnEggRecipe(EntityType.GLOW_SQUID, Items.GLOW_INK_SAC);
        addSpawnEggRecipe(EntityType.IRON_GOLEM, Items.IRON_INGOT);
        addSpawnEggRecipe(EntityType.MOOSHROOM, Items.RED_MUSHROOM);
        addSpawnEggRecipe(EntityType.TURTLE, Items.TURTLE_SCUTE);
    }


    private void addSpawnEggRecipe(EntityType<?> entityType, ItemLike mobDrop) {
        var spawnEgg = SpawnEggItem.byId(entityType);
        assert spawnEgg != null;
        create(
                spawnEgg.getName(spawnEgg.getDefaultInstance()).getString().toLowerCase().replaceAll(" ", "_"),
                builder -> builder
                        .require(Items.EGG)
                        .require(mobDrop)
                        .require(mobDrop)
                        .require(mobDrop)
                        .require(mobDrop)
                        .require(ItemContent.RAW_BIOPOLYMER)
                        .require(ItemContent.RAW_BIOPOLYMER)
                        .require(ItemContent.RAW_BIOPOLYMER)
                        .require(ItemContent.RAW_BIOPOLYMER)
                        .require(FluidContent.FLOWING_STRANGE_MATTER.get(), 100)
                        .output(spawnEgg)
        );
    }
}

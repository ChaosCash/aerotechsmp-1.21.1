package de.chaos_cash.aerotechsmp.team;

import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import de.chaos_cash.aerotechsmp.item.ModItems;
import de.chaos_cash.aerotechsmp.util.Rarity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RandomItemsReward extends Reward {
    public static final String typeName = "randomItemsReward";

    static final PoolGenerator<@NotNull Integer> pullCountPool = new PoolGenerator<@NotNull Integer>()
            .withEntry(50, 1)
            .withEntry(200, 2)
            .withEntry(50, 3)
            .withEntry(20, 4);

    static final PoolGenerator<ItemStack> commonItemsPool = new PoolGenerator<ItemStack>()
            .withEntry(150, () -> ModItems.CLAIM_SHARD.toStack(2))
            .withEntry(45, ModItems.CLAIM_CAPSULE::toStack)
            .withEntry(5, ModItems.CLAIM_ESSENCE::toStack)
            .withEntry(5, ModItems.FORCE_LOAD_SHARD::toStack)
            .withEntry(45, ArmorBlocks.getBlock(ArmorBlocks.Tier.Hardened, ArmorBlocks.Color.Default).toStack(64 * 3))
            .withEntry(25, ArmorBlocks.getBlock(ArmorBlocks.Tier.Compressed, ArmorBlocks.Color.Default).toStack(64))
            .withEntry(100, ModItems.COMPONENT_BASES.get(Rarity.Common)::toStack)
            .withEntry(20, ModItems.COMPONENT_BASES.get(Rarity.Uncommon)::toStack)
            ;


    static final PoolGenerator<ItemStack> uncommonItemsPool = new PoolGenerator<ItemStack>()
            .withEntry(150, ModItems.CLAIM_CAPSULE::toStack)
            .withEntry(75, () -> ModItems.CLAIM_CAPSULE.toStack(2))
            .withEntry(40, ModItems.CLAIM_ESSENCE::toStack)
            .withEntry(40, ModItems.FORCE_LOAD_SHARD::toStack)
            .withEntry(5, ModItems.FORCE_LOAD_CAPSULE::toStack)
            .withEntry(40, ArmorBlocks.getBlock(ArmorBlocks.Tier.Compressed, ArmorBlocks.Color.Default).toStack(64 * 3))
            .withEntry(100, ModItems.COMPONENT_BASES.get(Rarity.Uncommon)::toStack)
            .withEntry(20, ModItems.COMPONENT_BASES.get(Rarity.Rare)::toStack)
            ;


    static final PoolGenerator<ItemStack> rareItemsPool = new PoolGenerator<ItemStack>()
            .withEntry(150, ModItems.CLAIM_ESSENCE::toStack)
            .withEntry(75, () -> ModItems.CLAIM_ESSENCE.toStack(2))
            .withEntry(40, ModItems.FORCE_LOAD_CAPSULE::toStack)
            .withEntry(5, ModItems.FORCE_LOAD_ESSENCE::toStack)
            .withEntry(20, ArmorBlocks.getBlock(ArmorBlocks.Tier.Compressed, ArmorBlocks.Color.Default).toStack(64 * 9))
            .withEntry(100, ModItems.COMPONENT_BASES.get(Rarity.Rare)::toStack)
            .withEntry(20, ModItems.COMPONENT_BASES.get(Rarity.Epic)::toStack)
            ;


    static final PoolGenerator<ItemStack> epicItemsPool = new PoolGenerator<ItemStack>()
            .withEntry(75, ModItems.FORCE_LOAD_ESSENCE::toStack)
            .withEntry(20, () -> ModItems.CLAIM_ESSENCE.toStack(4))
            .withEntry(100, ModItems.COMPONENT_BASES.get(Rarity.Epic)::toStack)
            .withEntry(20, ModItems.COMPONENT_BASES.get(Rarity.Legendary)::toStack)
            ;


    static final PoolGenerator<ItemStack> legendaryItemsPool = new PoolGenerator<ItemStack>()
            .withEntry(100, ModItems.COMPONENT_BASES.get(Rarity.Legendary)::toStack)
            .withEntry(20, () -> ModItems.COMPONENT_BASES.get(Rarity.Legendary).toStack(2))
            ;



    @Override
    public final @NotNull String getTypeName() {return typeName;}

    @Override
    public @NotNull Reward create(@NotNull Rarity rarity) {
        return new RandomItemsReward(rarity);
    }

    public RandomItemsReward(@NotNull Rarity rarity) {
        super(rarity);
    }

    @Override
    public @NotNull List<ItemStack> getRewards(Team team) {
        return switch(rarity) {
            case Rarity.Common -> commonItemsPool.getRandom(pullCountPool.getRandom());
            case Uncommon -> uncommonItemsPool.getRandom(pullCountPool.getRandom());
            case Rare -> rareItemsPool.getRandom(pullCountPool.getRandom());
            case Epic -> epicItemsPool.getRandom(pullCountPool.getRandom());
            case Legendary -> legendaryItemsPool.getRandom(pullCountPool.getRandom());
        };
    }

}

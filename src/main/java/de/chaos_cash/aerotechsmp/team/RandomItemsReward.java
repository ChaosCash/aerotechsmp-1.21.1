package de.chaos_cash.aerotechsmp.team;

import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RandomItemsReward extends Reward {
    public static final String typeName = "randomItemsReward";

    @Override
    public final @NotNull String getTypeName() {return typeName;}

    @Override
    public @NotNull Reward create(Rarity rarity) {
        return new RandomItemsReward(rarity);
    }

    public RandomItemsReward(Rarity rarity) {
        super(rarity);
    }

    @Override
    public @NotNull List<ItemStack> getRewards(Team team) {
        var items = new ArrayList<ItemStack>();
        items.add(ArmorBlocks.getBlock(ArmorBlocks.Tier.Light, ArmorBlocks.Color.Default).toStack(64));
        return items;
    }
}

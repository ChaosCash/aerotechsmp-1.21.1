package de.chaos_cash.aerotechsmp.team;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MoneyReward extends Reward {
    public static final String typeName = "moneyReward";


    protected MoneyReward(@NotNull Rarity rarity) {
        super(rarity);
    }


    @Override
    public @NotNull List<ItemStack> getRewards() {
        return List.of(Items.GOLD_INGOT.getDefaultInstance());
    }

    @Override
    public @NotNull String getTypeName() {
        return typeName;
    }

    @Override
    public @NotNull Reward create(Rarity rarity) {
        return new MoneyReward(rarity);
    }
}

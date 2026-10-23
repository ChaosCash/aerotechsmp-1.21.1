package de.chaos_cash.aerotechsmp.team;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Reward {
    private static Reward[] rewardTypes;

    @NotNull final Rarity rarity;



    protected Reward(@NotNull Rarity rarity) {
        this.rarity = rarity;
    }



    static Reward getInstance(CompoundTag tag) {
        var rarity = stringToRarity(tag.getString("rarity"));

        return switch (tag.getString("type")) {
            case RandomItemsReward.typeName -> new RandomItemsReward(rarity);
            case MoneyReward.typeName -> new MoneyReward(rarity);
            default -> throw new RuntimeException("Reward type not found");
        };
    }

    public static Reward[] getRewardTypes() {
        if (rewardTypes == null)
            rewardTypes = new Reward[]{new RandomItemsReward(Rarity.Common), new MoneyReward(Rarity.Common)};
        return rewardTypes;
    }


    public @NotNull CompoundTag save() {
        var tag =  new CompoundTag();
        tag.putString("type", getTypeName());
        tag.putString("rarity", rarity.toString());
        return tag;

    }


    public static Rarity stringToRarity(String string){
        return switch (string){
            case "Uncommon" -> Rarity.Uncommon;
            case "Rarity" -> Rarity.Rare;
            case "Epic" -> Rarity.Epic;
            case "Legendary" -> Rarity.Legendary;
            default -> Rarity.Common;
        };
    }


    public abstract @NotNull List<ItemStack> getRewards();

    public abstract @NotNull String getTypeName();

    public abstract @NotNull Reward create(Rarity rarity);


    public enum Rarity {
        Common,
        Uncommon,
        Rare,
        Epic,
        Legendary,
    }
}

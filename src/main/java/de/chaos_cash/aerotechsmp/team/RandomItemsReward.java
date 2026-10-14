package de.chaos_cash.aerotechsmp.team;

import de.chaos_cash.aerotechsmp.block.custom.ArmorBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RandomItemsReward implements IReward {
    public static final String typeName = "randomItems";

    public final Rarity rarity;

    public RandomItemsReward(Rarity rarity) {
        this.rarity = rarity;
    }

    @Override 
    public @NotNull List<ItemStack> getRewards() {
        var items = new ArrayList<ItemStack>();
        items.add(ArmorBlocks.getBlock(ArmorBlocks.Tier.Light, ArmorBlocks.Color.Default).toStack(64));
        return items;
    }

    public static @NotNull IReward load(@NotNull CompoundTag tag) {
        return new RandomItemsReward(stringToRarity(tag.getString("rarity")));
    }

    @Override
    public @NotNull CompoundTag save() {
        var tag =  new CompoundTag();
        tag.putString("type", typeName);
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


    public enum Rarity {
        Common,
        Uncommon,
        Rare,
        Epic,
        Legendary,
    }
}

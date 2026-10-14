package de.chaos_cash.aerotechsmp.team;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IReward {
    @NotNull List<ItemStack> getRewards();
    @NotNull CompoundTag save();

    static IReward getInstance(CompoundTag tag) throws Exception {
        return switch (tag.getString("type")){
            case RandomItemsReward.typeName -> RandomItemsReward.load(tag);
            default -> throw new Exception("Reward type not found");
        };
    }
}

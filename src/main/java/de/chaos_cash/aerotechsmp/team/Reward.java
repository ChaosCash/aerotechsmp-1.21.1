package de.chaos_cash.aerotechsmp.team;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public abstract class Reward {
    private static Reward[] rewardTypes;

    @NotNull final Rarity rarity;



    protected Reward(@NotNull Rarity rarity) {
        this.rarity = rarity;
    }



    static Reward getInstance(CompoundTag tag) {
        var rarity = Rarity.fromString(tag.getString("rarity"));

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


    public abstract @NotNull List<ItemStack> getRewards(Team team);

    public abstract @NotNull String getTypeName();

    public abstract @NotNull Reward create(Rarity rarity);


    public enum Rarity {
        Common(ChatFormatting.GRAY),
        Uncommon(ChatFormatting.GREEN),
        Rare(ChatFormatting.BLUE),
        Epic(ChatFormatting.LIGHT_PURPLE),
        Legendary(ChatFormatting.GOLD);

        public final ChatFormatting color;

        Rarity(ChatFormatting color) {
            this.color = color;
        }


        public static Rarity fromString(String string){
            return switch (string){
                case "Uncommon" -> Rarity.Uncommon;
                case "Rarity" -> Rarity.Rare;
                case "Epic" -> Rarity.Epic;
                case "Legendary" -> Rarity.Legendary;
                default -> Rarity.Common;
            };
        }

        public static Rarity fromInt(int integer){
            return switch (integer){
                case 1 -> Rarity.Uncommon;
                case 2 -> Rarity.Rare;
                case 3 -> Rarity.Epic;
                case 4 -> Rarity.Legendary;
                default -> Rarity.Common;
            };
        }

        public static int toInt(Rarity rarity) {
            return switch (rarity){
                case Uncommon -> 1;
                case Rare -> 2;
                case Epic -> 3;
                case Legendary -> 4;
                default -> 0;
            };
        }

        public int toInt() {return toInt(this);}

        public static final double tierExponentiationBase = 3;

        public static double getMultiplier(Rarity rarity) {return Math.pow(tierExponentiationBase, rarity.toInt());}

        public double getMultiplier(){
            return getMultiplier(this);
        }
    }
}

package de.chaos_cash.aerotechsmp.util;

import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.Nullable;

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


    public static @Nullable Rarity fromString(String string){
        return switch (string){
            case "Uncommon" -> Rarity.Uncommon;
            case "Rarity" -> Rarity.Rare;
            case "Epic" -> Rarity.Epic;
            case "Legendary" -> Rarity.Legendary;
            default -> null;
        };
    }

    public static @Nullable Rarity fromInt(int integer){
        return switch (integer){
            case 1 -> Rarity.Common;
            case 2 -> Rarity.Uncommon;
            case 3 -> Rarity.Rare;
            case 4 -> Rarity.Epic;
            case 5 -> Rarity.Legendary;
            default -> null;
        };
    }

    public static int toInt(@Nullable Rarity rarity) {
        return switch (rarity){
            case null -> 0;
            case Common -> 1;
            case Uncommon -> 2;
            case Rare -> 3;
            case Epic -> 4;
            case Legendary -> 5;
        };
    }

    public int toInt() {return toInt(this);}

    public static final double rarityExponentiationBase = 2.5;

    public static double getMultiplier(@Nullable Rarity rarity) {
        if (rarity == null)
            return 0;

        return Math.pow(rarityExponentiationBase, rarity.toInt());
    }

    public double getMultiplier(){
        return getMultiplier(this);
    }

    public double getAccumulatedMultiplier(){
        double multiplier = 0;
        for (var rarity : Rarity.values()) {
            multiplier += getMultiplier(rarity);
            if (rarity == this)
                break;
        }

        return multiplier;
    }

    public static Rarity tryIncrease(@Nullable Rarity rarity) {
        return switch (rarity) {
            case null -> Common;
            case Common -> Uncommon;
            case Uncommon -> Rare;
            case Rare -> Epic;
            case Epic, Legendary -> Legendary;
        };
    }

    public Rarity tryIncrease() {
        return tryIncrease(this);
    }
}
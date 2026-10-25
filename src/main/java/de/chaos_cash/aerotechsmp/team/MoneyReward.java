package de.chaos_cash.aerotechsmp.team;

import dev.ithundxr.createnumismatics.Numismatics;
import dev.ithundxr.createnumismatics.content.backend.BankAccount;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class MoneyReward extends Reward {
    public static final String typeName = "moneyReward";
    public static int baseAmount = 100;



    protected MoneyReward(@NotNull Rarity rarity) {
        super(rarity);
    }


    @Override
    public @NotNull List<ItemStack> getRewards(Team team) {
        int depositAmount = (int) (baseAmount * rarity.getMultiplier());
        Numismatics.BANK.getOrCreateAccount(team.getOwner(), BankAccount.Type.PLAYER).deposit(depositAmount);
        team.sendTeamMessage(Component.literal(depositAmount + " Credits have been deposited in the Bank Account of the leader of your Team!").withColor(Color.yellow.getRGB()));
        return List.of();
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

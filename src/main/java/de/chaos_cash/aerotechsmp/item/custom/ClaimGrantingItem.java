package de.chaos_cash.aerotechsmp.item.custom;

import de.chaos_cash.aerotechsmp.team.Team;
import de.chaos_cash.aerotechsmp.util.Messages;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClaimGrantingItem extends ConsumableItem {
    public final int claimIncreaseCount;
    public final int claimLimit;

    public ClaimGrantingItem(@Nullable Messages successMessage, int claimIncreaseCount, int claimLimit, Component toolTip) {
        super(new Properties(), 10, successMessage, Messages.ClaimGrantingLimitReached, toolTip);
        this.claimIncreaseCount = claimIncreaseCount;
        this.claimLimit = claimLimit;
    }


    @Override
    protected boolean onUse(@NotNull Level level, @NotNull Player player, @NotNull ItemStack itemStack) {
        var optTeam = Team.get(player);
        if (optTeam.isEmpty()) {
            if (level.isClientSide())
                Messages.ActionRequiresTeam.send(player);
            return false;
        }

        var team = optTeam.get();
        if (team.getMaxClaims() < claimLimit) {
            if (!level.isClientSide())
                optTeam.get().increaseMaxClaims(claimIncreaseCount);
            return true;
        }
        else
            return false;
    }
}

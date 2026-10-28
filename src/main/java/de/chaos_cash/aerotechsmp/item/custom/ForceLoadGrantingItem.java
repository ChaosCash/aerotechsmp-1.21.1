package de.chaos_cash.aerotechsmp.item.custom;

import de.chaos_cash.aerotechsmp.team.Team;
import de.chaos_cash.aerotechsmp.util.Messages;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForceLoadGrantingItem extends ConsumableItem {
    public final int forceLoadIncreaseCount;

    public ForceLoadGrantingItem(@Nullable Messages successMessage, int forceLoadGrantCount) {
        super(new Properties(), 10, successMessage, Messages.HardMaxForceLoadLimitReached);

        this.forceLoadIncreaseCount = forceLoadGrantCount;
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
        if (team.getMaxForceLoads() < Team.hardMaxForceLoads) {
            if (!level.isClientSide())
                optTeam.get().increaseMaxForceLoads(forceLoadIncreaseCount);
            return true;
        }
        else
            return false;
    }
}

package de.chaos_cash.aerotechsmp.team;

import de.chaos_cash.aerotechsmp.phases.PVPPhase;
import de.chaos_cash.aerotechsmp.phases.PhaseHandler;
import de.chaos_cash.aerotechsmp.util.Messages;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class RewardHandler {

    static Reward[] getTeamCoreRewards() {
        return new Reward[]{new RandomItemsReward(RandomItemsReward.Rarity.Legendary)};
    }

    public static void onTeamCoreBreak(@NotNull Player breakingPlayer, @NotNull Team team) {

        var optTeam = Team.get(breakingPlayer);
        if (optTeam.isEmpty()){
            Messages.ActionRequiresTeam.send(breakingPlayer);
            return;
        }

        var breakingTeam = optTeam.get();

        if (breakingTeam == team) return;
        if (!(PhaseHandler.getPhase() instanceof PVPPhase)) return;

        for (var reward : getTeamCoreRewards())
            breakingTeam.addReward(reward);

        breakingTeam.sendTeamMessage(
                Messages.TeamCoreDestructionRewardsGranted.getTranslation()
        );
    }


    public static void grantTeamCoreGuardedRewards(@NotNull Team team) {
        for (var reward : getTeamCoreRewards())
            team.addReward(reward);

        team.sendTeamMessage(Messages.TeamCoreGuardedRewardsGranted.getTranslation());
    }
}

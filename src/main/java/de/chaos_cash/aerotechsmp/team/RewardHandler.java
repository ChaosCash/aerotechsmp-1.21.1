package de.chaos_cash.aerotechsmp.team;

import de.chaos_cash.aerotechsmp.phases.PVPPhase;
import de.chaos_cash.aerotechsmp.phases.PhaseHandler;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class RewardHandler {

    static Reward[] getTeamCoreBreakRewards() {
        return new Reward[]{new RandomItemsReward(RandomItemsReward.Rarity.Legendary)};
    }

    public static void onTeamCoreBreak(@NotNull Player breakingPlayer, @NotNull Team team) {
        var optTeam = Team.get(breakingPlayer);
        if (optTeam.isEmpty()){
            ChatHandler.sendMessage(
                    breakingPlayer,
                    Component.literal("You have broken an Enemy Team Core but have Received no rewards because you are not part of a Team.")
                        .withColor(Color.red.getRGB())
            );
            return;
        }

        var breakingTeam = optTeam.get();
        if (breakingTeam == team){
            if (PhaseHandler.getPhase() instanceof PVPPhase)
                ChatHandler.sendMessage(
                    breakingPlayer,
                    Component.literal("You have broken your own Team Core! You can not replace it in this Phase!")
                        .withColor(Color.red.getRGB())
                );
            return;
        }

        if (!(PhaseHandler.getPhase() instanceof PVPPhase)){
            ChatHandler.sendMessage(
                    breakingPlayer,
                    Component.literal("You have broken an enemy Team Core but will not receive Rewards for it in this Phase.")
                            .withColor(Color.red.getRGB())
            );
            return;
        }

        var rewards = getTeamCoreBreakRewards();

        for (var reward : rewards)
            breakingTeam.addReward(reward);

        breakingTeam.sendTeamMessage(
                Component.literal(breakingPlayer.getDisplayName().getString() + " has broken an enemy Team Core! Your Team has received Rewards")
                        .withColor(Color.green.getRGB())
        );
    }


}

package de.chaos_cash.aerotechsmp.phases;

import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.team.RewardHandler;
import de.chaos_cash.aerotechsmp.team.Team;
import de.chaos_cash.aerotechsmp.util.Messages;
import dev.ftb.mods.ftbchunks.FTBChunksAPIImpl;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class DominationPhase extends PVPPhase {
    public DominationPhase() {
        super(72000, new BreakPhase(12000, new DeathmatchPhase()));
    }



    @Override
    public void onActivation() {
        super.onActivation();

        deleteAllClaims();
    }

    @Override
    public void onDeactivation() {
        super.onDeactivation();

        grantRemainingTeamCoresRewards();
    }

    @Override
    public String getDisplayName() {
        return "Domination-Phase";
    }


    static void deleteAllClaims()
    {
        var claimedChunks = FTBChunksAPIImpl.INSTANCE.getManager().getAllClaimedChunks().stream().toList();
        var stack = AerotechSMP.getServer().createCommandSourceStack();
        for (var chunk : claimedChunks)
        {
            chunk.unclaim(stack, true);
        }

        Messages.AllClaimsDeleted.broadcast();
    }


    private void grantRemainingTeamCoresRewards() {
        for (var team : Team.list())
            if (team.hasCore())
                RewardHandler.grantTeamCoreGuardedRewards(team);
    }



    @SubscribeEvent
    @Override
    protected void onBossBarLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        super.onBossBarLogIn(event);
    }

    @SubscribeEvent
    @Override
    protected void onTick(ServerTickEvent.Post event) {
        super.onTick(event);
    }
}

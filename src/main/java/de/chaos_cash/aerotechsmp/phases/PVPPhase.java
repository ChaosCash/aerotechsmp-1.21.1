package de.chaos_cash.aerotechsmp.phases;

import de.chaos_cash.aerotechsmp.datagen.Language.Messages;
import de.chaos_cash.aerotechsmp.team.Team;
import dev.ftb.mods.ftbchunks.FTBChunksWorldConfig;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.List;

public class PVPPhase extends TimedPhase{
    private final int breakLength;
    private final List<Team> teamsWithCoreOnStart = new ArrayList<>();

    public PVPPhase(int length, int breakLength) {
        super(length, null);
        this.breakLength = breakLength;
    }



    public void onTeamCoreDestruction(Team coreTeam, Team destroyingTeam) {
        destroyingTeam.sendTeamMessage(Messages.EnemyTeamCoreDestroyed);
        coreTeam.sendTeamMessage(Messages.OwnTeamCoreDestroyed);
        destroyingTeam.grantCoreReward();
    }


    @Override
    public String getDisplayName() {
        return "PVP-Phase";
    }

    @Override
    public SoundEvent getActivationSound() {
        return SoundEvents.WITHER_SPAWN;
    }

    @Override
    public float getActivationSoundVolume() {
        return 0.75f;
    }


    @Override
    public void onActivation() {
        super.onActivation();

        activeDuration = 0;

        FTBChunksWorldConfig.DISABLE_PROTECTION.set(true);
        FTBChunksWorldConfig.onConfigChanged(true);

        if (nextPhase == null) {
            for (var team : Team.list())
                if (team.hasCore() && !teamsWithCoreOnStart.contains(team)) {
                    teamsWithCoreOnStart.add(team);
                }
            nextPhase = new BreakPhase(breakLength, this);
        }
        else
            nextPhase = new WaitForRestartPhase();
    }

    @Override
    public void onDeactivation() {
        super.onDeactivation();

        FTBChunksWorldConfig.DISABLE_PROTECTION.set(false);
        FTBChunksWorldConfig.onConfigChanged(true);

        if (nextPhase instanceof PeacefulPhase)
            for (var team : teamsWithCoreOnStart) {
                if (team.hasCore()) {
                    team.grantCoreReward();
                    team.sendTeamMessage(Messages.TeamCoreSurvived);
                }
                else
                    team.setHasCore(true);
            }
    }


    @Override
    @SubscribeEvent
    protected void onTick(ServerTickEvent.Post event) {
        super.onTick(event);
    }
}
